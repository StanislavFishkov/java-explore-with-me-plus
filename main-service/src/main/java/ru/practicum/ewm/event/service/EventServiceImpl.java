package ru.practicum.ewm.event.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.core.error.exception.ValidationException;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStates;
import ru.practicum.ewm.event.model.StateAction;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;


    @Override
    public EventResponseDto addEvent(Long id, EventRequestDto eventRequestDto) {
        checkEventTime(eventRequestDto.getEventDate());
        Category category = categoriesRepository.findById(eventRequestDto.getCategory()).get();
        User user = userRepository.findById(id).get();

        locationRepository.save(eventRequestDto.getLocation());
        return eventMapper.toEventResponseDto(eventRepository.save(eventMapper.toEvent(eventRequestDto, category, user)));
    }

    @Override
    public List<EventResponseDto> getEventsByUserId(Long id) {
        return eventRepository.findAllByInitiator_Id(id)
                .stream()
                .map(eventMapper::toEventResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        return eventMapper.toEventResponseDto(eventRepository.findById(id).get());
    }

    @Override
    public EventResponseDto updateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId).get();
        eventMapper.update(eventUpdateDto, event);
        if (eventUpdateDto.getStateAction() != null) {
            setStateToEvent(eventUpdateDto, event);
        }

        event.setId(eventId);
        return eventMapper.toEventResponseDto(eventRepository.save(event));
    }

    private void setStateToEvent(EventUpdateDto eventUpdateDto, Event event) {
        if (eventUpdateDto.getStateAction().toString().toLowerCase()
                .equals(StateAction.CANCEL_REVIEW.toString().toLowerCase())) {
            event.setState(EventStates.CANCELED);
        }
    }

    private void checkEventTime(LocalDateTime eventDate) {
        log.info("Проверяем дату события на корректность: {}", eventDate);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime correctEventTime = eventDate.plusHours(2);
        if (correctEventTime.isBefore(now)) {
            log.info("дата не корректна");
            throw new ValidationException("Дата события должна быть +2 часа вперед");
        }

    }
}
