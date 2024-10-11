package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHitDTO;
import ru.practicum.stats.dto.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(EndpointHitDTO endpointHitDTO);

    List<ViewStatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
