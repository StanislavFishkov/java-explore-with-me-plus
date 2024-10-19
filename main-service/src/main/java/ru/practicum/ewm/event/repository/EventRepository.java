package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiator_Id(Long id);
    Event findByIdAndInitiator_Id(Long id, Long initiatorId);
}
