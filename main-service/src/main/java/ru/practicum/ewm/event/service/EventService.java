package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;

import java.util.List;

public interface EventService {
    EventResponseDto addEvent(Long id, EventRequestDto eventRequestDto);

    List<EventResponseDto> getEventsByUserId(Long id);

    EventResponseDto getEventById(Long id);

    EventResponseDto updateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    EventFullDto update(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
