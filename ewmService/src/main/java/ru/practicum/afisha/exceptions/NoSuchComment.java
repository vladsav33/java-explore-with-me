package ru.practicum.afisha.exceptions;

public class NoSuchComment extends RuntimeException {
    public NoSuchComment(String message) {
        super(message);
    }
}
