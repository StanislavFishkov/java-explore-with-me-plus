package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStatsDTO;
import ru.practicum.stats.projection.ViewStatsProjection;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    ViewStatsDTO toDto(ViewStatsProjection projection);
}
