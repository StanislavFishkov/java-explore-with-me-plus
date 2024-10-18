package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event.service.EventService;

import java.util.List;


@RequiredArgsConstructor
@Validated
@RestController
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/{userId}/events")
    public Object createEvent(@PathVariable("userId") Long userId, @Valid @RequestBody EventRequestDto eventRequestDto) {
        return eventService.addEvent(userId, eventRequestDto);
    }


    @GetMapping(path = "/users/{userId}/events")
    public List<EventResponseDto> getEvent(@PathVariable("userId") Long userId) {
        return eventService.getEventsByUserId(userId);
    }

    @PatchMapping(path = "/users/{userId}/events/{eventId}")
    public Object updateEvent(@PathVariable("userId") Long userId,
                              @PathVariable("eventId") Long eventId,
                              @Valid @RequestBody EventUpdateDto eventUpdateDto) {
        return eventService.updateEvent(userId, eventId, eventUpdateDto);
    }


}
