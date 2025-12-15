package ru.yandex.practicum;

// Исключение: слова нет в словаре
public class WordNotFoundInDictionary extends Exception {
    public WordNotFoundInDictionary(String word) {
        super("Слово '" + word + "' не найдено в словаре.");
    }
}