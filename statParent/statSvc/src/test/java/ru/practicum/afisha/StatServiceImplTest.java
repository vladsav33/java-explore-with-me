package ru.practicum.afisha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.dto.StatDto;
import ru.practicum.afisha.model.Stat;
import ru.practicum.afisha.model.StatMapper;
import ru.practicum.afisha.repositories.StatRepository;
import ru.practicum.afisha.services.StatServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatServiceImplTest {
    @Mock
    private StatRepository repository;
    private final StatMapper statMapper = Mappers.getMapper(StatMapper.class);
    private StatServiceImpl service;
    private LocalDateTime start;
    private LocalDateTime end;
    private final String[] uris = new String[]{"/events/1"};
    private StatCountDto statCountDto;

    @BeforeEach
    void initTest() {
        service = new StatServiceImpl(repository, statMapper);
        start = LocalDateTime.of(2020, 5, 5, 0, 0, 0);
        end = LocalDateTime.of(2035, 5, 5, 0, 0, 0);
        statCountDto = StatCountDto.builder().app("client").uri("events/1").hits(1).build();
    }

    @Test
    void createStat() {
        Stat stat = Stat.builder().id(1).app("client").timestamp(LocalDateTime.now()).ip("192.168.0.2").uri("/events").build();
        when(repository.save(any(Stat.class))).thenReturn(stat);
        StatDto statDtoResult = service.createStat(statMapper.toStatDto(stat));
        assertEquals(statMapper.toStatDto(stat), statDtoResult);
    }

    @Test
    void getStatUniqueIp() {
        GetStatDto getStatDto = GetStatDto.builder().start(LocalDateTime.of(2020, 5, 5, 0, 0, 0))
                .end(LocalDateTime.of(2035, 5, 5, 0, 0, 0))
                .uris(new String[]{"/events/1"})
                .unique(true)
                .build();
        when(repository.getStatUniqueIP(start, end, uris)).thenReturn(List.of(statCountDto));
        List<StatCountDto> result = service.getStat(getStatDto);
        assertEquals(List.of(statCountDto), result);
    }

    @Test
    void getStatNonUniqueIp() {
        GetStatDto getStatDto = GetStatDto.builder().start(LocalDateTime.of(2020, 5, 5, 0, 0, 0))
                .end(LocalDateTime.of(2035, 5, 5, 0, 0, 0))
                .uris(new String[]{"/events/1"})
                .unique(false)
                .build();
        when(repository.getStatNonUniqueIP(start, end, uris)).thenReturn(List.of(statCountDto));
        List<StatCountDto> result = service.getStat(getStatDto);
        assertEquals(List.of(statCountDto), result);
    }
}