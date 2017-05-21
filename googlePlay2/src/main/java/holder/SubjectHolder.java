package holder;

import utils.BitmapHelper;
import utils.UIUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import base.BaseHolder;
import bean.SubjectInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import conf.Constants.URLS;

public class SubjectHolder extends BaseHolder<SubjectInfoBean> {
	@ViewInject(R.id.item_subject_iv_icon)
	ImageView mIvIcon;
	@ViewInject(R.id.item_subject_tv_title)
	TextView mTvTitle;
	
	@Override
	public View initHolderView() {
		
		View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
		ViewUtils.inject(this,view);
		
		return view;
	}//initHolderView

	@Override
	public void refreshHolderView(SubjectInfoBean data) {
		
		mTvTitle.setText(data.des);
		mIvIcon.setImageResource(R.drawable.ic_default);
		
		BitmapHelper.display(mIvIcon, URLS.IMAGEBASEURL + data.url);
	}//refreshHolderView

}//End
