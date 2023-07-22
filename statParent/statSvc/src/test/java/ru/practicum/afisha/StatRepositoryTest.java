package ru.practicum.afisha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.model.Stat;
import ru.practicum.afisha.repositories.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class StatRepositoryTest {
    @Autowired
    private StatRepository repository;
    private final String[] uris = new String[3];
    private List<StatCountDto> result;

    @BeforeEach
    void initTest() {
        Stat stat;
        stat = Stat.builder().id(1).app("client").timestamp(LocalDateTime.now()).ip("192.168.0.1").uri("/events/1").build();
        repository.save(stat);
        stat = Stat.builder().id(2).app("client").timestamp(LocalDateTime.now()).ip("192.168.0.2").uri("/events").build();
        repository.save(stat);
        stat = Stat.builder().id(3).app("client").timestamp(LocalDateTime.now()).ip("192.168.0.2").uri("/events").build();
        repository.save(stat);
        uris[0] = "/events";
    }

    @Test
    void getStatUniqueIP() {
        result = repository.getStatUniqueIP(LocalDateTime.now().minusSeconds(10), LocalDateTime.now().plusSeconds(10), uris);
        assertEquals(1, result.get(0).getHits());
    }

    @Test
    void getStatNonUniqueIP() {
        result = repository.getStatNonUniqueIP(LocalDateTime.now().minusSeconds(10), LocalDateTime.now().plusSeconds(10), uris);
        assertEquals(2, result.get(0).getHits());
    }

    @Test
    void getStatUniqueIPNoUris() {
        result = repository.getStatNonUniqueIPNoUris(LocalDateTime.now().minusSeconds(10), LocalDateTime.now().plusSeconds(10));
        assertEquals(2, result.get(0).getHits());
    }

    @Test
    void getStatNonUniqueIPNoUris() {
        result = repository.getStatNonUniqueIPNoUris(LocalDateTime.now().minusSeconds(10), LocalDateTime.now().plusSeconds(10));
        assertEquals(2, result.get(0).getHits());
        assertEquals(1, result.get(1).getHits());
    }
}