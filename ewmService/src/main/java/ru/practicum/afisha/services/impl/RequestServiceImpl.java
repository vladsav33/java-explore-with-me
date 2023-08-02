package ru.practicum.afisha.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.EventRequestStatusUpdateRequest;
import ru.practicum.afisha.dto.EventRequestStatusUpdateResult;
import ru.practicum.afisha.dto.RequestDto;
import ru.practicum.afisha.dto.mappers.RequestMapper;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.enums.RequestState;
import ru.practicum.afisha.exceptions.ConflictException;
import ru.practicum.afisha.exceptions.NoSuchEvent;
import ru.practicum.afisha.exceptions.NoSuchRequest;
import ru.practicum.afisha.exceptions.NoSuchUser;
import ru.practicum.afisha.models.Event;
import ru.practicum.afisha.models.Request;
import ru.practicum.afisha.repositories.EventRepository;
import ru.practicum.afisha.repositories.RequestRepository;
import ru.practicum.afisha.repositories.UserRepository;
import ru.practicum.afisha.services.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper mapper;

    public List<RequestDto> getRequestsByUserId(long userId) {
        return repository.findAllByRequester(userId).stream().map(mapper::toRequestDto).collect(Collectors.toList());
    }

    public RequestDto createRequest(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchUser("No such user"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchEvent("No such event"));
        if (repository.existsByRequesterAndEvent(userId, eventId)
                || (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() > 0)
                || event.getState() != EventState.PUBLISHED
                || event.getInitiator().getId() == userId) {
            throw new ConflictException("Request can't be added");
        }
        Request request = Request.builder().requester(userId).event(eventId).created(LocalDateTime.now())
                .build();
        if (event.isRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(RequestState.PENDING);
        } else {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        repository.save(request);
        return mapper.toRequestDto(request);
    }

    public RequestDto cancelRequest(long userId, long eventId) {
        Request request = repository.findById(eventId).orElseThrow(() -> new NoSuchRequest("No such request"));
        request.setStatus(RequestState.CANCELED);
        repository.save(request);
        return mapper.toRequestDto(request);
    }

    public List<RequestDto> getRequestsByUserIdPrivate(long userId, long eventId) {
       List<Request> requests = repository.findRequestsForUserId(userId, eventId);
       if (requests.isEmpty()) {
           return new ArrayList<>();
       } else {
           return requests.stream().map(mapper::toRequestDto).collect(Collectors.toList());
       }
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestByUserIdPrivate(long userId, long eventId,
        EventRequestStatusUpdateRequest updateRequest) {
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchEvent("No such event"));
        Arrays.stream(updateRequest.getRequestIds()).forEach(value -> {
            Request request = repository.findById(value).orElseThrow(() -> new NoSuchRequest("No such request"));
            if (request.getStatus() == RequestState.CONFIRMED && updateRequest.getStatus() == RequestState.REJECTED) {
                throw new ConflictException("Can't reject confirmed request");
            }
            request.setStatus(updateRequest.getStatus());
            if (updateRequest.getStatus() == RequestState.CONFIRMED) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                if (event.getConfirmedRequests() > event.getParticipantLimit()) {
                    throw new ConflictException("Participation limit exceeded");
                }
                confirmedRequests.add(mapper.toRequestDto(request));
            }
            if (updateRequest.getStatus() == RequestState.REJECTED) {
                rejectedRequests.add(mapper.toRequestDto(request));
            }
            repository.save(request);
        });
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (!confirmedRequests.isEmpty()) {
            result.setConfirmedRequests(confirmedRequests.toArray(RequestDto[]::new));
        }
        if (!rejectedRequests.isEmpty()) {
            result.setRejectedRequests(rejectedRequests.toArray(RequestDto[]::new));
        }
        return result;
    }
}
