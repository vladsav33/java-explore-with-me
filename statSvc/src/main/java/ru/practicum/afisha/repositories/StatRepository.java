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
    String queryCommon = "from Stat as st " +
            "where st.timestamp between ?1 and ?2 ";
    String queryUniqueTop = "select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(distinct st.ip)) ";
    String queryNonUniqueTop = "select new ru.practicum.afisha.dto.StatCountDto(st.app, st.uri, count(st.ip)) ";
    String queryUniqueBottom = "group by st.app, st.uri " +
                               "order by count(distinct st.ip) desc";
    String queryNonUniqueBottom = "group by st.app, st.uri " +
                                  "order by count(st.ip) desc";
    String queryUris = "and st.uri in ?3 ";

    @Query(queryUniqueTop + queryCommon + queryUris + queryUniqueBottom)
    List<StatCountDto> getStatUniqueIP(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(queryNonUniqueTop + queryCommon + queryUris + queryNonUniqueBottom)
    List<StatCountDto> getStatNonUniqueIP(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(queryUniqueTop + queryCommon + queryUniqueBottom)
    List<StatCountDto> getStatUniqueIPNoUris(LocalDateTime start, LocalDateTime end);

    @Query(queryNonUniqueTop + queryCommon + queryNonUniqueBottom)
    List<StatCountDto> getStatNonUniqueIPNoUris(LocalDateTime start, LocalDateTime end);
}
