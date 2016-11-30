package studio.dinhduc.doctruyen.model;

public class Word{
    private int length;
    private int posInSentence;
    private int posInQuery;
    private String content;


    public Word(int length, int posInSentence, int posInQuery, String content) {
        this.length = length;
        this.posInSentence = posInSentence;
        this.posInQuery = posInQuery;
        this.content = content;
    }

    public Word( Word word) {
        this.length = word.getLength();
        this.posInSentence = word.getPosInSentence();
        this.posInQuery = word.getPosInQuery();
        this.content = word.getContent();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPosInSentence() {
        return posInSentence;
    }

    public void setPosInSentence(int posInSentence) {
        this.posInSentence = posInSentence;
    }

    public int getPosInQuery() {
        return posInQuery;
    }

    public void setPosInQuery(int posInQuery) {
        this.posInQuery = posInQuery;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
