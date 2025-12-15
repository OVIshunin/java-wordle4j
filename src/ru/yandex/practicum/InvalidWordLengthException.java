package ru.yandex.practicum;

// Исключение: слово не из 5 букв
public class InvalidWordLengthException extends Exception {
    public InvalidWordLengthException() {
        super("Слово должно состоять из 5 букв.");
    }
}