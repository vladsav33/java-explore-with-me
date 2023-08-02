package ru.practicum.afisha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompilationDto {
    private long id;
    private Set<EventDto> events;
    private Boolean pinned;
    private String title;
}
