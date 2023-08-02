package ru.practicum.afisha.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.afisha.dto.CompilationDto;
import ru.practicum.afisha.dto.NewCompilationDto;
import ru.practicum.afisha.dto.UpdateCompilationDto;
import ru.practicum.afisha.services.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Created a compilation {}", compilationDto.getTitle());
        return service.createCompilation(compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compilationId) {
        log.info("Deleted the compilation {}", compilationId);
        service.deleteCompilation(compilationId);
    }

    @PatchMapping("/admin/compilations/{compilationId}")
    public CompilationDto updateCompilation(@PathVariable long compilationId, @Valid @RequestBody UpdateCompilationDto compilationDto) {
        return service.updateCompilation(compilationId, compilationDto);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable page = PageRequest.of(from / size, size);
        return service.getCompilations(pinned, page);
    }

    @GetMapping("/compilations/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable long compilationId) {
        return service.getCompilationById(compilationId);
    }
}
