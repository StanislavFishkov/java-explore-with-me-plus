package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequestDto;
import ru.practicum.ewm.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;

import java.util.List;


@RequiredArgsConstructor
@Validated
@RestController
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    //Добавление нового события
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users/{userId}/events")
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    //Получение событий, добавленных текущим пользователем
    @GetMapping(path = "/users/{userId}/events")
    public List<EventShortDto> getEvent(@PathVariable("userId") Long userId) {
        return eventService.getEventsByUserId(userId);
    }

    //Получение полной информации о событии добавленном текущим пользователем
    @GetMapping(path = "/users/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable("userId") Long userId, @PathVariable("eventId") Long eventId) {
        return eventService.getEventById(userId, eventId);
    }

    //Изменение события добавленного текущим пользователем
    @PatchMapping(path = "/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequestDto eventUpdateDto) {
        return eventService.updateEvent(userId, eventId, eventUpdateDto);
    }

    //Получение информации о запросах на участие в событии текущего пользователя
    @GetMapping(path = "/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable("userId") Long userId,
                                                                  @PathVariable("eventId") Long eventId) {
        return eventService.getEventAllParticipationRequests(userId, eventId);
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    @PatchMapping(path = "/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updatetEventRequestStatus(@PathVariable("userId") Long userId,
                                                                    @PathVariable("eventId") Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.changeEventState(userId, eventId, request);
    }

}