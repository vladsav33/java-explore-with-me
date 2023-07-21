package ru.practicum.afisha.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.dto.StatDto;
import ru.practicum.afisha.services.StatService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class StatController {
    private final StatService service;

    @Autowired
    public StatController(StatService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDto createStat(@RequestBody StatDto statDto, HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return service.createStat(statDto);
    }

    @GetMapping("/stats")
    public List<StatCountDto> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(required = false) String[] uris,
                                      @RequestParam(defaultValue = "false") Boolean unique) {
        GetStatDto getStatDto = new GetStatDto(start, end, uris, unique);
        log.info("request for hits starting {} and ending {}", getStatDto.getStart(), getStatDto.getEnd());
            return service.getStat(getStatDto);
    }
}
