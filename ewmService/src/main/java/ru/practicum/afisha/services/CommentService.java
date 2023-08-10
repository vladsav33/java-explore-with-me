package ru.practicum.afisha.services;

import ru.practicum.afisha.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createCommentByAdmin(long userId, long eventId, CommentDto commentDto);

    CommentDto createCommentByPrivate(long userId, long eventId, CommentDto commentDto);

    CommentDto updateCommentByIdPrivate(long userId, long eventId, long commentId, CommentDto commentDto);

    CommentDto updateCommentByIdAdmin(long userId, long eventId, long commentId, CommentDto commentDto);

    List<CommentDto> getCommentsByEventAdmin(long eventId);

    List<CommentDto> getCommentsByEventPrivate(long userId, long eventId);

}
