package studio.dinhduc.doctruyen.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.model.SearchResult;
import studio.dinhduc.doctruyen.util.CommonUtils;

/**
 * Created by dinhduc on 14/11/2016.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {
    private Context mContext;
    private ArrayList<SearchResult> mSearchResults;
    private int mLayoutId;

    public SearchResultAdapter(Context context, int resource, List<SearchResult> objects) {
        super(context, resource, objects);
        mContext = context;
        mSearchResults = (ArrayList<SearchResult>) objects;
        mLayoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            viewHolder = new SearchResultViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchResultViewHolder) convertView.getTag();
        }
        viewHolder.bindData(mSearchResults.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return mSearchResults.size();
    }

    class SearchResultViewHolder {
        @BindView(R.id.tv_result_name)
        TextView mTvChapterName;
        @BindView(R.id.tv_result_content)
        TextView mTvResultContent;

        public SearchResultViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        private void bindData(SearchResult searchResult) {
            String chapterContent = searchResult.getResultContent();
            chapterContent = CommonUtils.highLightQueryInText(
                    mContext, searchResult.getSearchQuery(), chapterContent);
            mTvChapterName.setText(searchResult.getChapterName());
            mTvResultContent.setText(Html.fromHtml(chapterContent));
        }
    }

}
