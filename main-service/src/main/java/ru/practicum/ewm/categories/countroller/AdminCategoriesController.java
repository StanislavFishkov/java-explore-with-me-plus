package ru.practicum.ewm.categories.countroller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.service.CategoriesService;

@Validated
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@RestController
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Category addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoriesService.addCategory(categoryDto);
    }

    @PatchMapping("/{id}")
    public Category updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        return categoriesService.updateCategory(id, categoryDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
        categoriesService.deleteCategory(id);
    }
}
