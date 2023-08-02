package ru.practicum.afisha.exceptions;

public class NoSuchUser extends RuntimeException {
    public NoSuchUser(String message) {
        super(message);
    }
}