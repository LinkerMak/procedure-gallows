import printers.ConsoleOutput;
import printers.OutputPrinter;
import session.Session;
import wordsrepository.WordsRepository;
import writers.ConsoleInput;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Session session = new Session(
                        new OutputPrinter(new ConsoleOutput()),
                        new ConsoleInput(new Scanner(System.in)),
                        new WordsRepository());
        session.create();
    }
}


