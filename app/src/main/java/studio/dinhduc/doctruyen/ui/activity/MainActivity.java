package studio.dinhduc.doctruyen.ui.activity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    ArrayList<String> mNovelNames = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_main_list_novel) ListView mLvListNovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getControlWidget();
    }

    private void initView() {
        setSupportActionBar(mToolBar);

        File f = new File(Const.APP_DIR_PATH);
        if (!(f.mkdirs() || f.isDirectory())) {
            return;
        }
        ArrayList<File> novelList = new ArrayList<>(Arrays.asList(f.listFiles()));
        for (int i = 0; i < novelList.size(); i++) {
            mNovelNames.add(novelList.get(i).getName());
        }
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNovelNames);
        mLvListNovel.setAdapter(mAdapter);
    }

    private void getControlWidget() {
        mLvListNovel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String novelDirPath = Const.APP_DIR_PATH + File.separator + mNovelNames.get(position);
                Intent intent = new Intent(getBaseContext(), ListChapterActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_DIR_PATH, novelDirPath);
                startActivity(intent);
            }
        });
    }

}
