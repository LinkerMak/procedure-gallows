package game;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WordMask {

    private StringBuilder mask;
    private String word;

    public WordMask(String word, int n) {
        this.word = word;
        mask = createMask(word);
        openNLettersInMask(n);
    }

    private StringBuilder createMask(String word) {
        StringBuilder wordMask = new StringBuilder(word.length());
        for(int i = 0; i < word.length(); i++) {
            wordMask.append("*");
        }
        return wordMask;
    }

    private void openNLettersInMask(int n) {
        Set<Integer> usesIndexes = new HashSet<>();
        for(int i = 0; i < n;i++) {
            int index;
            do {
                index = new Random().nextInt(word.length());
            } while (usesIndexes.contains(index));
            usesIndexes.add(index);
            updateMask(word.charAt(index));
        }
    }

    public void updateMask(char letter) {
        for(int i = 0; i < mask.length(); i++) {
            if(word.charAt(i) == letter) {
                mask.setCharAt(i, letter);
            }
        }
    }

    public boolean allLettersGuessed() {
        for(int i = 0; i < mask.length();i++) {
            if(mask.charAt(i) == '*') return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return mask.toString();
    }
}
