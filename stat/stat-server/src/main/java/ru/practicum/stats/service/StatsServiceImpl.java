package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDTO;
import ru.practicum.ViewStatsDTO;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.mapper.ViewStatsMapper;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public void saveHit(EndpointHitDTO endpointHitDTO) {
        Stats stats = endpointHitMapper.toEntity(endpointHitDTO);
        statsRepository.save(stats);
    }

    @Override
    public List<ViewStatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Stats> statsList;
        if (uris != null && !uris.isEmpty()) {
            statsList = statsRepository.findByTimestampBetweenAndUriIn(start, end, uris);
        } else {
            statsList = statsRepository.findByTimestampBetween(start, end);
        }

        return statsList.stream()
                .collect(Collectors.groupingBy(Stats::getUri))
                .entrySet().stream()
                .map(entry -> viewStatsMapper.toDto(
                        entry.getValue().get(0).getApp(),
                        entry.getKey(),
                        entry.getValue().size()))
                .collect(Collectors.toList());
    }
}
