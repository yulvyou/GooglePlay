package holder;

import java.util.List;

import utils.BitmapHelper;
import utils.UIUtils;
import views.RatioLayout;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import base.BaseHolder;
import bean.AppInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import conf.Constants.URLS;

public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {
	
	@ViewInject(R.id.app_detail_pic_iv_container)
	LinearLayout mContainerPic;
	
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_pic, null);
		//注入
		ViewUtils.inject(this,view);

		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		//得到截图的URL
		List<String> picUrls = data.screen;
		for(int i = 0 ;i<picUrls.size() ; i++)
		{
			String url = picUrls.get(i);
			ImageView ivPic = new ImageView(UIUtils.getContext());
			ivPic.setImageResource(R.drawable.ic_default);//默认图片
			BitmapHelper.display(ivPic, URLS.IMAGEBASEURL + url);
			//控件宽度等于屏幕的1/3
			int widthPixels = UIUtils.getResources().getDisplayMetrics().widthPixels;//得到屏幕的宽度
			widthPixels = widthPixels - mContainerPic.getPaddingLeft() - mContainerPic.getPaddingRight();
			int childWidth = widthPixels / 3;
			//已知控件的宽度和图片的宽高比，去动态的计算控件的高度
			RatioLayout rl = new RatioLayout(UIUtils.getContext());
			rl.setPicRatio(150/250);//设置宽高比
			rl.setRelative(RatioLayout.RELATIVE_WIDTH);

			rl.addView(ivPic);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			if (i != 0) {// 不处理第一张图片的左边距
				params.leftMargin = UIUtils.dipToPx(5);
			}
			
			mContainerPic.addView(rl , params);
			
		}//for
	}//refreshHolderView

}//End
