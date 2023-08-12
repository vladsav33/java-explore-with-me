package ru.practicum.afisha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.afisha.variables.Variables.DATETIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class CommentDto {
    private Long id;
    private EventDto event;
    private UserDto user;

    @NotBlank
    @Size(min = 20, max = 5000)
    private String commentText;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime updated;
}
