package studio.dinhduc.doctruyen.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_list_chapter) ListView mLvListChapter;

    private ArrayList<String> mChapterNames = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private String mNovelDirPath;

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

        mNovelDirPath = getIntent().getStringExtra(Const.KeyIntent.KEY_NOVEL_DIR_PATH);
        File novelDir = new File(mNovelDirPath);

        ArrayList<File> chapters = new ArrayList<>(Arrays.asList(novelDir.listFiles()));
        for (File chapter : chapters) {
            mChapterNames.add(chapter.getName());
        }

        final Dialog dialog = CommonUtils.showLoadingDialog(this);
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
                intent.putExtra(Const.KeyIntent.KEY_CHAPTER_PATH,
                        mNovelDirPath + File.separator + mChapterNames.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
