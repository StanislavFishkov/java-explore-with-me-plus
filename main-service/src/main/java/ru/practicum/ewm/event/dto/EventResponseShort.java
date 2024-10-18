package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.categories.model.Category;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseShort {
    private Long id;
    private String annotation;
    private Category category;
    private LocalDateTime eventDate;
    private Boolean paid;
    private String title;
}

