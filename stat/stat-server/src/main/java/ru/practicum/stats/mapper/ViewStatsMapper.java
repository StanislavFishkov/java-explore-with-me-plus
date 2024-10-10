package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ViewStatsDTO;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    // Конвертация сущности (Entity) в DTO для просмотра статистики
    @Mapping(target = "hits", source = "hits")
    // Используем Integer для hits
    ViewStatsDTO toDto(String app, String uri, Integer hits);
}
