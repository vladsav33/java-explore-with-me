package ru.practicum.afisha.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.CategoryDto;
import ru.practicum.afisha.dto.mappers.CategoryMapper;
import ru.practicum.afisha.exceptions.ConflictException;
import ru.practicum.afisha.exceptions.NoSuchCategory;
import ru.practicum.afisha.repositories.CategoryRepository;
import ru.practicum.afisha.repositories.EventRepository;
import ru.practicum.afisha.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (repository.existsCategoryByName(categoryDto.getName())) {
            throw new ConflictException("Can't create duplicate category name");
        }
        return mapper.toCategoryDto(repository.save(mapper.toCategory(categoryDto)));
    }

    public void deleteCategory(long categoryId) {
        repository.findById(categoryId).orElseThrow(() -> new NoSuchCategory("Category was not found"));
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("Category can't be deleted, it is referenced by an event");
        }
        repository.deleteById(categoryId);
    }

    public CategoryDto updateCategory(long categoryId, CategoryDto categoryDto) {
        if (repository.existsCategoryByName(categoryDto.getName())
                && repository.findById(categoryId).isPresent()
                && !repository.findById(categoryId).get().getName().equals(categoryDto.getName())) {
            throw new ConflictException("Can't create duplicate category name");
        }
        return mapper.toCategoryDto(repository.save(mapper.toCategory(categoryDto)));
    }

    public List<CategoryDto> getCategories(Pageable page) {
        return repository.findAll(page).stream().map(mapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoriesById(long categoryId) {
        return mapper.toCategoryDto(repository.findById(categoryId).orElseThrow(() -> new NoSuchCategory("Category was not found")));
    }
}
