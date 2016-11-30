package studio.dinhduc.doctruyen.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by dinhduc on 29/11/2016.
 */

public class ChapterContentAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mChapterNames;

    public ChapterContentAdapter(Context context, ArrayList<String> chapterNames) {
        mContext = context;
        mChapterNames = chapterNames;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
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
