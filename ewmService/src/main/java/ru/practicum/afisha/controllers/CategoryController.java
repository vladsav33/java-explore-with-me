package ru.practicum.afisha.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.CategoryDto;
import ru.practicum.afisha.services.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Data
@Slf4j
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Create category {}", categoryDto.getName());
        return service.createCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long categoryId) {
        log.info("Delete category {}", categoryId);
        service.deleteCategory(categoryId);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public CategoryDto updateCategory(@PathVariable long categoryId, @Valid @RequestBody CategoryDto categoryDto) {
        return service.updateCategory(categoryId, categoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam (defaultValue = "0") int from,
                                           @RequestParam (defaultValue = "10") int size) {
        Pageable page = PageRequest.of(from / size, size);
        return service.getCategories(page);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategoriesById(@PathVariable long categoryId) {
        return service.getCategoriesById(categoryId);
    }
}
