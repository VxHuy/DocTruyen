package studio.dinhduc.doctruyen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    @BindView(R.id.lv_result) ListView mLvSearchResult;
    @BindView(R.id.tv_result_chapter_count) TextView mTvResultChapterCount;
    @BindView(R.id.pb_result_progress) ProgressBar mPbResultProgress;
    @BindView(R.id.tv_result_count) TextView mTvResultCount;

    private ArrayList<SearchResult> mSearchResults = new ArrayList<>();
    private SearchResultAdapter mAdapter;
    private String mNovelDirPath;
    private ArrayList<String> mChapterNames;
    private String mSearchQuery;
    private int mCount = 0;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        initView();
        getWidgetControl();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        mSearchQuery = intent.getStringExtra(Const.KeyIntent.KEY_SEARCH_QUERY);
        mChapterNames = intent.getStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME);
        mNovelDirPath = intent.getStringExtra(Const.KeyIntent.KEY_NOVEL_PATH);
        mAdapter = new SearchResultAdapter(
                getBaseContext(), R.layout.item_result, mSearchResults);
        mLvSearchResult.setAdapter(mAdapter);
        mPbResultProgress.setMax(mChapterNames.size());
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int chapterSize = mChapterNames.size();
                for (int i = 0; i < chapterSize; i++) {
                    if (mThread.isInterrupted()) {
                        mThread = null;
                        System.gc();
                        return;
                    }
                    final int finalI1 = i;
                    Log.d(TAG, "run: " + i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPbResultProgress.setProgress(finalI1 + 1);
                            mTvResultChapterCount.setText("Chương: " + (finalI1 + 1) + "/" + chapterSize);
                        }
                    });
                    String chapterName = mChapterNames.get(i);
                    String chapterPath = mNovelDirPath + File.separator + chapterName;
                    String chapterContent = CommonUtils.readFileTxt(chapterPath); //trả lại 1 content of chapter
                    SearchResult searchResult = null;
                    try {
                        if (mSearchQuery.contains(" ")) {
                            searchResult = CommonUtils.getSentenceMultilSearch(chapterContent, mSearchQuery);
                            if (searchResult != null) {
                                searchResult.setSentence(searchResult.getSentence().replaceAll("<br>", ""));
                                searchResult.setChapterName(chapterName);
                                searchResult.setResultContent(searchResult.getSentence());
                                mCount++;
                            }
                        } else {
                            String sentence = CommonUtils.getSentence(chapterContent, mSearchQuery); //trả lại câu có chứa cụm từ/từ cần tìm trong chương
                            if (sentence != null) {
                                sentence = sentence.replaceAll("<br>", "");
                                searchResult = new SearchResult();
                                searchResult.setChapterName(chapterName);
                                searchResult.setResultContent(sentence);
                                searchResult.setSearchQuery(mSearchQuery);
                                mCount++;
                            }
                        }
                        final SearchResult finalSearchResult = searchResult;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (finalSearchResult != null) {
                                    mSearchResults.add(finalSearchResult);
                                    Collections.sort(mSearchResults);
                                    mAdapter.notifyDataSetChanged();
                                    mTvResultCount.setText("Có " + mCount + " kết quả tìm kiếm từ: \"" + mSearchQuery + "\"");
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("LOG", e.toString());
                    }
                }

            }
        });
        mThread.start();
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

    @Override
    protected void onDestroy() {
        if (mThread != null) {
            mThread.interrupt();
        }
        super.onDestroy();
    }
}
