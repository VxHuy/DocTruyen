package studio.dinhduc.doctruyen.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.model.SearchResult;
import studio.dinhduc.doctruyen.ui.adapter.SearchResultAdapter;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.util.CommonUtils;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = "SearchResultActivity";

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_search_result) ListView mLvSearchResult;

    private ArrayList<SearchResult> mSearchResults = new ArrayList<>();
    private SearchResultAdapter mAdapter;
    private String mNovelDirPath;
    private ArrayList<String> mChapterNames;
    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initView();
        getWidgetControl();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mSearchQuery = intent.getStringExtra(Const.KeyIntent.KEY_SEARCH_QUERY);
        mChapterNames = intent.getStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME);
        mNovelDirPath = intent.getStringExtra(Const.KeyIntent.KEY_NOVEL_PATH);
        final ProgressDialog dialog = CommonUtils.showProgressLoadingDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mChapterNames.size(); i++) {
                    int progress = ((int) ((float) i / mChapterNames.size() * 100));
//                    Log.d(TAG, "run: " + progress);
                    dialog.setProgress(progress);
                    String chapterName = mChapterNames.get(i);
                    String chapterPath = mNovelDirPath + File.separator + chapterName;
                    String chapterContent = CommonUtils.readFileTxt(chapterPath); //trả lại 1 content of chapter

                    try {
                        if (mSearchQuery.contains(" ")) {
                            SearchResult sr = CommonUtils.getSentenceMultilSearch(chapterContent, mSearchQuery);
                            if (sr != null) {
                                sr.setSentence(sr.getSentence().replaceAll("<br>", ""));
                                String highlightSentence = CommonUtils.highLightQueryInText(
                                        getBaseContext(), sr.getSearchQuery(), sr.getSentence(), true);
                                sr.setChapterName(chapterName);
                                sr.setResultContent(highlightSentence);
                                mSearchResults.add(sr);
                            }
                        } else {
                            String sentence = CommonUtils.getSentence(chapterContent, mSearchQuery); //trả lại câu có chứa cụm từ/từ cần tìm trong chương
                            if (sentence != null) {
                                sentence = sentence.replaceAll("<br>", "");
                                String highlightSentence = CommonUtils.highLightQueryInText(
                                        getBaseContext(), mSearchQuery, sentence, true);
                                SearchResult searchResult = new SearchResult();
                                searchResult.setChapterName(chapterName);
                                searchResult.setResultContent(highlightSentence);
                                searchResult.setSearchQuery(mSearchQuery);
                                mSearchResults.add(searchResult);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("LOG", e.toString());
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Collections.sort(mSearchResults);
                        mAdapter = new SearchResultAdapter(
                                getBaseContext(), R.layout.item_result, mSearchResults);
                        mLvSearchResult.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }

    private void getWidgetControl() {
        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), ChapterContentActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
                intent.putExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                intent.putExtra(Const.KeyIntent.KEY_SEARCH_QUERY, mSearchResults.get(position).getSearchQuery());
                intent.putExtra(Const.KeyIntent.KEY_CHAPTER_CHOSEN_POSITION,
                        mChapterNames.indexOf(mSearchResults.get(position).getChapterName()));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

}
