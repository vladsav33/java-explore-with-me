package ru.practicum.afisha.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.afisha.models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(long userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    Optional<Event> findByIdAndPublishedOnIsNotNull(long eventId);

    boolean existsByCategoryId(long categoryId);
}
