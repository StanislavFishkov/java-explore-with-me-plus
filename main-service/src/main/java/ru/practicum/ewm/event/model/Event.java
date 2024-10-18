package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    private final LocalDateTime createdOn = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    private Category category;
    private String description;
    private LocalDateTime eventDate;
    @ManyToOne
    private Location location;
    private Boolean paid = false;
    private int participantLimit = 0;
    private Boolean requestModeration = true;
    private String title;
    @ManyToOne
    private User initiator;
    private EventStates state = EventStates.PENDING;
    private int views;
    private LocalDateTime publishedOn;
}
