package ru.practicum.afisha.model;

import org.mapstruct.Mapper;
import ru.practicum.afisha.dto.StatDto;

@Mapper(componentModel = "spring")
public interface StatMapper {
    Stat toStat(StatDto statDto);

    StatDto toStatDto(Stat stat);
}
