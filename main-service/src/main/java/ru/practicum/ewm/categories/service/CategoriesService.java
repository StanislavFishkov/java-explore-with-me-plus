package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(long id, NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    CategoryDto getCategoryBy(Long id);

    List<CategoryDto> findBy(Long id, Long limit);
}
