package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.EndpointHitDTO;
import ru.practicum.stats.model.Stats;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    // Конвертация DTO в сущность (Entity)
    @Mapping(target = "id", ignore = true)
    // Игнорируем ID, так как он генерируется базой данных
    Stats toEntity(EndpointHitDTO dto);

    // Конвертация сущности (Entity) в DTO
    EndpointHitDTO toDto(Stats entity);
}
