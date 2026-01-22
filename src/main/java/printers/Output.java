package printers;

public interface Output {

    public void print(String message);
    public void println(String message);
    public void println();
    public void printf(String format, Object... args);
}
