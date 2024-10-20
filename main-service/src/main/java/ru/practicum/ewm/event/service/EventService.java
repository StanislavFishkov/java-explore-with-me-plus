package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewm.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    EventFullDto addEvent(Long id, NewEventDto newEventDto);

    List<EventShortDto> getEventsByUserId(Long id);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequestDto eventUpdateDto);

    EventFullDto update(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventFullDto> get(EventsFilterParamsDto filters);

    List<ParticipationRequestDto> getEventAllParticipationRequests(Long eventId, Long userId);

    EventRequestStatusUpdateResult changeEventState(Long userId, Long eventId,
                                                    EventRequestStatusUpdateRequest requestStatusUpdateRequest);
}