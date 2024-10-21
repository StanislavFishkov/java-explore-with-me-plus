package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.core.util.DateTimeUtil;
import ru.practicum.ewm.core.validation.DateTimeRange;
import ru.practicum.ewm.event.model.EventStates;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@DateTimeRange(before = "rangeStart", after = "rangeEnd")
public class EventAdminFilterParamsDto {
    List<Long> users;
    List<EventStates> states;
    List<Long> categories;
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    LocalDateTime rangeEnd;
}