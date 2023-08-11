package ru.practicum.afisha.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.CommentDto;
import ru.practicum.afisha.dto.mappers.CommentMapper;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.exceptions.ConflictException;
import ru.practicum.afisha.exceptions.NoSuchComment;
import ru.practicum.afisha.exceptions.NoSuchEvent;
import ru.practicum.afisha.exceptions.NoSuchUser;
import ru.practicum.afisha.models.Comment;
import ru.practicum.afisha.models.Event;
import ru.practicum.afisha.models.User;
import ru.practicum.afisha.repositories.CommentRepository;
import ru.practicum.afisha.repositories.EventRepository;
import ru.practicum.afisha.repositories.UserRepository;
import ru.practicum.afisha.services.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper mapper;

    public CommentDto createCommentByAdmin(long userId, long eventId, CommentDto commentDto) {
        checkEventState(eventId);
        Comment comment = createComment(commentDto, userId, eventId);
        return mapper.toCommentDto(repository.save(comment));
    }

    public CommentDto createCommentByPrivate(long userId, long eventId, CommentDto commentDto) {
        checkEventState(eventId);
        checkEventOwner(userId, eventId);
        Comment comment = createComment(commentDto, userId, eventId);
        return mapper.toCommentDto(repository.save(comment));
    }

    public CommentDto updateCommentByIdAdmin(long userId, long eventId, long commentId, CommentDto commentDto) {
        Comment comment = getComment(commentId);
        checkEventState(eventId);
        comment.setCommentText(commentDto.getCommentText());
        return mapper.toCommentDto(repository.save(comment));
    }

    public CommentDto updateCommentByIdPrivate(long userId, long eventId, long commentId, CommentDto commentDto) {
        Comment comment = getComment(commentId);
        checkEventState(eventId);
        checkEventOwner(userId, eventId);
        comment.setCommentText(commentDto.getCommentText());
        return mapper.toCommentDto(repository.save(comment));
    }

    public List<CommentDto> getCommentsByEventAdmin(long eventId) {
        return getCommentsDto(eventId);
    }

    public List<CommentDto> getCommentsByEventPrivate(long userId, long eventId) {
        checkEventOwner(userId, eventId);
        return getCommentsDto(eventId);
    }

    private void checkEventState(long eventId) {
        Event event = getEvent(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Event is already published");
        }
    }

    private void checkEventOwner(long userId, long eventId) {
        Event event = getEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException("Can't access comments on the event created by someone else");
        }
    }

    private Comment createComment(CommentDto commentDto, long userId, long eventId) {
        commentDto.setUpdated(LocalDateTime.now());
        Event event = getEvent(eventId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUser("User was not found"));
        Comment comment = mapper.toComment(commentDto);
        comment.setUser(user);
        comment.setEvent(event);
        return comment;
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoSuchEvent("Event was not found"));
    }

    private Comment getComment(long commentId) {
        return repository.findById(commentId).orElseThrow(() -> new NoSuchComment("Comment was not found"));
    }

    private List<CommentDto> getCommentsDto(long eventId) {
        return repository.findAllByEventId(eventId).stream().map(mapper::toCommentDto).collect(Collectors.toList());
    }
}
