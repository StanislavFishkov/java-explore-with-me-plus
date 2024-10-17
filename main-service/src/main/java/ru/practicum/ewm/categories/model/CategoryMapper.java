package ru.practicum.ewm.categories.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);

}
