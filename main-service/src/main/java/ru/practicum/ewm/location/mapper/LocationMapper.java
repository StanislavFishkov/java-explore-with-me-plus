package ru.practicum.ewm.location.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationRequestDto;
import ru.practicum.ewm.location.dto.UpdateLocationRequestDto;
import ru.practicum.ewm.location.model.Location;


@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toDto(Location location);

    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationRequestDto locationRequestDto);

    @Mapping(target = "id", ignore = true)
    Location toLocation(LocationDto locationDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Location update(@MappingTarget Location location, UpdateLocationRequestDto updateLocationRequestDto);
}