package studio.dinhduc.doctruyen.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.util.CommonUtils;

public class ChapterContentActivity extends AppCompatActivity {

    @BindView(R.id.tv_chapter_content) TextView mTvContent;
    @BindView(R.id.tool_bar) Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_content);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String chapterPath = getIntent().getStringExtra(Const.KeyIntent.KEY_CHAPTER_PATH);
        String chapterContent = CommonUtils.readFileTxt(chapterPath);
        mTvContent.setText(chapterContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
