package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import ru.practicum.afisha.dto.RequestDto;
import ru.practicum.afisha.models.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toRequestDto(Request request);
}
