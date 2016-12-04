package studio.dinhduc.doctruyen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.model.SpellCheckResult;
import studio.dinhduc.doctruyen.ui.adapter.SpellCheckResultAdapter;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.ui.rule.Dictionary;
import studio.dinhduc.doctruyen.util.RuleUtils;

public class SpellCheckResultActivity extends AppCompatActivity {
    private static final String TAG = "SpellCheckResultActivit";
    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_result) ListView mLvSpellCheck;
    @BindView(R.id.pb_result_progress) ProgressBar mPbResultProgress;
    @BindView(R.id.tv_result_count) TextView mTvResultCount;
    @BindView(R.id.tv_result_chapter_count) TextView mTvResultChapterCount;
    private ArrayList<String> mChapterNames;
    private String mNovelDirPath;
    private Dictionary mDictionary;
    private int count = 0;
    private ArrayList<SpellCheckResult> mSpellCheckResults = new ArrayList<>();
    private SpellCheckResultAdapter mAdapter;

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

        Intent intent = getIntent();
        mChapterNames = intent.getStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME);
        mNovelDirPath = intent.getStringExtra(Const.KeyIntent.KEY_NOVEL_PATH);
        mDictionary = new Dictionary();
        mDictionary.build(this, "dictionary.txt");
        mPbResultProgress.setMax(mChapterNames.size());
        mAdapter = new SpellCheckResultAdapter(getBaseContext(), R.layout.item_result, mSpellCheckResults);
        mLvSpellCheck.setAdapter(mAdapter);
        RuleUtils.setUp();
        checkSpell();
    }

    private void getWidgetControl() {
        mLvSpellCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), ChapterContentActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelDirPath);
                intent.putExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                intent.putExtra(Const.KeyIntent.KEY_SEARCH_QUERY, mSpellCheckResults.get(position).getWord());
                intent.putExtra(Const.KeyIntent.KEY_CHAPTER_CHOSEN_POSITION,
                        mChapterNames.indexOf(mSpellCheckResults.get(position).getChapterName()));
                intent.putExtra(Const.KeyIntent.KEY_SPELL_CHECK, true);
                startActivity(intent);
            }
        });
    }

    private void checkSpell() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int chapterSize = mChapterNames.size();
                for (int i = 0; i < chapterSize; i++) {
                    final int finalI1 = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPbResultProgress.setProgress(finalI1 + 1);
                            mTvResultChapterCount.setText("Chương: " + (finalI1 + 1) + "/" + chapterSize);
                        }
                    });
                    File file = new File(mNovelDirPath + File.separator + mChapterNames.get(i));
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        InputStreamReader fileReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            Log.d(TAG, line);
                            line = deleteSign(line);
                            final String[] words = line.split("\\s+");
                            for (String word : words) {
                                if (!mDictionary.contains(word) && RuleUtils.check(word) && !word.equals("")) {
                                    Log.d(TAG, "test: " + "-" + word + "-");
                                    final SpellCheckResult result = new SpellCheckResult();
                                    result.setWord(word);
                                    result.setChapterName(mChapterNames.get(i));
                                    result.setLine(line);
                                    count++;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSpellCheckResults.add(result);
                                            mAdapter.notifyDataSetChanged();
                                            mTvResultCount.setText("Số từ: " + count);
                                        }
                                    });
                                }
                            }
                        }
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    // delete sign: ? : ! ...
    private String deleteSign(String words) {

        words = words.replaceAll("[?.,:;\"!()-]", " ");
        return words;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
