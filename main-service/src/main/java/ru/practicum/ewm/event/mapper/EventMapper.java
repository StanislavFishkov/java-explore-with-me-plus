package ru.practicum.ewm.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class})
public interface EventMapper {
    EventResponseDto toEventResponseDto(Event event);

    EventResponseShort toEventResponseShort(Event event);

    EventFullDto toFullDto(Event event);

    List<EventFullDto> toFullDto(List<Event> event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "userFromRequest")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "participantLimit", defaultValue = "0")
    @Mapping(target = "paid", defaultValue = "false")
    @Mapping(target = "requestModeration", defaultValue = "true")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    Event toEvent(EventRequestDto eventRequestDto, Category category, User userFromRequest, Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "location", source = "location")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event update(@MappingTarget Event event, EventUpdateDto eventUpdateDto, Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "location", source = "location")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event update(@MappingTarget Event event, UpdateEventAdminRequestDto eventUpdateDto, Category category, Location location);
}