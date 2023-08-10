package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import ru.practicum.afisha.dto.CommentDto;
import ru.practicum.afisha.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentDto commentDto);

    CommentDto toCommentDto(Comment comment);
}
