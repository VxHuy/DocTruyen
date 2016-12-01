package studio.dinhduc.doctruyen.ui.custom;

import android.app.Activity;
import android.os.AsyncTask;
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
    @BindView(R.id.tv_check_high_light) TextView mTvCheckHighLight;
    private Activity mActivity;
    private AlertDialog mAlertDialog;
    private ButtonCallback mCallback;
    private ArrayList<String> mListErrorWords = new ArrayList<>();
    private String mChapterContent;
    private ArrayAdapter<String> mAdapter;
    private int mCount = 0;
    private CheckSpellTask mTask;
    private boolean mIsRunning;
    private boolean mIsContentChange;

    public CheckSpellDialog(Activity activity) {
        mActivity = activity;
        initView();
    }

    // delete sign: ? : ! ...
    private String deleteSign(String word) {

        String sign = "?.,;:\"!)";
        if (word.length() > 0) {
            if ((word.charAt(0) == '"') || (word.charAt(0) == '(')) {
                word = word.substring(1);
            }
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
        mTvCheckHighLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onHighlightButtonClick(CheckSpellDialog.this, mListErrorWords);
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

    public CheckSpellDialog setButtonCallback(ButtonCallback callback) {
        mCallback = callback;
        return this;
    }

    public CheckSpellDialog show() {
        mAlertDialog.show();
        if (mTask == null || mTask.isCancelled() || mIsContentChange) {
            mListErrorWords.clear();
            mCount = 0;
            mTvCheckWordNumber.setText(String.valueOf(mCount));
            mPbCheckProgress.setProgress(0);
            mTask = new CheckSpellTask();
            mTask.execute(mChapterContent);
        }
        return this;
    }

    public void setChapterContent(String chapterContent) {
        mChapterContent = chapterContent;
        mIsContentChange = true;
    }

    public void cancel() {
        if (mIsRunning) {
            mTask.cancel(true);
        }
        mAlertDialog.cancel();
    }

    public interface ButtonCallback {
        void onCloseButtonClick(CheckSpellDialog checkSpellDialog);

        void onHighlightButtonClick(CheckSpellDialog checkSpellDialog, ArrayList<String> listErrors);
    }

    private class CheckSpellTask extends AsyncTask<String, Integer, String> {
        private String[] mWords;

        @Override
        protected String doInBackground(String... strings) {
            mIsRunning = true;
            String content = strings[0].replace("<br>", " ");
            mWords = content.split("\\s+");
            for (int i = 0; i < mWords.length; i++) {
                if (isCancelled()) {
                    break;
                }
                publishProgress(i);
                String word = mWords[i];
                word = deleteSign(word);
                if (word.length() > 0 && RuleUtils.check(word)) {
                    ++mCount;
                    final String finalWord = word;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListErrorWords.add(finalWord);
                            mTvCheckWordNumber.setText(String.valueOf(mCount));
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            mIsRunning = false;
            mIsContentChange = false;
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = (int) (((float) values[0] + 1) / mWords.length * 100);
            Log.d(TAG, "run: " + progress);
            mPbCheckProgress.setProgress(progress);
        }

    }

}

