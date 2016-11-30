package studio.dinhduc.doctruyen.ui.custom;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.activity.ChapterContentActivity;
import studio.dinhduc.doctruyen.ui.constant.Const;

/**
 * Created by dinhduc on 30/11/2016.
 */

public class GotoDialog {
    @BindView(R.id.spn_goto_novel) Spinner mSpnGotoNovel;
    @BindView(R.id.spn_goto_chapter) Spinner mSpnGotoChapter;
    @BindView(R.id.tv_go_to) TextView mTvGoto;
    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private ArrayList<String> mChapterNames;
    private String mNovelPath;

    public GotoDialog(Activity activity) {
        mActivity = activity;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_goto, null);
        ButterKnife.bind(this, view);
        mAlertDialog = new AlertDialog.Builder(mActivity)
                .setView(view)
                .create();
        File f = new File(Const.APP_DIR_PATH);
        final ArrayList<String> novelNames = new ArrayList<>();
        ArrayList<File> novelList = new ArrayList<>(Arrays.asList(f.listFiles()));
        for (int i = 0; i < novelList.size(); i++) {
            novelNames.add(novelList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mActivity, android.R.layout.simple_dropdown_item_1line, novelNames);
        mSpnGotoNovel.setAdapter(adapter);

        mSpnGotoNovel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String novelName = novelNames.get(position);
                mNovelPath = Const.APP_DIR_PATH + File.separator + novelName;
                File f = new File(mNovelPath);
                mChapterNames = new ArrayList<>();
                ArrayList<File> chapterList = new ArrayList<>(Arrays.asList(f.listFiles()));
                for (int i = 0; i < chapterList.size(); i++) {
                    mChapterNames.add(chapterList.get(i).getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        mActivity, android.R.layout.simple_dropdown_item_1line, mChapterNames);
                mSpnGotoChapter.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mTvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mSpnGotoChapter.getSelectedItemPosition();
                Intent intent = new Intent(mActivity, ChapterContentActivity.class);
                intent.putExtra(Const.KeyIntent.KEY_NOVEL_PATH, mNovelPath);
                intent.putExtra(Const.KeyIntent.KEY_LIST_CHAPTER_NAME, mChapterNames);
                intent.putExtra(Const.KeyIntent.KEY_CHAPTER_CHOSEN_POSITION, position);
                mActivity.startActivity(intent);
                cancel();
            }
        });
    }

    public GotoDialog setTitle(String title) {
        mAlertDialog.setTitle(title);
        return this;
    }

    public GotoDialog show() {
        mAlertDialog.show();
        return this;
    }

    public void cancel() {
        mAlertDialog.cancel();
    }

}
