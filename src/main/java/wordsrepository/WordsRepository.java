package wordsrepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class WordsRepository {

    private List<String> words;

    private final String PATH = "src/main/resources/russian_words.txt";

    private void generateWords()  {
        validateFilePath();
        Path path = Path.of(PATH);

        try {
            words = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать файл", e);
        }
    }

    public String takeRandomWord() {
        if(words == null) {
            generateWords();
        }

        int index = new Random().nextInt(words.size());
        return words.get(index);
    }

    private void validateFilePath() {
        if(PATH == null || PATH.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь файла не может быть пустым или равным null");
        }
    }
}
