package studio.dinhduc.doctruyen.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

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
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chapter_content, menu);
        return true;
    }

}
