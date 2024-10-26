package ru.practicum.ewm.location.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.core.error.exception.NotFoundException;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationRequestDto;
import ru.practicum.ewm.location.dto.UpdateLocationRequestDto;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LocationServiceImpl implements  LocationService{
    LocationRepository locationRepository;
    LocationMapper locationMapper;

    @Override
    public List<LocationDto> getLocations(Integer from, Integer size) {
        return null;
    }

    @Override
    public LocationDto getById(Long locationId) {
        return null;
    }

    @Override
    @Transactional
    public LocationDto addLocation(LocationRequestDto locationRequestDto) {
        Location location = locationRepository.save(locationMapper.toLocation(locationRequestDto));
        log.info("Location is created: {}", location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional
    public LocationDto updateLocation(Long locationId, UpdateLocationRequestDto updateLocationRequestDto) {
        log.info("start updateLocation");
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("Location with id " + locationId + " not found"));
        location = locationRepository.save(locationMapper.update(location, updateLocationRequestDto));
        log.info("Location is updated: {}", location);
        return locationMapper.toDto(location);
    }

    @Override
    @Transactional
    public void delete(Long locationId) {
        locationRepository.deleteById(locationId);
        log.info("Location deleted with id: {}", locationId);
    }
}
