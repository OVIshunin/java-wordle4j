package ru.yandex.practicum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordleConditions {
    Set<Character> charIsExist = new HashSet<>();                   // Буквы, которые есть в слове
    Set<Character> charIsNotExist = new HashSet<>();                // Букв нет в слове
    Map<Integer, Character> charOnRightPos = new HashMap<>();       // Позиция буквы, когда она на своем месте
    Map<Integer, Set<Character>> charOnWrongPos = new HashMap<>();  // Позиция букв, которые есть, но не на своем месте

    public void addWrongPos(int pos, char ch) {
        charOnWrongPos.computeIfAbsent(pos, k -> new HashSet<>()).add(ch);
    }

}
