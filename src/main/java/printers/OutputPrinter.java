package printers;

import game.WordMask;

import java.util.Set;

public class OutputPrinter {

    private final Output output;

    public OutputPrinter(Output output) {
        this.output = output;
    }

    public void printSessionChoice(String start, String quit) {
        output.printf("[%s]овая игра или [%s]ыход?",start, quit);
        output.print("\nВаш ответ:");
    }

    public void printSessionQuit() {
        output.println("Пока :(");
    }

    public void printSessionWrongInput() {
        output.println("Введите корректный ответ");
    }

    public void printGameState(WordMask wordMask, Set<Character> inputLetters) {
        output.println("---------------------------");
        output.println("Ваше слово:" + wordMask);
        printInputLetters(inputLetters);
    }

    private void printInputLetters(Set<Character> inputLetters) {
        if(inputLetters.isEmpty()) {
            output.println("Вы пока еще не вводили буквы");
        }
        else {
            output.print("Вы использовали буквы:[");
            for (Character c : inputLetters) {
                output.print(c + " ");
            }
            output.print("]");
            output.println();
        }
    }

    public void printGameResult(int errorCounter, String word) {
        if(errorCounter == 0) {
            output.print("Вы проиграли. Ваше слово было:" + word);
        }
        else{
            output.print("Поздравляем! Вы выиграли! Ваше слово:" + word);
        }
        output.println();
    }

    public void printCorrectLetterMessage() {
        output.println("Такая буква есть");
    }

    public void printWrongLetterMessage() {
        output.println("Такой буквы нет");
    }

    public void printInputLetter() {
        output.print("Введите букву:");
    }

    public void printWrongInputLetter() {
        output.println("Некорректный ввод. Введите одну русскую букву");
    }

    public void printRepeatedCorrectLetterMessage() {
        output.println("Вы уже вводили эту правильную букву");
    }

    public void printRepeatedWrongLetterMessage() {
        output.println("Вы уже вводили эту ошибочную букву");
    }

    public void printDifficultySelection() {
        output.println("Выберите сложность. Доступны: Легкая - 1, Средняя - 2, Сложная - 3");

    }

    public void printInputDifficulty() {
        output.print("Введите число выбранной сложности:");
    }

}

