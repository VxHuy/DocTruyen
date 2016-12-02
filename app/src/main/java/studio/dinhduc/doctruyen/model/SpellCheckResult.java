package studio.dinhduc.doctruyen.model;

/**
 * Created by dinhduc on 02/12/2016.
 */

public class SpellCheckResult {
    private String mChapterName;
    private String mWord;
    private String mLine;

    public String getChapterName() {
        return mChapterName;
    }

    public void setChapterName(String chapterName) {
        mChapterName = chapterName;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getLine() {
        return mLine;
    }

    public void setLine(String line) {
        mLine = line;
    }
}
