package studio.dinhduc.doctruyen.ui.custom;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.util.RuleUtils;

/**
 * Created by dinhduc on 28/11/2016.
 */

public class CheckSpellDialog {
    private static final String TAG = "CheckSpellDialog";
    @BindView(R.id.pb_check_progress) ProgressBar mPbCheckProgress;
    @BindView(R.id.tv_check_word_number) TextView mTvCheckWordNumber;
    @BindView(R.id.lv_check_list_word) ListView mLvCheckListWord;
    @BindView(R.id.tv_check_close) TextView mTvCheckClose;
    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private CloseCallback mCallback;
    private ArrayList<String> mListErrorWords = new ArrayList<>();
    private String mChapterContent;
    private ArrayAdapter<String> mAdapter;
    private Thread mThread;
    private int mCount = 0;

    public CheckSpellDialog(Activity activity, String content) {
        mActivity = activity;
        mChapterContent = content;
        initView();
    }

    private void checkSpell() {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = mChapterContent.replace("<br>", "");
                    final String[] words = content.split("\\s+");
                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];
                        word = deleteSign(word);
                        if (word.length() > 0 && RuleUtils.check(word)) {
                            final int finalI = i;
                            final String finalWord = word;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int progress = (int) (((float) finalI + 1) / words.length * 100);
                                    Log.d(TAG, "run: " + progress);
                                    mPbCheckProgress.setProgress(progress);
                                    mTvCheckWordNumber.setText(String.valueOf(++mCount));
                                    mListErrorWords.add(finalWord);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                }
            });
            mThread.start();
        }
    }

    // delete sign: ? : ! ...
    private String deleteSign(String word) {

        String sign = "?.,;:\"!)";
        if ((word.charAt(0) == '"') || (word.charAt(0) == '(')) {
            word = word.substring(1);
        }
        while ((word.length() > 0) && (sign.contains("" + word.charAt(word.length() - 1)))) {
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_check_spell, null);
        ButterKnife.bind(this, view);
        mAlertDialog = new AlertDialog.Builder(mActivity)
                .setCancelable(false)
                .setView(view)
                .create();
        mTvCheckClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onCloseButtonClick(CheckSpellDialog.this);
            }
        });
        mAdapter = new ArrayAdapter<>(
                mActivity, android.R.layout.simple_list_item_1, mListErrorWords);
        mLvCheckListWord.setAdapter(mAdapter);
    }

    public CheckSpellDialog setTitle(String title) {
        mAlertDialog.setTitle(title);
        return this;
    }

    public CheckSpellDialog setButtonCallback(CloseCallback callback) {
        mCallback = callback;
        return this;
    }

    public CheckSpellDialog show() {
        mAlertDialog.show();
        checkSpell();
        return this;
    }

    public void cancel() {
        mThread.interrupt();
        mAlertDialog.cancel();
    }

    public interface CloseCallback {
        void onCloseButtonClick(CheckSpellDialog checkSpellDialog);
    }

}

