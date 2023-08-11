package ru.practicum.afisha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.afisha.enums.RequestState;

import java.time.LocalDateTime;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestDto {
    private long id;
    private long event;
    private long requester;
    private RequestState status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime created;
}
