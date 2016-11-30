package studio.dinhduc.doctruyen.model;

/**
 * Created by dinhduc on 14/11/2016.
 */

public class SearchResult implements Comparable<SearchResult> {
    private String mChapterName;
    private String mResultContent;
    private int mLvResult;
    private String sentence;
    private String searchQuery;

    public int getLvResult() {
        return mLvResult;
    }

    public void setLvResult(int mLvResult) {
        this.mLvResult = mLvResult;
    }

    public String getSentence() {
        return sentence;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getChapterName() {
        return mChapterName;
    }

    public void setChapterName(String chapterName) {
        mChapterName = chapterName;
    }

    public String getResultContent() {
        return mResultContent;
    }

    public void setResultContent(String resultContent) {
        mResultContent = resultContent;
    }

    @Override
    public int compareTo(SearchResult searchResult) {
        int comparelv=((SearchResult)searchResult).getLvResult();
        return comparelv-this.getLvResult();
    }
}

