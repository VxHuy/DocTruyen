package studio.dinhduc.doctruyen.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.util.CommonUtils;

/**
 * Created by dinhduc on 29/11/2016.
 */

public class ChapterContentAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mChapterNames;
    private String mNovelPath;
    private String mSearchQuery;
    private int mChapterChosenPosition;

    public ChapterContentAdapter(Context context, ArrayList<String> chapterNames, String novelPath) {
        mContext = context;
        mChapterNames = chapterNames;
        mNovelPath = novelPath;
    }

    public void hightLight(String query, int chosenPosition) {
        mSearchQuery = query;
        mChapterChosenPosition = chosenPosition;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chapter_content, null);
        view.setTag(position);
        final TextView mTvChapterContent = (TextView) view.findViewById(R.id.tv_chapter_content);
        final ScrollView mSvChapterContent = (ScrollView) view.findViewById(R.id.sv_chapter_content);
        String chapterPath = mNovelPath + File.separator + mChapterNames.get(position);
        String chapterContent = CommonUtils.readFileTxt(chapterPath);
        final String content;

        if (mSearchQuery != null && position == mChapterChosenPosition) {
            content = CommonUtils.highLightQueryInText(mContext, mSearchQuery, chapterContent);
            mTvChapterContent.setText(Html.fromHtml(content));

            mSvChapterContent.post(new Runnable() {
                @Override
                public void run() {
                    int offset = content.indexOf(mSearchQuery);
                    // get line number from index
                    int line = mTvChapterContent.getLayout().getLineForOffset(offset);
                    // get coordinateY in textview
                    int coordinateY = mTvChapterContent.getLayout().getLineTop(line);
                    mSvChapterContent.scrollTo(0, coordinateY - 400);
                }
            });
        } else {
            content = chapterContent;
            mTvChapterContent.setText(Html.fromHtml(content));
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mChapterNames.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
