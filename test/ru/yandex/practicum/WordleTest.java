package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

class WordleTest {

    private WordleGame game;
    private WordleDictionary dictionary;
    private WordleConditions conditions;
    private static final String TEST_DICT = "test_dictionary.txt";

    // Подготавливаем тестовый словарь
    @BeforeEach
    void setUp() throws IOException, LoadedDictionaryIsEmpty {
        // Создаём временный файл словаря
        String content = "яблоко\nветер\nгород\nокно\nроза\nастра";
        //String content = ""; //если проверить на пустом словаре - в лог теперь пишется ошибка
        Files.writeString(Path.of(TEST_DICT), content);

        // Инициализируем игру с тестовым словарём
        WordleLogger logger = new WordleLogger();
        WordleDictionaryLoader wordsLoader = new WordleDictionaryLoader(logger);
        WordleDictionary wDict = wordsLoader.loadDictionary(TEST_DICT);
        game = new WordleGame(wDict,logger);

    }

    // Удаляем файл после тестов
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_DICT));
    }

    //проверим загрузку словаря - фильтрацию слов по 5 букв из списка загружаемых слов
    @Test
    void testLoadDictionary() {
        dictionary = game.getDictionary();
        //количество
        assertEquals(3, dictionary.getWordsList().size()); //должны попасть только "ветер", "город", и "астра"
        //попавшие слова
        assertTrue(dictionary.getWordsList().contains("город")); //должно попасть
        assertTrue(dictionary.getWordsList().contains("ветер")); //должно попасть
        //не попавшие слова
        assertFalse(dictionary.getWordsList().contains("кот")); // вообще не было в списке
        assertFalse(dictionary.getWordsList().contains("окно")); // не 5 букв
    }

    //тест полностью угаданного слова
    @Test
    void testEvaluateGuessExactMatch() {
        game.setAnswer("проза");
        String result = game.evaluateGuess("проза");
        assertEquals("+++++", result);
    }

    //тест частичного совпадения
    @Test
    void testEvaluateGuessPartialMatch() {
        game.setAnswer("проза");
        String result = game.evaluateGuess("дробь");
        assertEquals("-++--", result);

        game.setAnswer("ролик");
        result = game.evaluateGuess("позор");
         assertEquals("-+-^^", result);
    }

    //тест полного не совпадения
    @Test
    void testEvaluateGuessNoMatches() {
        game.setAnswer("палка");
        String result = game.evaluateGuess("бобер");
        assertEquals("-----", result); // ни одной общей буквы
    }

    //попадание в коллекции условий правильных букв с правильной позицией
    @Test
    void testUpdateConditions() {
        game.setAnswer("город");
        game.evaluateGuess("город");

        conditions = game.getConditions();
        assertEquals(5, conditions.charOnRightPos.size());
        assertEquals('г', conditions.charOnRightPos.get(0));
        assertEquals('о', conditions.charOnRightPos.get(1));
        // и т.д.
        assertTrue(conditions.charIsExist.contains('г'));
        assertTrue(conditions.charIsExist.contains('о'));
        assertTrue(conditions.charIsExist.contains('р'));
        assertTrue(conditions.charIsExist.contains('д'));
    }

     //проверка на победу
    @Test
    void testWinCondition() {
        game.setAnswer("ветер");
        boolean won = game.evaluateGuess("ветер").equals("+++++");
        assertTrue(won);
    }

    @Test
    void testMakeGuessInvalidLength() {
        assertThrows(InvalidWordLengthException.class, () -> {
            game.makeGuess("кот");
        });
    }

    @Test
    void testMakeGuessUnknownWord() {
        assertThrows(WordNotFoundInDictionary.class, () -> {
            game.makeGuess("абвгд"); // слова нет в словаре
        });
    }

}