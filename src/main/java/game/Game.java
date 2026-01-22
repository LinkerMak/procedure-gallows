package game;

import printers.HangmanPrinter;
import printers.OutputPrinter;
import utils.RussianLetterUtils;
import writers.Input;

import java.util.*;

public class Game {

    private final Input input;
    private final OutputPrinter printer;

    private final String word;
    private final WordMask wordMask;

    private final Set<Character> inputLetters = new HashSet<>();

    private static final int MAX_MISTAKES = 5;

    public Game(String word, Input input, OutputPrinter printer, int numberOpenLettersInWord) {
        this.input = input;
        this.printer = printer;

        this.word = word;
        wordMask = new WordMask(word, numberOpenLettersInWord);
    }

    public void start() {
        int errorCounter = MAX_MISTAKES;
        while(errorCounter != 0 && !isWordGuessed()) {
            printer.printGameState(wordMask,inputLetters);
            char letter = inputLetter();
            errorCounter = processLetter(letter, errorCounter);
        }

        printer.printGameResult(errorCounter, word);
    }

    private int processLetter(char letter, int errorCounter) {
        if(!wordContains(letter)) {
            errorCounter = handleWrongLetter(letter, errorCounter);
            return errorCounter;
        }

        if(inputLetters.contains(letter)) {
            printer.printRepeatedCorrectLetterMessage();
            return errorCounter;
        }

        handleCorrectLetter(letter);
        return errorCounter;
    }

    private boolean isWordGuessed() {
        return wordMask.allLettersGuessed();
    }

    private boolean wordContains(char letter) {
        return word.indexOf(letter) != -1;
    }

    private void handleCorrectLetter(char letter) {
        printer.printCorrectLetterMessage();
        inputLetters.add(letter);
        wordMask.updateMask(letter);
    }

    private int handleWrongLetter(char letter, int errorCounter) {
        if(!inputLetters.contains(letter)) {
            printer.printWrongLetterMessage();

            inputLetters.add(letter);

            HangmanPrinter.print(errorCounter);
            return errorCounter - 1;
        }
        else {
            printer.printRepeatedWrongLetterMessage();
            return errorCounter;
        }
    }

    private char inputLetter() {
        while(true) {
            printer.printInputLetter();
            String inputString = input.nextLine().toLowerCase();

            if(RussianLetterUtils.check(inputString)) {
                return inputString.charAt(0);
            }
            else {
                printer.printWrongInputLetter();
            }
        }
    }

}
