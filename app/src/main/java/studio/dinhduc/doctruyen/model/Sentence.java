package studio.dinhduc.doctruyen.model;

import java.util.ArrayList;


public class Sentence implements Comparable<Sentence> {
    private int lv;  //số từ có trong từ cần tìm có trong câu.
    private String content;
    private int position;
    private ArrayList<SameWord> hasWord = new ArrayList<>();
    private int expected;
    private String subResult;
    private int startPosResult;
    private int maxLength;


    public Sentence(int lv, String content, int position) {
        this.lv = lv;
        this.content = content;
        this.position = position;
    }

    public Sentence(int lv, String content, int position, ArrayList<SameWord> hasWord) {
        this.lv = lv;
        this.content = content;
        this.position = position;
        this.hasWord = hasWord;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getSubResult() {
        return subResult;
    }

    public void setSubResult(String subResult) {
        this.subResult = subResult;
    }

    public int getStartPosResult() {
        return startPosResult;
    }

    public void setStartPosResult(int startPosResult) {
        this.startPosResult = startPosResult;
    }

    public ArrayList<SameWord> getHasWord() {
        return hasWord;
    }

    public void setHasWord(ArrayList<SameWord> hasWord) {
        this.hasWord = hasWord;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    @Override
    public int compareTo(Sentence comparesen) {
        int comparelv=((Sentence)comparesen).getExpected();
        return comparelv-this.getExpected();
    }
}
