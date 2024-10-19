package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;

import java.util.List;

public interface EventService {
    EventResponseDto addEvent(Long id, EventRequestDto eventRequestDto);

    List<EventResponseDto> getEventsByUserId(Long id);

    EventResponseDto getEventById(Long userId, Long eventId);

    EventResponseDto updateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto);
}
