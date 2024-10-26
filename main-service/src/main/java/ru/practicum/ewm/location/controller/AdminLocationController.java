package ru.practicum.ewm.location.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationRequestDto;
import ru.practicum.ewm.location.dto.UpdateLocationRequestDto;
import ru.practicum.ewm.location.service.LocationService;

@RestController
@RequestMapping(path = "/admin/locations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminLocationController {
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto addLocation(@RequestBody @Valid LocationRequestDto locationRequestDto) {
        log.info("POST /admin/locations with body({})", locationRequestDto);
        return locationService.addLocation(locationRequestDto);
    }

    @PatchMapping("/{locationId}")
    public LocationDto updateLocation(@PathVariable(name = "locationId") Long locationId,
                                      @RequestBody @Valid UpdateLocationRequestDto updateLocationRequestDto) {
        log.info("PATCH /admin/locations with body({})", updateLocationRequestDto);
        return locationService.updateLocation(locationId, updateLocationRequestDto);
    }

    @DeleteMapping("/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "locationId") Long locationId) {
        log.info("DELETE /admin/locations/{locationId} locationId = {})", locationId);
        locationService.delete(locationId);
    }
}
