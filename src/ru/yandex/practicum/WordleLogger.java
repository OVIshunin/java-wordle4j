package ru.yandex.practicum;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class WordleLogger {
    private final PrintWriter writer;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WordleLogger() throws IOException {
        this.writer = new PrintWriter(new FileWriter("wordle_log.txt", true)); // дописываем в файл
    }

    // Запись события с временной меткой
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        writer.println("[" + timestamp + "] " + message);
        writer.flush(); // сразу записываем
    }

    // Логирование попытки игрока
    public void logGuess(String guess, String result) {
        log("Попытка: " + guess + " → Результат: " + result);
    }

    // Логирование подсказки
    public void logHint(String hint) {
        if (hint != null) {
            log("Подсказка: " + hint);
        } else {
            log("Подсказка: не найдена");
        }
    }

    // Логирование победы
    public void logWin(String secretWord, int attempts) {
        log("Победа! Загаданное слово: " + secretWord + " (попыток: " + attempts + ")");
    }

    // Логирование поражения
    public void logLoss(String secretWord) {
        log("Поражение. Загаданное слово: " + secretWord);
    }

    // Закрытие логгера
    public void close() {
        writer.close();
    }
}