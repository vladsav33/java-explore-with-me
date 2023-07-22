package ru.practicum.afisha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.afisha.dto.GetStatDto;
import ru.practicum.afisha.dto.StatDto;

import java.util.Map;

@Component
public class StatClient extends BaseClient {

    @Autowired
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
                "start", getStatDto.getStart(),
                "end", getStatDto.getEnd(),
                "uris", getStatDto.getUris(),
                "unique", getStatDto.isUnique()
        );
        return get("/stats", parameters);
    }

    public ResponseEntity<Object> createStat(StatDto statDto) {
        return post("/hit", statDto);
    }
}
