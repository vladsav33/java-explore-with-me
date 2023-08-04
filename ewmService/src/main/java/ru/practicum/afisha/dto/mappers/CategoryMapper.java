package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import ru.practicum.afisha.dto.CategoryDto;
import ru.practicum.afisha.models.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);
}
