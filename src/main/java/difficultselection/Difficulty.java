package difficultselection;

public enum Difficulty {
    EASY(3, 1),
    MEDIUM(2, 2),
    HARD(1, 3);

    private final int openedLettersCount;
    private final int menuNumber;

    Difficulty(int openedLettersCount, int menuNumber) {
        this.openedLettersCount = openedLettersCount;
        this.menuNumber = menuNumber;
    }

    public int getOpenedLettersCount() {
        return openedLettersCount;
    }

    public int getMenuNumber() {
        return menuNumber;
    }

    public static Difficulty fromNumber(int number) {
        for (Difficulty d : values()) {
            if(d.menuNumber == number) {
                return d;
            }
        }
        throw new IllegalArgumentException("Unknown difficulty: " + number);
    }
}
