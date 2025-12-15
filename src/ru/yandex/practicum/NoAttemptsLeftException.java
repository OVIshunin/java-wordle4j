package ru.yandex.practicum;

// Исключение: закончились попытки
public class NoAttemptsLeftException extends Exception {
    public NoAttemptsLeftException() {
        super("Попытки исчерпаны. Игра окончена.");
    }
}
