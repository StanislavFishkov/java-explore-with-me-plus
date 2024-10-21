package ru.practicum.ewm.categories.controller;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.service.CategoriesService;

import java.util.List;

@Validated
@RequestMapping("/categories")
@RequiredArgsConstructor
@RestController
public class PublicCategoriesController {
    private final CategoriesService categoriesService;


    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam Long catId, @RequestParam Long limit) {

        return categoriesService.findBy(catId, limit);
    }

    //Получение категории по id
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return categoriesService.getCategoryBy(catId);
    }
}
