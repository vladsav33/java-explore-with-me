package ru.practicum.afisha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.afisha.enums.EventState;
import ru.practicum.afisha.models.Category;
import ru.practicum.afisha.models.Location;

import java.time.LocalDateTime;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

@Data
public class EventDto {
    private long id;
    private String annotation;
    private Category category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime createdOn;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime eventDate;

    private UserDto initiator;

    private Location location;

    private boolean paid;
    private int participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private EventState state;
    private String title;
    private long views;
}
