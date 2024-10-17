package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.model.CategoryDto;
import ru.practicum.ewm.categories.model.CategoryMapper;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.error.NotFoundException;

import java.io.NotActiveException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesServiceImpl {
    public final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    public Category addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoriesRepository.save(category);
    }
    public Category updateCategory(long id,CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        if (categoriesRepository.existsById(id)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Category not found", "category not found", );
        }
        return categoriesRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
    }

}
