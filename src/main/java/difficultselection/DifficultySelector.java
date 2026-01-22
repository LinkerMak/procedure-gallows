package difficultselection;

import printers.OutputPrinter;
import writers.Input;

public class DifficultySelector {

    private final OutputPrinter printer;
    private final Input input;

    public DifficultySelector(OutputPrinter printer, Input input) {
        this.printer = printer;
        this.input = input;
    }

    public Difficulty chooseDifficulty() {
        printer.printDifficultySelection();
        while (true) {
            printer.printInputDifficulty();

            try {
                String line = input.nextLine();
                int input = Integer.parseInt(line);
                return Difficulty.fromNumber(input);
            }catch (IllegalArgumentException e) {
                printer.printSessionWrongInput();
            }
        }
    }
}
