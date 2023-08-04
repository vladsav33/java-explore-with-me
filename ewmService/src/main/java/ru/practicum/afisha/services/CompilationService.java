package ru.practicum.afisha.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.afisha.dto.CompilationDto;
import ru.practicum.afisha.dto.NewCompilationDto;
import ru.practicum.afisha.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compilationId);

    CompilationDto updateCompilation(long compilationId, UpdateCompilationDto compilationDto);

    List<CompilationDto> getCompilations(Boolean pinned, Pageable page);

    CompilationDto getCompilationById(long compilationId);
}
