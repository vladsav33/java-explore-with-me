package ru.practicum.afisha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetStatDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private String[] uris;
    private boolean unique;
}
