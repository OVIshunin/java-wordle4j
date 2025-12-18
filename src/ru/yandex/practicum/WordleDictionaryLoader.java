package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WordleDictionaryLoader {
    private final WordleLogger logger;

    public WordleDictionaryLoader(WordleLogger logger) {
        this.logger = logger;
    }

    public WordleDictionary loadDictionary(String filename) throws IOException, LoadedDictionaryIsEmpty {
        WordleDictionary words = new WordleDictionary();
        //явно указал кодировку для чтения файла
        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase().replace('ё','е');
                if (word.length() == 5 && word.matches("[а-я]+")) {
                    words.addWord(word);
                }
            }
        }
        if (words.getWordsList().size() == 0) {
            logger.log("Ошибка: Загруженный справочник пуст");
            throw new LoadedDictionaryIsEmpty();
        } else {
            logger.log("Справочник успешно загружен, количество слов: " + words.getWordsList().size());
            return words;
        }
    }





}
