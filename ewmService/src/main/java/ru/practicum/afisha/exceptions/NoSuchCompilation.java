package ru.practicum.afisha.exceptions;

public class NoSuchCompilation extends RuntimeException {
    public NoSuchCompilation(String message) {
        super(message);
    }
}
