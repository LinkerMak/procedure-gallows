package printers;

public class HangmanPrinter {

    public static void print(int errorCounter) {
        System.out.println(HANGMAN_IMAGES[HANGMAN_IMAGES.length - errorCounter]);
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
