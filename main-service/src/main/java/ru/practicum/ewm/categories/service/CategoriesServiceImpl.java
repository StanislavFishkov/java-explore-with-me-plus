package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.mapper.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoriesRepository;
import ru.practicum.ewm.core.error.exception.NotFoundException;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoriesRepository.save(categoryMapper.toEntity(newCategoryDto));
        log.info("Category is created: {}", category);
        return categoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(long id, NewCategoryDto updateCategoryDto) {
        log.info("start updateCategory");
        Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
        category = categoriesRepository.save(categoryMapper.update(category, updateCategoryDto));
        log.info("Category is updated: {}", category);
        return categoryMapper.toDto(categoriesRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
        log.info("Category deleted with id: {}", id);
    }
}