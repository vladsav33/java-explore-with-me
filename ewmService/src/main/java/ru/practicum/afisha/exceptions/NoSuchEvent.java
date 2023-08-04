package ru.practicum.afisha.exceptions;

public class NoSuchEvent extends RuntimeException {
    public NoSuchEvent(String message) {
        super(message);
    }
}
