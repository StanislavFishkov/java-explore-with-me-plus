package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.core.util.DateTimeUtil;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;


    @JsonFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private Location location;

    @JsonProperty(defaultValue = "false")
    private Boolean paid;

    @PositiveOrZero(message = "Необходимо указать количество участников")
    @JsonProperty(defaultValue = "0")
    private int participantLimit;

    @JsonProperty(defaultValue = "true")
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}