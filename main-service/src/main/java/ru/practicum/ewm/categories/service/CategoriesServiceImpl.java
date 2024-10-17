package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.mapper.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.core.error.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoriesRepository.save(category);
    }

    @Override
    public Category updateCategory(long id, CategoryDto categoryDto) {
        log.info("start updateCategory");
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        if (!categoriesRepository.existsById(id)) {
            throw new NotFoundException("Category with id " + id + " not found");
        }
        return categoriesRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
    }
}
