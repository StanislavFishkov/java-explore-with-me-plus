package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventsFilterParamsDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.ewm.event.model.EventStates;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.stats.utils.DateTimeUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.update(eventId, updateEventAdminRequestDto);
    }

    @GetMapping
    public List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(required = false) List<EventStates> states,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) String rangeStart,
                                  @RequestParam(required = false) String rangeEnd,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        EventsFilterParamsDto filters = EventsFilterParamsDto.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart == null ? null : LocalDateTime.parse(
                        URLDecoder.decode(rangeStart, StandardCharsets.UTF_8), DateTimeUtil.DATE_TIME_FORMATTER)
                )
                .rangeEnd(rangeEnd == null ? null : LocalDateTime.parse(
                        URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8), DateTimeUtil.DATE_TIME_FORMATTER)
                )
                .from(from)
                .size(size)
                .build();

        return eventService.get(filters);
    }
}