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
import studio.dinhduc.doctruyen.model.SpellCheckResult;

/**
 * Created by dinhduc on 02/12/2016.
 */

public class SpellCheckResultAdapter extends ArrayAdapter<SpellCheckResult> {
    private Context mContext;
    private ArrayList<SpellCheckResult> mSpellCheckResults;
    private int mLayoutId;

    public SpellCheckResultAdapter(Context context, int resource, List<SpellCheckResult> objects) {
        super(context, resource, objects);
        mContext = context;
        mSpellCheckResults = new ArrayList<>(objects);
        mLayoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpellCheckViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            viewHolder = new SpellCheckViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SpellCheckViewHolder) convertView.getTag();
        }
        viewHolder.bindData(mSpellCheckResults.get(position));
        return convertView;
    }

    class SpellCheckViewHolder {
        @BindView(R.id.tv_result_name)
        TextView mTvChapterName;
        @BindView(R.id.tv_result_content)
        TextView mTvResultContent;

        public SpellCheckViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        private void bindData(SpellCheckResult spellCheckResult) {
            mTvChapterName.setText(spellCheckResult.getChapterName());
            mTvResultContent.setText(Html.fromHtml(spellCheckResult.getLine()));
        }
    }
}
