package ru.yandex.practicum;

// Исключение: загруженный из файла словарь - пуст
public class LoadedDictionaryIsEmpty extends Exception {
    public LoadedDictionaryIsEmpty() {
        super("Загруженный словарь пуст");
    }
}
