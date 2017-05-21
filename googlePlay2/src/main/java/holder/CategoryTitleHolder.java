package holder;

import utils.UIUtils;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import base.BaseHolder;
import bean.CategoryInfoBean;

public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

	private TextView	mTvTitle;

	@Override
	public View initHolderView() {
		mTvTitle = new TextView(UIUtils.getContext());
		mTvTitle.setTextSize(15);
		int padding = UIUtils.dipToPx(5);
		mTvTitle.setPadding(padding, padding, padding, padding);
		mTvTitle.setBackgroundColor(Color.WHITE);
		
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mTvTitle.setLayoutParams(params);
		return mTvTitle;
	}

	@Override
	public void refreshHolderView(CategoryInfoBean data) {
		mTvTitle.setText(data.title);
		
	}
}
