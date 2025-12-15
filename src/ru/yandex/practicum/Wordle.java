package ru.yandex.practicum;

import java.io.IOException;


public class Wordle {

    public static void main(String[] args) {
        try {
            WordleDictionaryLoader wordsLoader = new WordleDictionaryLoader();
            WordleDictionary wDict = wordsLoader.loadDictionary("words_ru.txt");
            WordleGame game = new WordleGame(wDict);

            game.start();

        } catch (IOException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
        }

    }

}
