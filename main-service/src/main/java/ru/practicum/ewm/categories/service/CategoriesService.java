package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;

public interface CategoriesService {
    Category addCategory(CategoryDto categoryDto);

    Category updateCategory(long id, CategoryDto categoryDto);

    void deleteCategory(Long id);
}
