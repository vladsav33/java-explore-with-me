package ru.practicum.afisha.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 50)
    private String title;
}
