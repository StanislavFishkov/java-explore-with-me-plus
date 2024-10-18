package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.dto.EventResponseShort;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class})
public interface EventMapper {


    EventResponseDto toEventResponseDto(Event event);

    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(source = "userFromRequest", target = "initiator")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "category", target = "category")
    Event toEvent(EventRequestDto eventRequestDto, Category category, User userFromRequest);

    EventResponseShort toEventResponseShort(Event event);
}