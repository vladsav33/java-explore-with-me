package ru.practicum.afisha.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.afisha.dto.CompilationDto;
import ru.practicum.afisha.dto.NewCompilationDto;
import ru.practicum.afisha.models.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);


}
