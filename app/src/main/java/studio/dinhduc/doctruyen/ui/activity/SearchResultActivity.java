package studio.dinhduc.doctruyen.ui.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
                    Log.d(TAG, "run: " + progress);
                    dialog.setProgress(progress);
                    String chapterName = mChapterNames.get(i);
                    String chapterPath = mNovelDirPath + File.separator + chapterName;
                    String chapterContent = CommonUtils.readFileTxt(chapterPath); //trả lại 1 content of chapter

                    try {
                        if (mSearchQuery.contains(" ")) {
                            SearchResult sr = CommonUtils.getSentenceMultilSearch(chapterContent, mSearchQuery);
                            if (sr != null) {
                                sr.setSentence(sr.getSentence().replaceAll("<br>", ""));
                                String highlightSentence = CommonUtils.hiLightQueryInText(
                                        getBaseContext(), sr.getSearchQuery(), sr.getSentence());
                                sr.setChapterName(chapterName);
                                sr.setResultContent(highlightSentence);
                                mSearchResults.add(sr);
                            }
                        } else {
                            String sentence = CommonUtils.getSentence(chapterContent, mSearchQuery); //trả lại câu có chứa cụm từ/từ cần tìm trong chương
                            if (sentence != null) {
                                sentence = sentence.replaceAll("<br>", "");
                                String highlightSentence = CommonUtils.hiLightQueryInText(
                                        getBaseContext(), mSearchQuery, sentence);
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
                                getBaseContext(), R.layout.item_search_result, mSearchResults);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_chapter, menu);
        MenuItem item = menu.findItem(R.id.menu_list_chapter_search);
        SearchView mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_SEARCH_QUERY, query);
                intent.putStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_mic_control:
                startSpeechToText();
                return true;
            default:
                return true;
        }
    }

    //vxhuy
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    if (text.equals(new String("quay lại")) || text.equals(new String("back"))) {
                        onBackPressed();
                    } else {
                        if (text.equals(new String("đóng ứng dụng")) || text.equals(new String("exit"))) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startActivity(startMain);
                            finish();
                        } else {
                            if (text.equals(new String("trang chủ")) || text.equals(new String("home"))) {
                                Intent startMain = new Intent(this, MainActivity.class);
                                startActivity(startMain);
                            } else {
                                Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                                intent.putExtra(Const.KeyIntent.KEY_SEARCH_QUERY, text);
                                intent.putStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
                                startActivity(intent);
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
