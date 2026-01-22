package utils;

public class RussianLetterUtils {

    private RussianLetterUtils() {}

    public static boolean check(String input) {
        return input.length() == 1 && isRussianLetter(input.charAt(0));
    }

    private static boolean isRussianLetter(char ch) {
        return ch >= 'а' && ch <= 'я' || ch == 'ё';
    }
}
