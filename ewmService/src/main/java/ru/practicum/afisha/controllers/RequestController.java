package ru.practicum.afisha.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.EventRequestStatusUpdateRequest;
import ru.practicum.afisha.dto.EventRequestStatusUpdateResult;
import ru.practicum.afisha.dto.RequestDto;
import ru.practicum.afisha.services.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getRequestsByUserId(@PathVariable long userId) {
        return service.getRequestsByUserId(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info("Created a request for the event {} from the user {}", eventId, userId);
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return service.cancelRequest(userId, requestId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsByUserIdPrivate(@PathVariable long userId, @PathVariable long eventId) {
        return service.getRequestsByUserIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestPrivate(@PathVariable long userId, @PathVariable long eventId,
        @RequestBody EventRequestStatusUpdateRequest request) {
        return service.updateRequestByUserIdPrivate(userId, eventId, request);
    }
}
