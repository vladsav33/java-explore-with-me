package ru.practicum.afisha.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewCompilationDto {
    private Long[] events;
    private Boolean pinned;
    @NotBlank
    @Size(max = 50)
    private String title;
}
