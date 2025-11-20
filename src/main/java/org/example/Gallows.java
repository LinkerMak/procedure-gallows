package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Gallows {

    private static List<String> words;
    private static String word;
    private static StringBuilder wordMask;

    private static Set<Character> wrongLetters;
    private static Set<Character> inputLetters;

    private static final int MAX_ERRORS = 5;

    private static final int HARD = 2;
    private static final int MEDIUM = 1;
    private static final int EASY = 0;

    private static final String PATH = "src/main/resources/russian_words.txt";

    public static final String START = "Н";
    public static final String QUIT = "В";

    public static void startSession() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.printf("[%s]овая игра или [%s]ыход?",START, QUIT);
            System.out.print("\nВаш ответ:");

            String response  = scanner.nextLine().toUpperCase();

            if(response.equals(START)) {
                startGame(difficultSelection(scanner));
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

    private static int difficultSelection(Scanner scanner) {
        System.out.println("Выберите сложность. Доступны: Легкая - 1, Средняя - 2, Сложная - 3");
        while (true) {
            try {
                System.out.print("Введите число выбранной сложности:");
                int input = Integer.parseInt(scanner.nextLine());

                if(input < 1 || input > 3) {
                    System.out.println("Некорректный ввод режима сложности");
                    continue;
                }

                switch(input) {
                    case 1 -> {
                        return EASY;
                    }
                    case 2 -> {
                        return MEDIUM;
                    }
                    case 3-> {
                        return HARD;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод режима сложности");
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
        words = generate(PATH, difficult);
        word = takeRandomWord();
        wordMask = createMask();
        inputLetters = new HashSet<>();
        wrongLetters = new HashSet<>();
        System.out.println("Игра началась!");
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
        Scanner scanner = new Scanner(System.in);
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


    public static List<String> generate(String inputPath, int minLengthFromDifficult) {
        if(words != null) return words;

        validateFilePath(inputPath);
        Path path = Path.of(inputPath);

        try {
            List<String> words = Files.readAllLines(path);

            int minLengthWordInFile = findMinLengthWordInFile(words);
            return words
                    .stream()
                    .filter(word -> word.length() >= minLengthFromDifficult + minLengthWordInFile)
                    .toList();
        } catch(IOException e) {
            System.out.println("Ошибка чтения файла:" + e);
            return List.of();
        }
    }

    private static int findMinLengthWordInFile(List<String> words) throws IOException {
        return words
                .stream()
                .mapToInt(String::length)
                .min()
                .orElse(0);
    }

    private static List<String> generate(String inputPath) throws IOException {
        validateFilePath(inputPath);
        Path path = Path.of(inputPath);

        return Files.readAllLines(path);
    }

    private static void validateFilePath(String path) {
        if(path == null || path.trim().isEmpty()) {
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