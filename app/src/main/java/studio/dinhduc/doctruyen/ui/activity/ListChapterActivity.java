package studio.dinhduc.doctruyen.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.constant.Const;

public class ListChapterActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_list_chapter) ListView mLvListChapter;

    ArrayList<String> mChapterNames = new ArrayList<>();
    ArrayAdapter<String> mAdapter;

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

        String novelDirPath = getIntent().getStringExtra(Const.KeyIntent.KEY_NOVEL_DIR_PATH);
        File novelDir = new File(novelDirPath);

        ArrayList<File> chapters = new ArrayList<>(Arrays.asList(novelDir.listFiles()));
        for (File chapter : chapters) {
           mChapterNames.add(chapter.getName());
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mChapterNames);
        mLvListChapter.setAdapter(mAdapter);
    }

    private void getWidgetControl() {
        mLvListChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
