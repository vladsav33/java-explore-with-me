package ru.practicum.afisha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

public class StatClient extends BaseClient {

    public StatClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStat(GetStatDto getStatDto) {
        Map<String, Object> parameters = Map.of(
                "start", getStatDto.getStart().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                "end", getStatDto.getEnd().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                "uris", getStatDto.getUris(),
                "unique", getStatDto.isUnique()
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> createStat(StatDto statDto) {
        statDto.setTimestamp(LocalDateTime.now());
        return post("/hit", statDto);
    }
}
