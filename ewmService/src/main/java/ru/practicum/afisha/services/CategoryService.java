package ru.practicum.afisha.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.afisha.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(long categoryId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(Pageable page);

    CategoryDto getCategoriesById(long categoryId);
}
