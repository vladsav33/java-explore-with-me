package ru.practicum.afisha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.afisha.models.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewEventDto {
    private long id;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    private long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @Size(min = 3, max = 120)
    private String title;
}
