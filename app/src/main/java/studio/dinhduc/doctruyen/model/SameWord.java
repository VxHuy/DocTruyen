package studio.dinhduc.doctruyen.model;

import java.util.ArrayList;

public class SameWord{
    ArrayList<Integer> posSameWord = new ArrayList<>();
    Word word;

    public SameWord(ArrayList<Integer> posSameWord, Word word) {
        this.posSameWord = posSameWord;
        this.word = word;
    }

    public SameWord(ArrayList<Integer> posSameWord) {
        this.posSameWord = posSameWord;
    }

    public SameWord() {

    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public ArrayList<Integer> getPosSameWord() {
        return posSameWord;
    }

    public void setPosSameWord(ArrayList<Integer> posSameWord) {
        this.posSameWord = posSameWord;
    }
}
