package ru.yandex.practicum;

import java.io.IOException;
import java.util.Scanner;


public class Wordle {

    public static void main(String[] args) {

        WordleGame game = null; //объявляем до блока try, чтобы потом можно было использовать в блоке catch
        try {
            WordleDictionaryLoader wordsLoader = new WordleDictionaryLoader();
            WordleDictionary wDict = wordsLoader.loadDictionary("words_ru.txt");

            game = new WordleGame(wDict);
            //все System.in и System.out, сканнер - перенесены в метод класса Wordle,
            // класс WordleGame только предоставляет свои методы
            startGame(game);

        } catch (IOException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
            if (game != null) {
                game.setLog("Ошибка загрузки словаря: " + e.getMessage());
            }
            //добавлена обработка всех иных исключений с выводом в консоль и записью в лог
        } catch  (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            if (game != null) {
                game.setLog("Ошибка: " + e.getMessage());
            }
        }
    }


    public static void startGame(WordleGame game) {

        Scanner scanner = new Scanner(System.in);

        //внешний блок - на исключение по количеству ошибок
        try {
            while (true) {

                game.addAttempt();
                System.out.print("Попытка " + game.getAttempt() + "/" + game.getAttemptsCount() + ". Введите слово: ");
                String guess = scanner.nextLine().trim().toLowerCase();

                //Запрос подсказки (если вместо слова - вводится просто ентер - ввод нулевой длины
                if (guess.length() == 0) {
                    String hint = game.findHint();
                    if (hint != null) {
                        System.out.println("Подсказка: " + hint);
                        guess = hint;
                    } else {
                        System.out.println("Подсказка не найдена.");
                    }
                }

                //попытка с обработкой ошибок в случае некорректного ввода
                try {
                    String result = game.makeGuess(guess);
                    System.out.println("Результат: " + result);

                    if (result.equals("+++++")) {
                        System.out.println("Победа! Загаданное слово: " + game.getAnswer());
                        return;
                    }
                /*если слово не в 5 символов или такого не существует(нет в словаре) -
                ошибка, и не списываем попытку
                 */
                } catch (InvalidWordLengthException | WordNotFoundInDictionary e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    game.setLog("Ошибка ввода: " + e.getMessage());
                    game.subAttempt(); // не засчитывать попытку
                } catch (NoAttemptsLeftException e) {
                    //превышение попыток - выкидываем исключение выше и завершаем игру
                    throw new NoAttemptsLeftException();
                }

            }

        } catch (NoAttemptsLeftException e) {
            System.out.println("Игра окончена. " + e.getMessage());
            System.out.println("Загаданное слово: " + game.getAnswer());
        }

    }

}
