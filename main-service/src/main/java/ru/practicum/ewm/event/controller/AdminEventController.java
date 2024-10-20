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
    public List<EventFullDto> get(@RequestParam List<Long> users, @RequestParam List<EventStates> states,
                                  @RequestParam List<Long> categories, @RequestParam String rangeStart,
                                  @RequestParam String rangeEnd, @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        EventsFilterParamsDto filters = EventsFilterParamsDto.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(LocalDateTime.parse(
                        URLDecoder.decode(rangeStart, StandardCharsets.UTF_8), DateTimeUtil.DATE_TIME_FORMATTER)
                )
                .rangeEnd(LocalDateTime.parse(
                        URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8), DateTimeUtil.DATE_TIME_FORMATTER)
                )
                .from(from)
                .size(size)
                .build();

        return eventService.get(filters);
    }
}