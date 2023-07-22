package ru.practicum.afisha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.afisha.dto.StatCountDto;
import ru.practicum.afisha.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query("select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(distinct st.ip)) " +
           "from Stat as st " +
           "where st.timestamp between ?1 and ?2 " +
           "and st.uri in ?3 " +
           "group by st.app, st.uri " +
           "order by count(distinct st.ip) desc")
    List<StatCountDto> getStatUniqueIP(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(st.ip)) " +
            "from Stat as st " +
            "where st.timestamp between ?1 and ?2 " +
            "and st.uri in ?3 " +
            "group by st.app, st.uri " +
            "order by count(st.ip) desc")
    List<StatCountDto> getStatNonUniqueIP(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(distinct st.ip)) " +
            "from Stat as st " +
            "where st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri " +
            "order by count(distinct st.ip) desc")
    List<StatCountDto> getStatUniqueIPNoUris(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(st.ip)) " +
            "from Stat as st " +
            "where st.timestamp between ?1 and ?2 " +
            "group by st.app, st.uri " +
            "order by count(st.ip) desc")
    List<StatCountDto> getStatNonUniqueIPNoUris(LocalDateTime start, LocalDateTime end);
}
