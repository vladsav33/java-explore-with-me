package ru.practicum.afisha.exceptions;

public class NoSuchCategory extends RuntimeException {
    public NoSuchCategory(String message) {
        super(message);
    }
}
