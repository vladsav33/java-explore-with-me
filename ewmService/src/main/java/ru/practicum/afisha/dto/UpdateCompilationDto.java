package ru.practicum.afisha.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateCompilationDto {
    private Long[] events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}
