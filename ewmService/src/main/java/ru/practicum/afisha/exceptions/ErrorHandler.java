package ru.practicum.afisha.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.afisha.controllers.CategoryController;
import ru.practicum.afisha.controllers.CompilationController;
import ru.practicum.afisha.controllers.EventController;
import ru.practicum.afisha.controllers.RequestController;
import ru.practicum.afisha.controllers.UserController;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class, CategoryController.class, RequestController.class,
                                         EventController.class, CompilationController.class})
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(NoSuchUser.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoUser(final NoSuchUser exception) {
        log.warn(exception.getMessage());
        return Map.of("Object not found", "No such user");
    }

    @ExceptionHandler(NoSuchCategory.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoCategory(final NoSuchCategory exception) {
        log.warn(exception.getMessage());
        return Map.of("Object not found", "No such category");
    }

    @ExceptionHandler(NoSuchEvent.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoEvent(final NoSuchEvent exception) {
        log.warn(exception.getMessage());
        return Map.of("Object not found", "No such event");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ConstraintViolationException exception) {
        log.warn(exception.getMessage());
        return Map.of("Data validation error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleRejection(final ConflictException exception) {
        log.warn(exception.getMessage());
        return Map.of("Rejection error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final WrongDataException exception) {
        log.warn(exception.getMessage());
        return Map.of("Data validation error", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNoCompilation(final NoSuchCompilation exception) {
        log.warn(exception.getMessage());
        return Map.of("Data validation error", exception.getMessage());
    }
}
