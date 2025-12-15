package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordleDictionaryLoader {

    public WordleDictionary loadDictionary(String filename) throws IOException {
        WordleDictionary words = new WordleDictionary();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase().replace('ё','е');
                if (word.length() == 5 && word.matches("[а-я]+")) {
                    words.addWord(word);
                }
            }
        }
        return words;
    }





}
