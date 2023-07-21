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
    LocalDateTime start;
    LocalDateTime end;
    String[] uris;
    boolean unique;
}
