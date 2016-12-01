package studio.dinhduc.doctruyen.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.adapter.ChapterContentAdapter;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.ui.custom.CheckSpellDialog;
import studio.dinhduc.doctruyen.util.CommonUtils;
import studio.dinhduc.doctruyen.util.RuleUtils;

public class ChapterContentActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.vpg_chapter_content) ViewPager mVpgChapterContent;
    private String mSearchQuery;
    private CheckSpellDialog mCheckSpellDialog;
    private ChapterContentAdapter mChapterContentAdapter;
    private ArrayList<String> mChapterNames;
    private String mNovelPath;
    private String mChapterContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_content);
        ButterKnife.bind(this);
        initView();
        getWidgetControl();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RuleUtils.setUp();
        Intent intent = getIntent();
        mNovelPath = intent.getStringExtra(Const.KeyIntent.KEY_NOVEL_PATH);
        mChapterNames = intent.getStringArrayListExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME);
        mSearchQuery = intent.getStringExtra(Const.KeyIntent.KEY_SEARCH_QUERY);
        int chapterChosenPosition = intent.getIntExtra(Const.KeyIntent.KEY_CHAPTER_CHOSEN_POSITION, 0);
        String chapterName = mChapterNames.get(chapterChosenPosition);
        getSupportActionBar().setTitle(chapterName);
        mChapterContentAdapter = new ChapterContentAdapter(this, mChapterNames, mNovelPath);
        mChapterContentAdapter.hightLight(mSearchQuery, chapterChosenPosition);
        mVpgChapterContent.setAdapter(mChapterContentAdapter);
        mVpgChapterContent.setCurrentItem(chapterChosenPosition);


        mCheckSpellDialog = new CheckSpellDialog(ChapterContentActivity.this)
                .setTitle("Check Spell")
                .setButtonCallback(new CheckSpellDialog.ButtonCallback() {
                    @Override
                    public void onCloseButtonClick(CheckSpellDialog checkSpellDialog) {
                        checkSpellDialog.cancel();
                    }

                    @Override
                    public void onHighlightButtonClick(CheckSpellDialog checkSpellDialog, ArrayList<String> listErrors) {
                        checkSpellDialog.cancel();
                        String chapterContent = mChapterContent;
                        try {
                            TextView textView = (TextView) mVpgChapterContent.findViewWithTag(mVpgChapterContent.getCurrentItem()).findViewById(R.id.tv_chapter_content);
                            for (String error : listErrors) {
                                chapterContent = CommonUtils.highLightInText(getBaseContext(), error, chapterContent);
                                textView.setText(Html.fromHtml(chapterContent));
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mChapterContent = CommonUtils.readFileTxt(mNovelPath + File.separator + chapterName);
        mCheckSpellDialog.setChapterContent(mChapterContent);
    }

    private void getWidgetControl() {
        mVpgChapterContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String chapterName = mChapterNames.get(position);
                getSupportActionBar().setTitle(chapterName);
                mChapterContent = CommonUtils.readFileTxt(mNovelPath + File.separator + chapterName);
                mCheckSpellDialog.setChapterContent(mChapterContent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                onBackPressed();
                return true;
            case R.id.menu_mic_control:
                startSpeechToText();
                return true;
            case R.id.menu_chapter_content_check:
                mCheckSpellDialog.show();
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
                    if (text.equals("quay lại") || text.equals("back")) {
                        onBackPressed();
                    } else {
                        if (text.equals("đóng ứng dụng") || text.equals("exit")) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startActivity(startMain);
                            finish();
                        } else {
                            if (text.equals("trang chủ") || text.equals("home")) {
                                Intent startMain = new Intent(this, MainActivity.class);
                                startActivity(startMain);
                            }
                        }
                    }
                }
                break;
            }
        }
    }


}
