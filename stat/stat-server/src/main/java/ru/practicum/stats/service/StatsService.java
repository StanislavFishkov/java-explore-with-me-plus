package ru.practicum.stats.service;

import ru.practicum.EndpointHitDTO;
import ru.practicum.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(EndpointHitDTO endpointHitDTO);

    List<ViewStatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
