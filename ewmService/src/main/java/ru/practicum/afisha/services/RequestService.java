package ru.practicum.afisha.services;

import ru.practicum.afisha.dto.EventRequestStatusUpdateRequest;
import ru.practicum.afisha.dto.EventRequestStatusUpdateResult;
import ru.practicum.afisha.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getRequestsByUserId(long userId);

    RequestDto createRequest(long userId, long eventId);

    RequestDto cancelRequest(long userId, long eventId);

    List<RequestDto> getRequestsByUserIdPrivate(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestByUserIdPrivate(long userId, long eventId,
        EventRequestStatusUpdateRequest request);
}
