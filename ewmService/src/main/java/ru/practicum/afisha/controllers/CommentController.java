package ru.practicum.afisha.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.CommentDto;
import ru.practicum.afisha.services.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {
    private final CommentService service;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentPrivate(@PathVariable Long userId, @PathVariable Long eventId,
                                           @Valid @RequestBody CommentDto commentDto) {
        return service.createCommentByPrivate(userId, eventId, commentDto);
    }

    @PostMapping("/admin/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentAdmin(@PathVariable Long userId, @PathVariable Long eventId,
                                         @Valid @RequestBody CommentDto commentDto) {
        return service.createCommentByAdmin(userId, eventId, commentDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto updateCommentPrivate(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long commentId,
                                           @Valid @RequestBody CommentDto commentDto) {
        return service.updateCommentByIdPrivate(userId, eventId, commentId, commentDto);
    }

    @PatchMapping("/admin/{userId}/events/{eventId}/comments/{commentId}")
    public CommentDto updateCommentAdmin(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long commentId,
                                           @Valid @RequestBody CommentDto commentDto) {
        return service.updateCommentByIdAdmin(userId, eventId, commentId, commentDto);
    }

    @GetMapping("/users/events/{eventId}/comments")
    public List<CommentDto> getCommentsAdmin(@PathVariable Long eventId) {
        return service.getCommentsByEventAdmin(eventId);
    }

    @GetMapping("/admin/{userId}/events/{eventId}/comments")
    public List<CommentDto> getCommentsPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getCommentsByEventPrivate(userId, eventId);
    }
}
