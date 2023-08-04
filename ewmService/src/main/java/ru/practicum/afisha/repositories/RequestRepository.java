package ru.practicum.afisha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.afisha.models.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester(long userId);

    @Query("select req " +
           "from Request req, Event ev " +
           "where req.event = ev.id " +
           "and ev.initiator.id = ?1 " +
           "and ev.id = ?2")
    List<Request> findRequestsForUserId(long userId, long eventId);

    boolean existsByRequesterAndEvent(long userId, long eventId);
}
