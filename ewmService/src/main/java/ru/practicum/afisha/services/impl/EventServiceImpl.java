package ru.practicum.afisha.services.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.EventDto;
import ru.practicum.afisha.dto.NewEventDto;
import ru.practicum.afisha.dto.UpdateEventAdminRequestDto;
import ru.practicum.afisha.dto.mappers.EventMapper;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.enums.StateAction;
import ru.practicum.afisha.exceptions.ConflictException;
import ru.practicum.afisha.exceptions.NoSuchCategory;
import ru.practicum.afisha.exceptions.NoSuchEvent;
import ru.practicum.afisha.exceptions.NoSuchUser;
import ru.practicum.afisha.exceptions.WrongDataException;
import ru.practicum.afisha.models.Category;
import ru.practicum.afisha.models.Event;
import ru.practicum.afisha.models.QEvent;
import ru.practicum.afisha.models.User;
import ru.practicum.afisha.repositories.CategoryRepository;
import ru.practicum.afisha.repositories.EventRepository;
import ru.practicum.afisha.repositories.UserRepository;
import ru.practicum.afisha.services.EventService;
import ru.practicum.afisha.services.StatService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper mapper;
    private final StatService statService;

    public EventDto createEvent(long userId, NewEventDto newEventDto) {
        Event event = mapper.toEvent(newEventDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUser("User was not found"));
        event.setInitiator(user);

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new WrongDataException("The event can't be sooner than in 2 hours");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NoSuchCategory("Category was not found"));
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        return mapper.toEventDto(eventRepository.save(event));
    }

    public List<EventDto> getEvents(long userId, Pageable page) {
        return eventRepository.findAllByInitiatorId(userId, page).stream().map(mapper::toEventDto).collect(Collectors.toList());
    }

    public EventDto getEventById(long userId, long eventId) {
        return mapper.toEventDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NoSuchEvent("No such event")));
    }

    public EventDto updateEvent(long userId, long eventId, UpdateEventAdminRequestDto updateEvent) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NoSuchEvent("No such event"));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event must be later than current time + 2 hours");
        }
        if (event.getState() != EventState.CANCELED && event.getState() != EventState.PENDING) {
            throw new ConflictException("Wrong event state");
        }
        updateEventByUpdateRequest(event, updateEvent);
        return mapper.toEventDto(eventRepository.save(event));
    }

    public List<EventDto> getEventsAdmin(Long[] userIds, EventState[] states, Long[] categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean isPending,
                                         Pageable page) {

        BooleanExpression expression = QEvent.event.isNotNull();
        if (userIds != null) {
            expression = QEvent.event.initiator.id.in(userIds);
        }
        if (states != null) {
            expression = expression.and(QEvent.event.state.in(states));
        }
        if (categories != null) {
            expression = expression.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(QEvent.event.eventDate.between(rangeStart, rangeEnd));
        }
        if (isPending != null) {
            expression = expression.and(QEvent.event.state.eq(EventState.PENDING));
        }

        Page<Event> eventList = eventRepository.findAll(expression, page);

        return eventList.stream()
                .map(mapper::toEventDto)
                .collect(Collectors.toList());
    }

    public EventDto updateEventAdmin(long eventId, UpdateEventAdminRequestDto updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchEvent("No such event"));
        if (updateEvent.getStateAction() == StateAction.PUBLISH_EVENT
                && event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Can't publish events starting in less than one hour");
        }
        if (updateEvent.getStateAction() == StateAction.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new ConflictException("Can publish events only in the PENDING state");
        }
        if (updateEvent.getStateAction() == StateAction.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Can't reject events that were already published");
        }

        updateEventByUpdateRequest(event, updateEvent);
        return mapper.toEventDto(eventRepository.save(event));
    }

    public List<EventDto> getEventsPublic(Boolean paid, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Boolean onlyAvailable, String text, Pageable page, HttpServletRequest request) {
        BooleanExpression expression = QEvent.event.isNotNull();
        if (text != null) {
            expression = expression.and(QEvent.event.annotation.lower().contains(text.toLowerCase())
                    .or(QEvent.event.description.lower().contains(text.toLowerCase())));
        }
        if (paid != null) {
            expression = expression.and(QEvent.event.paid.eq(paid));
        }
        if (categories != null) {
            expression = expression.and(QEvent.event.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(QEvent.event.eventDate.between(rangeStart, rangeEnd));
        } else {
            expression = expression.and(QEvent.event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable != null) {
            expression = expression.and(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit));
        }

        statService.addStatEvent(request);
        return eventRepository.findAll(expression, page).stream()
                .map(mapper::toEventDto).collect(Collectors.toList());
    }

    public EventDto getEventByIdPublic(long eventId, HttpServletRequest request) {
        statService.addStatEvent(request);
        updateEventView(eventId, statService.getStatEvent(request));
        return mapper.toEventDto(eventRepository.findByIdAndPublishedOnIsNotNull(eventId)
                .orElseThrow(() -> new NoSuchEvent("No such event")));
    }

    public void updateEventView(long eventId, long views) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(eventId)
                .orElseThrow(() -> new NoSuchEvent("No such event"));
        event.setViews(views);
        eventRepository.save(event);
    }

    private void updateEventByUpdateRequest(Event event, UpdateEventAdminRequestDto updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NoSuchCategory("No such category"));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now())) {
                throw new WrongDataException("Can't change event date to before the current time");
            }
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocationList(new double[]{updateEvent.getLocation().getLat(), updateEvent.getLocation().getLon()});
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
            }
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }
}
