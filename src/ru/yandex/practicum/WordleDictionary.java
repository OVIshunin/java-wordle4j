package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(){
        words = new ArrayList<>();
    }

    public String pickRandomWord() {
        List<String> wordList = new ArrayList<>(words);
        return wordList.get(new Random().nextInt(wordList.size()));
    }

    public void addWord(String word){
        words.add(word);
    }

    public List<String> getWordsList(){
        return words;
    }


}
