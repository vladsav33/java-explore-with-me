package ru.practicum.afisha.services;

import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.dto.StatDto;

import java.util.List;

public interface StatService {
    StatDto createStat(StatDto statDto);

    List<StatCountDto> getStat(GetStatDto getStatDto);
}
