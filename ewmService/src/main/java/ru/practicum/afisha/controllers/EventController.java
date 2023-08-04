package ru.practicum.afisha.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.EventDto;
import ru.practicum.afisha.dto.NewEventDto;
import ru.practicum.afisha.dto.UpdateEventAdminRequestDto;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.enums.SortOrder;
import ru.practicum.afisha.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto eventDto) {
        log.info("Added an event from the user {}", userId);
        return service.createEvent(userId, eventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventDto> getEvents(@PathVariable long userId, @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        Pageable page = PageRequest.of(from / size, size);
        log.info("Looking for events for the user {}", userId);
        return service.getEvents(userId, page);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEventById(@PathVariable long userId, @PathVariable long eventId) {
        return service.getEventById(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto updateEvent(@PathVariable long userId, @PathVariable long eventId, @Valid @RequestBody UpdateEventAdminRequestDto eventDto) {
        return service.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/admin/events")
    public List<EventDto> getEventsAdmin(@RequestParam (required = false) Long[] users,
                                         @RequestParam (required = false) EventState[] states,
                                         @RequestParam (required = false) Long[] categories,
                                         @RequestParam (required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeStart,
                                         @RequestParam (required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable page = PageRequest.of(from / size, size);
        log.info("Looking for events (admin endpoint) for users {} and categories {}", users, categories);
        return service.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, page);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto updateEventAdmin(@PathVariable long eventId, @Valid @RequestBody UpdateEventAdminRequestDto updateEvent) {
        log.info("Updated the event with the category {}", updateEvent.getCategory());
        return service.updateEventAdmin(eventId, updateEvent);
    }

    @GetMapping("/events")
    public List<EventDto> getEventsPublic(@Valid @RequestParam(required = false) @Size(min = 1, max = 7000) @Pattern(regexp = "\\D+\\.*") String text,
                                          @RequestParam(required = false) Long[] categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime rangeEnd,
                                          @RequestParam(required = false) Boolean onlyAvailable,
                                          @RequestParam(defaultValue = "EVENT_DATE") SortOrder sort,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Pageable page = null;
        if (sort == SortOrder.EVENT_DATE) {
            page = PageRequest.of(from / size, size, Direction.DESC, "eventDate");
        }
        if (sort == SortOrder.VIEWS) {
            page = PageRequest.of(from / size, size, Direction.DESC, "views");
        }
        return service.getEventsPublic(paid, categories, rangeStart, rangeEnd, onlyAvailable, text, page, request);
    }

    @GetMapping("/events/{eventId}")
    public EventDto getEventsByIdPublic(@PathVariable long eventId, HttpServletRequest request) {
        return service.getEventByIdPublic(eventId, request);
    }
}
