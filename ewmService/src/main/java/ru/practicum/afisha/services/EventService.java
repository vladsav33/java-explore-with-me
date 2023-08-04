package ru.practicum.afisha.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.afisha.dto.EventDto;
import ru.practicum.afisha.dto.NewEventDto;
import ru.practicum.afisha.dto.UpdateEventAdminRequestDto;
import ru.practicum.afisha.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto createEvent(long userId, NewEventDto eventDto);

    List<EventDto> getEvents(long userId, Pageable page);

    EventDto getEventById(long userId, long eventId);

    EventDto updateEvent(long userId, long eventId, UpdateEventAdminRequestDto updateEvent);

    List<EventDto> getEventsAdmin(Long[] userIds, EventState[] states, Long[] categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    EventDto updateEventAdmin(long eventId, UpdateEventAdminRequestDto updateEvent);

    List<EventDto> getEventsPublic(Boolean paid, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onylAvailable, String text, Pageable page, HttpServletRequest request);

    EventDto getEventByIdPublic(long eventId, HttpServletRequest request);

    void updateEventView(long eventId, long views);
}
