package ru.practicum.afisha.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.afisha.dto.CompilationDto;
import ru.practicum.afisha.dto.NewCompilationDto;
import ru.practicum.afisha.dto.UpdateCompilationDto;
import ru.practicum.afisha.dto.mappers.CompilationMapper;
import ru.practicum.afisha.exceptions.NoSuchCompilation;
import ru.practicum.afisha.exceptions.NoSuchEvent;
import ru.practicum.afisha.models.Compilation;
import ru.practicum.afisha.models.Event;
import ru.practicum.afisha.repositories.CompilationRepository;
import ru.practicum.afisha.repositories.EventRepository;
import ru.practicum.afisha.services.CompilationService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = mapper.toCompilation(compilationDto);
        if (compilationDto.getEvents() != null) {
            updateCompilationNew(compilation, compilationDto);
        }

        return mapper.toCompilationDto(repository.save(compilation));
    }

    public void deleteCompilation(long compilationId) {
        repository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(long compilationId, UpdateCompilationDto compilationDto) {
        Compilation compilation = repository.findById(compilationId).orElseThrow(() -> new NoSuchCompilation("No such compilation"));
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getEvents() != null) {
            updateCompilationUpdate(compilation, compilationDto);
        }
        return mapper.toCompilationDto(repository.save(compilation));
    }

    public List<CompilationDto> getCompilations(Boolean pinned, Pageable page) {
        if (pinned != null) {
            return repository.findAllByPinned(pinned, page).stream().map(mapper::toCompilationDto).collect(Collectors.toList());
        }
        return repository.findAll(page).stream().map(mapper::toCompilationDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(long compilationId) {
        return mapper.toCompilationDto(repository.findById(compilationId)
                .orElseThrow(() -> new NoSuchCompilation("No such compilation")));
    }

    private void updateCompilationNew(Compilation compilation, NewCompilationDto compilationDto) {
        Set<Event> events = new HashSet<>();
        Arrays.stream(compilationDto.getEvents())
                .forEach(value -> events.add(eventRepository.findById(value)
                        .orElseThrow(() -> new NoSuchEvent("No such event"))));
        compilation.setEvents(events);
    }

    private void updateCompilationUpdate(Compilation compilation,UpdateCompilationDto compilationDto) {
        Set<Event> events = new HashSet<>();
        Arrays.stream(compilationDto.getEvents()).forEach(value ->
                events.add(eventRepository.findById(value).orElseThrow(() -> new NoSuchEvent("No such event"))));
        compilation.setEvents(events);
    }
}
