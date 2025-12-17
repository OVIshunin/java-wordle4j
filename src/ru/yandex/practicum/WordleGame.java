package ru.yandex.practicum;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WordleGame {

    private String answer;
    private final WordleDictionary dictionary;
    private final WordleConditions conditions;
    private final Set<String> guessedWords;   // Уже введённые слова
    private final WordleLogger logger;
    private int attempt;

    // Маска совпадений
    private static final char C_CORRECT_CHAR = '+';
    private static final char C_PRESENT_CHAR = '^';
    private static final char C_ABSENT_CHAR = '-';
    private static final int C_GUESSES_COUNT = 6;

    public WordleGame(WordleDictionary dictionary) throws IOException {
        this.dictionary = dictionary;
        this.answer = dictionary.pickRandomWord();
        conditions = new WordleConditions();
        this.guessedWords = new HashSet<>();
        this.logger = new WordleLogger();
        logger.log("Игра начата. Загаданное слово: " + answer);
        attempt = 0;
    }


    public String makeGuess(String guess) throws InvalidWordLengthException, WordNotFoundInDictionary, NoAttemptsLeftException {

        guess = guess.trim().toLowerCase();

        // Проверка длины
        if (guess.length() != 5) {
            throw new InvalidWordLengthException();
        }

        // Проверка наличия в словаре
        if (!dictionary.getWordsList().contains(guess)) {
            throw new WordNotFoundInDictionary(guess);
        }

        guessedWords.add(guess);

        String result = evaluateGuess(guess);
        logger.logGuess(guess, result);

        if (result.equals("+++++")) {
            logger.logWin(answer, attempt); // логируем победу
            return result;
        }

        // Проверка оставшихся попыток
        if (guessedWords.size() >= C_GUESSES_COUNT) {
            logger.logLoss(answer); // логируем поражение
            throw new NoAttemptsLeftException();
        }

        return result;
    }

    //найти подсказку
    public String findHint() {
        for (String word : dictionary.getWordsList()) {
            if (isValid(word)) {
                logger.logHint(word);
                return word;
            }
        }
        logger.logHint(null);
        return null; // Нет подходящих слов
    }

    //проверить подходящее ли слово на основании условий
    private boolean isValid(String word) {
        //проверяем уже использованные слова
        if (guessedWords.contains(word)) return false;

        char[] chars = word.toCharArray();

        // Проверяем отсутствующие буквы
        for (char ch : conditions.charIsNotExist) {
            if (word.indexOf(ch) != -1) return false;
        }

        // Проверяем присутствующие буквы
        for (char ch : conditions.charIsExist) {
            if (word.indexOf(ch) == -1) return false;
        }

        // Точные позиции
        for (Map.Entry<Integer, Character> entry : conditions.charOnRightPos.entrySet()) {
            int pos = entry.getKey();
            char ch = entry.getValue();
            if (chars[pos] != ch) return false;
        }

        // Неправильные позиции: буква есть, но не на этой позиции
        for (Map.Entry<Integer, Set<Character>> entry : conditions.charOnWrongPos.entrySet()) {
            int pos = entry.getKey();
            Set<Character> charsAtPos = entry.getValue();
            char actualChar = chars[pos];
            // Если на этой позиции стоит буква из charOnWrongPos — неверно
            if (charsAtPos.contains(actualChar)) return false;
            // При этом буква должна быть где‑то ещё
            for (char ch : charsAtPos) {
                if (word.indexOf(ch) == -1 || word.indexOf(ch) == pos) {
                    return false;
                }
            }
        }

        return true;
    }

    public String evaluateGuess(String guess) {
        StringBuilder result = new StringBuilder();
        char[] secretArray = answer.toCharArray();
        char[] guessArray = guess.toCharArray();

        // Совпадение буквы и позиции
        for (int i = 0; i < 5; i++) {
            if (guessArray[i] == secretArray[i]) {
                result.append(C_CORRECT_CHAR);
                conditions.charOnRightPos.put(i, guessArray[i]);
                conditions.charIsExist.add(guessArray[i]);
            } else {
                result.append('?');
            }
        }

        // наличие буквы не в той позиции, или отсутствие
        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) == '?') {
                char ch = guessArray[i];
                if (answer.indexOf(ch) != -1) {
                    result.setCharAt(i, C_PRESENT_CHAR);
                    conditions.charIsExist.add(ch);
                    conditions.addWrongPos(i, ch);
                } else {
                    result.setCharAt(i, C_ABSENT_CHAR);
                    conditions.charIsNotExist.add(ch);
                }
            }
        }
        return result.toString();
    }

    //Добавим методы с доступом к внутренним объектам для тестирования
    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public WordleConditions getConditions() {
        return conditions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addAttempt() {
        attempt++;
    }

    public void subAttempt() {
        attempt--;
    }

    public int getAttempt() {
        return attempt;
    }

    public int getAttemptsCount() {
        return C_GUESSES_COUNT;
    }

    public void setLog(String message) {
        logger.log(message);
    }
}
