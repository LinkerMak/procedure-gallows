package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Gallows {

    private static final Scanner scanner = new Scanner(System.in);

    private static List<String> words;
    private static String word;
    private static StringBuilder wordMask;

    private static Set<Character> wrongLetters;
    private static Set<Character> inputLetters;
    private static final int MAX_ERRORS = 5;

    private static final int HARD = 3;
    private static final int EASY = 1;

    private static final String PATH = "src/main/resources/russian_words.txt";

    public static void startSession() {
        final String START = "Н";
        final String QUIT = "В";

        while(true) {
            System.out.printf("[%s]овая игра или [%s]ыход?",START, QUIT);
            System.out.print("\nВаш ответ:");

            String response  = scanner.nextLine().toUpperCase();

            if(response.equals(START)) {
                startGame(difficultSelection());
            }
            else if(response.equals(QUIT)){
                System.out.println("Пока :(");
                break;
            }
            else {
                System.out.println("Введите корректный ответ");
            }
        }
    }

    private static int difficultSelection() {
        System.out.println("Выберите сложность. Доступны: Легкая - 1, Средняя - 2, Сложная - 3");
        while (true) {
            System.out.print("Введите число выбранной сложности:");

            int input = scanner.nextInt();
            scanner.nextLine();

            if(input >= EASY && input <= HARD) {
                return input;
            }
        }
    }

    private static void startGame(int difficult) {
        initialize(difficult);
        int errorCounter = MAX_ERRORS;
        while(errorCounter != 0) {
            printGameState();
            char letter = inputLetter();

            if(!wordContains(letter)) {
                errorCounter = handleWrongLetter(letter, errorCounter);
                continue;
            }

            if(inputLetters.contains(letter)) {
                System.out.println("Вы уже вводили эту правильную букву");
                continue;
            }
            handleCorrectLetter(letter);

            if(isWordGuessed()) break;
        }

        printGameResult(errorCounter);
    }

    private static void initialize(int difficult) {
        words = generate();
        word = takeRandomWord();
        wordMask = createMask();

        openNLettersInMask(HARD - difficult);

        inputLetters = new HashSet<>();
        wrongLetters = new HashSet<>();

        System.out.println("Игра началась!");
    }

    private static void openNLettersInMask(int n) {
        Set<Integer> usesIndexes = new HashSet<>();
        for(int i = 0; i < n;i++) {
            int index;
            do {
                index = new Random().nextInt(word.length());
            } while (usesIndexes.contains(index));
            usesIndexes.add(index);
            updateMask(word.charAt(index));
        }
    }

    private static void printGameState() {
        System.out.println("---------------------------");
        System.out.println("Ваше слово:" + wordMask);
        printInputLetters();
    }

    private static void handleCorrectLetter(char letter) {
        System.out.println("Такая буква есть");
        inputLetters.add(letter);
        updateMask(letter);
    }

    private static void updateMask(char letter) {
        for(int i = 0; i < wordMask.length(); i++) {
            if(word.charAt(i) == letter) {
                wordMask.setCharAt(i, letter);
            }
        }
    }


    private static int handleWrongLetter(char letter, int errorCounter) {
        if(!wrongLetters.contains(letter)) {
            System.out.println("Такой буквы нет");
            wrongLetters.add(letter);
            inputLetters.add(letter);
            printHangman(errorCounter);
            return --errorCounter;
        }
        else {
            System.out.println("Вы уже вводили эту ошибочную букву");
            return errorCounter;
        }
    }

    private static void printGameResult(int errorCounter) {
        if(errorCounter == 0) {
            System.out.print("Вы проиграли. Ваше слово было:" + word);
        }
        else{
            System.out.print("Поздравляем! Вы выиграли! Ваше слово:" + word);
        }
        System.out.println();
    }

    private static StringBuilder createMask() {
        StringBuilder wordMask = new StringBuilder(word.length());
        for(int i = 0; i < word.length(); i++) {
            wordMask.append("*");
        }
        return wordMask;
    }

    private static boolean isWordGuessed() {
        for(int i = 0; i < wordMask.length();i++) {
            if(wordMask.charAt(i) == '*') return false;
        }
        return true;
    }

    private static void printInputLetters() {
        if(inputLetters.isEmpty()) {
            System.out.println("Вы пока еще не вводили буквы");
        }
        else {
            System.out.print("Вы использовали буквы:[");
            for (Character c : inputLetters) {
                System.out.print(c + " ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    private static boolean wordContains(char letter) {
        for(int i = 0; i < word.length(); i++) {
            if(letter == word.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    private static char inputLetter() {
        while(true) {
            System.out.print("Введите букву:");
            String input = scanner.nextLine().toLowerCase();
            if(check(input)) {
                return input.charAt(0);
            }
            else {
                System.out.println("Некорректный ввод. Попробуйте еще раз");
            }
        }
    }

    private static boolean check(String input) {
       return input.length() == 1 && isRussianLetter(input.charAt(0));
    }


    private static boolean isRussianLetter(char ch) {
        return ch >= 'а' && ch <= 'я' || ch == 'ё';
    }

    private static String takeRandomWord() {
        int index = new Random().nextInt(words.size());
        return words.get(index);
    }

    private static List<String> generate()  {
        if(words != null) return words;

        validateFilePath();
        Path path = Path.of(Gallows.PATH);

        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл", e);
        }
    }

    private static void validateFilePath() {
        if(Gallows.PATH == null || Gallows.PATH.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь файла не может быть пустым или равным null");
        }
    }

    private static int calculateIndexImages(int errorCounter) {
        // какая-то жесткая логика размазывания ошибок по изображениям, если они не равны
        // чтобы мы могли изменить только константу MAX_ERRORS и ничего не нужно было бы переписывать
        // но я тут не алгосы дрочу
        return HANGMAN_IMAGES.length - errorCounter;
    }

    private static void printHangman(int errorCounter) {
        System.out.println(HANGMAN_IMAGES[calculateIndexImages(errorCounter)]);
    }

    private static final String[] HANGMAN_IMAGES = {
            """
         ___________
         ║         ║
         ║         O
         ║
         ║
         ║
         ║
         ║
        """,
            """
         ___________
         ║         ║
         ║         O
         ║         │
         ║
         ║
         ║
         ║
        """,
            """
         ___________
         ║         ║
         ║         O
         ║        /│\\
         ║
         ║
         ║
         ║
        """,
            """
         ___________
         ║         ║
         ║         O
         ║        /│\\
         ║        /
         ║
         ║
         ║
        """,
            """
         ___________
         ║         ║
         ║         O
         ║        /│\\
         ║        / \\
         ║
         ║
         ║
        """
    };

}