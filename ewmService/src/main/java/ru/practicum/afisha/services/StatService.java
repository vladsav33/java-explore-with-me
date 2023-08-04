package ru.practicum.afisha.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.StatClient;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.practicum.afisha.variables.Variables.STAT_SERVER;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatService {
    private final StatClient client;

    public void addStatEvent(HttpServletRequest request) {
        log.info("Adding statistics to {}", STAT_SERVER);
        StatDto statDto = new StatDto();
        statDto.setApp("ewmService");
        statDto.setIp(request.getRemoteAddr());
        statDto.setUri(request.getRequestURI());
        client.createStat(statDto);
    }

    public long getStatEvent(HttpServletRequest request) {
        GetStatDto getStatDto = new GetStatDto(LocalDateTime.of(2000, 1, 1, 0, 0,0),
                LocalDateTime.of(2050, 1, 1, 0, 0, 0),
                new String[]{request.getRequestURI()}, true);
        return client.getStat(getStatDto);
    }
}
