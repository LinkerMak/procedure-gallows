package session;

import difficultselection.Difficulty;
import difficultselection.DifficultySelector;
import game.Game;
import printers.OutputPrinter;
import wordsrepository.WordsRepository;
import writers.Input;

public class Session {

    private final Input input;
    private final OutputPrinter printer;
    private final WordsRepository wordsRepository;

    private static final String START = "Н";
    private static final String QUIT = "В";

    public Session(OutputPrinter outputPrinter, Input input, WordsRepository wordsRepository) {
        this.printer = outputPrinter;
        this.input = input;
        this.wordsRepository = wordsRepository;
    }

    public void create() {
        while(true) {
            printer.printSessionChoice(START, QUIT);
            String response = input.nextLine().toUpperCase();

            if(response.equals(START)) {
                startGame();
            }
            else if(response.equals(QUIT)){
                printer.printSessionQuit();
                break;
            }
            else {
                printer.printSessionWrongInput();
            }
        }
    }

    private void startGame() {
        String word = wordsRepository.takeRandomWord();

        DifficultySelector difficultySelector = new DifficultySelector(printer, input);
        Difficulty difficulty = difficultySelector.chooseDifficulty();

        int numberOpenLettersInWord = difficulty.getOpenedLettersCount();

        Game game = new Game(word, input, printer, numberOpenLettersInWord);
        game.start();
    }

}
