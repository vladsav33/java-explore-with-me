package ru.practicum.afisha.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.afisha.controllers.StatController;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = StatController.class)
public class ErrorHandler {
    @ExceptionHandler(InvalidParameter.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNoUser(final InvalidParameter exception) {
        log.warn(exception.getMessage());
        return Map.of("error", "Invalid parameters");
    }
}
