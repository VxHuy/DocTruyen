package studio.dinhduc.doctruyen.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.ui.custom.CheckSpellDialog;
import studio.dinhduc.doctruyen.util.CommonUtils;
import studio.dinhduc.doctruyen.util.RuleUtils;

public class ChapterContentActivity extends AppCompatActivity {

    @BindView(R.id.tv_chapter_content) TextView mTvContent;
    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.sv_chapter_content) ScrollView mSvContent;
    private String mChapterContent;
    private String mSearchQuery;
    private CheckSpellDialog mCheckSpellDialog;

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
        String chapterPath = getIntent().getStringExtra(Const.KeyIntent.KEY_CHAPTER_PATH);
        mSearchQuery = getIntent().getStringExtra(Const.KeyIntent.KEY_SEARCH_QUERY);
        mChapterContent = CommonUtils.readFileTxt(chapterPath);
        String content;
        if (mSearchQuery == null) {
            content = mChapterContent;
        } else {
            content = CommonUtils.hiLightQueryInText(this, mSearchQuery, mChapterContent);
        }
        mTvContent.setText(Html.fromHtml(content));

        mCheckSpellDialog = new CheckSpellDialog(ChapterContentActivity.this, mChapterContent)
                .setTitle("Check Spell")
                .setButtonCallback(new CheckSpellDialog.CloseCallback() {
                    @Override
                    public void onCloseButtonClick(CheckSpellDialog checkSpellDialog) {
                        checkSpellDialog.cancel();
                    }
                });
    }

    private void getWidgetControl() {
        if (mSearchQuery != null) {
            mSvContent.post(new Runnable() {
                @Override
                public void run() {
                    int offset = mChapterContent.toLowerCase().indexOf(mSearchQuery.toLowerCase());
                    // get line number from index
                    int line = mTvContent.getLayout().getLineForOffset(offset);
                    // get coordinateY in textview
                    int coordinateY = mTvContent.getLayout().getLineTop(line);
                    mSvContent.scrollTo(0, coordinateY - 100);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_chapter_content_check) {
            mCheckSpellDialog.show();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
            case R.id.menu_mic_control: startSpeechToText();
                return true;
            case R.id.menu_chapter_content_check:  mCheckSpellDialog.show();
                return true;
            default: return true;
        }

    }

    //vxhuy
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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
                    if(text.equals(new String("quay lại")) ||text.equals(new String("back"))){
                        onBackPressed();
                    }else {
                        if (text.equals(new String("đóng ứng dụng")) || text.equals(new String("exit"))) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startActivity(startMain);
                            finish();
                        } else {
                            if (text.equals(new String("trang chủ")) || text.equals(new String("home"))) {
                                Intent startMain = new Intent(this,MainActivity.class);
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
