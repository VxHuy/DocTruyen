package studio.dinhduc.doctruyen.ui.activity;

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.util.CommonUtils;

public class ListChapterActivity extends AppCompatActivity {
    private static final String TAG = "ListChapterActivity";
    private final int SPEECH_RECOGNITION_CODE = 1;

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_list_chapter) ListView mLvListChapter;

    private ArrayList<String> mChapterNames = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private String mNovelDirPath;
    private SearchView mSearchView;
    private String mNovelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chapter);
        ButterKnife.bind(this);
        initView();
        getWidgetControl();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNovelName = getIntent().getStringExtra(Const.KeyIntent.KEY_NOVEL_NAME);
        getSupportActionBar().setTitle(mNovelName);

        mNovelDirPath = Const.APP_DIR_PATH + File.separator + mNovelName;

        File novelDir = new File(mNovelDirPath);

        ArrayList<File> chapters = new ArrayList<>(Arrays.asList(novelDir.listFiles()));
        for (File chapter : chapters) {
            mChapterNames.add(chapter.getName());
        }

        final Dialog dialog = CommonUtils.showCircleLoadingDialog(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Collections.sort(mChapterNames, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        String numberString1 = s1.replaceAll("[^0-9]", "");
                        String numberString2 = s2.replaceAll("[^0-9]", "");
                        if (numberString1.equals("") || numberString2.equals("")) {
                            return numberString1.compareTo(numberString2);
                        } else {
                            Integer chapterNumber1 = Integer.valueOf(numberString1);
                            Integer chapterNumber2 = Integer.valueOf(numberString2);
                            return chapterNumber1.compareTo(chapterNumber2);
                        }
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        mAdapter = new ArrayAdapter<>(
                                getBaseContext(), android.R.layout.simple_list_item_1, mChapterNames);
                        mLvListChapter.setAdapter(mAdapter);
                    }
                });
            }
        }).start();
    }

    private void getWidgetControl() {
        mLvListChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), ChapterContentActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
                intent.putExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                intent.putExtra(Const.KeyIntent.KEY_CHAPTER_CHOSEN_POSITION, position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_chapter, menu);
        MenuItem item = menu.findItem(R.id.menu_list_chapter_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                startSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    //vxhuy
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_mic_search:
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
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startSearch(String query) {
        Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
        intent.putExtra(Const.KeyIntent.KEY_SEARCH_QUERY, query);
        intent.putStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
        intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
        startActivity(intent);
    }

    //vxhuy
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    Log.d(TAG, "onActivityResult: " + text);
                    if (text.equals("quay lại") || text.equals("back")) {
                        onBackPressed();
                    } else {
                        if (text.equals("thoát") || text.equals("exit")) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startActivity(startMain);
                            finish();
                        } else {
                            if (text.equals("trang chủ") || text.equals("home")) {
                                Intent startMain = new Intent(this, MainActivity.class);
                                startActivity(startMain);

                            } else {
                                startSearch(text);
                            }
                        }
                    }
                }
                break;
            }
        }
    }
}
