package studio.dinhduc.doctruyen.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.util.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ArrayList<String> mNovelNames = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    @BindView(R.id.tool_bar) Toolbar mToolBar;
    @BindView(R.id.lv_main_list_novel) ListView mLvListNovel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PermissionUtils.requestPermission(this, 0, Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
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
                Intent intent = new Intent(getBaseContext(), ListChapterActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_NAME, mNovelNames.get(position));
                startActivity(intent);
            }
        });
    }
    //vxhuy
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
                return true;
            case R.id.menu_mic_control: startSpeechToText();
                return true;
            default: return true;
        }
    }
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
                    Log.d(TAG, "onActivityResult: " + text);

                    if (text.equals(new String("đóng ứng dụng")) ||text.equals(new String("exit"))){
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startActivity(startMain);
                        finish();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (PermissionUtils.isPermissionGranted(
                    permissions, grantResults, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                initView();
                getControlWidget();
            }
        }
    }
}

