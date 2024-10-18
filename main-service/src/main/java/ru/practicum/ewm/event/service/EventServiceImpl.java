package ru.practicum.ewm.event.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.repository.LocationRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        Category category = categoriesRepository.findById(eventRequestDto.getCategory()).get();
        User user = userRepository.findById(id).get();
        locationRepository.save(eventRequestDto.getLocation());
        System.out.println(eventMapper.toEvent(eventRequestDto, category, user));
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
    public EventResponseDto updateEvent(Long userId, Long eventId, EventRequestDto eventRequestDto) {
/*        User user = userRepository.findById(userId).get();
        Category category = categoriesRepository.findById(eventRequestDto.getCategory()).get();
        Event event = eventMapper.toEvent(eventRequestDto, category, user);
        event.setId(eventId);
        return eventMapper.toEventResponseDto(eventRepository.save(event));
        */
        return null;
    }
}
