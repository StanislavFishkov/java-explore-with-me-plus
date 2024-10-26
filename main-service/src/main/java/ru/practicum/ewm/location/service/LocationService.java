package ru.practicum.ewm.location.service;

import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationRequestDto;
import ru.practicum.ewm.location.dto.UpdateLocationRequestDto;

import java.util.List;

public interface LocationService {

    List<LocationDto> getLocations(Integer from, Integer size);

    LocationDto getById(Long locationId);

    LocationDto addLocation(LocationRequestDto locationRequestDto);

    LocationDto updateLocation(Long locationId, UpdateLocationRequestDto updateLocationRequestDto);

    void delete(Long locationId);
}
