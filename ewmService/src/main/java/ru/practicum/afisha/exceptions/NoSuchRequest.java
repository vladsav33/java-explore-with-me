package ru.practicum.afisha.exceptions;

public class NoSuchRequest extends RuntimeException {
    public NoSuchRequest(String message) {
        super(message);
    }
}
