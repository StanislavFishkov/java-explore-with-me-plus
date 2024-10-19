package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.event.dto.EventRequestDto;
import ru.practicum.ewm.event.dto.EventResponseDto;
import ru.practicum.ewm.event.dto.EventResponseShort;
import ru.practicum.ewm.event.dto.EventUpdateDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {


    EventResponseDto toEventResponseDto(Event event);

    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(source = "userFromRequest", target = "initiator")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "category", target = "category")
    Event toEvent(EventRequestDto eventRequestDto, Category category, User userFromRequest);

    EventResponseShort toEventResponseShort(Event event);

    @Mapping(target = "event.publishedOn", ignore = true)
    @Mapping(source = "userFromRequest", target = "initiator")
    @Mapping(target = "eventUpdateDto.id", ignore = true)
    @Mapping(source = "categoryEntity", target = "category")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "eventUpdateDto.annotation", target = "annotation")
    @Mapping(source = "eventUpdateDto.description", target = "description")
    @Mapping(source = "eventUpdateDto.eventDate", target = "eventDate")
    @Mapping(source = "eventUpdateDto.location", target = "location")
    @Mapping(source = "eventUpdateDto.paid", target = "paid")
    @Mapping(source = "eventUpdateDto.participantLimit", target = "participantLimit")
    @Mapping(source = "eventUpdateDto.requestModeration", target = "requestModeration")
    @Mapping(source = "eventUpdateDto.title", target = "title")
    Event toEventFromUpdateDto(EventUpdateDto eventUpdateDto, Category categoryEntity, User userFromRequest, Event event);

    @Mapping(target = "category", ignore = true)
    void update(EventUpdateDto eventUpdateDto, @MappingTarget Event event);
}