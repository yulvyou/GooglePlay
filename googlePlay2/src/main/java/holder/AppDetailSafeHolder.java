package holder;

import java.util.List;

import utils.BitmapHelper;
import utils.UIUtils;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import base.BaseHolder;
import bean.AppInfoBean;
import bean.AppInfoBean.AppInfoSafeBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import conf.Constants.URLS;

public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
	
	@ViewInject(R.id.app_detail_safe_des_container)
	LinearLayout mContainerDes;
	
	@ViewInject(R.id.app_detail_safe_pic_container)
	LinearLayout mContainerPic;
	
	@ViewInject(R.id.app_detail_safe_iv_arrow)
	ImageView mIvArrow;
	
	//折叠是开还是关
	private boolean isOpen = true;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_safe, null);
		//注入
		ViewUtils.inject(this,view);

		//设置点击事件
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		//获得safe安全部分的数据
		List<AppInfoSafeBean> safeBeans = data.safe;

		//循环加载安全的图片部分，和文字描述部分（描述图标，描述内容）
		for(AppInfoSafeBean appInfoSafeBean : safeBeans){
			/*==========图片部分=============*/
			ImageView ivIcon = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivIcon ,  URLS.IMAGEBASEURL + appInfoSafeBean.safeUrl);
			mContainerPic.addView(ivIcon);

			/*==========文字描述部分=============*/
			LinearLayout ll = new LinearLayout(UIUtils.getContext());
			//描述图标
			ImageView ivDes = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivDes, URLS.IMAGEBASEURL + appInfoSafeBean.safeDesUrl);
			//描述内容
			TextView tvDes = new TextView(UIUtils.getContext());
			tvDes.setText(appInfoSafeBean.safeDes);
			//加间距
			int padding = UIUtils.dipToPx(5);
			ll.setPadding(padding, padding, padding, padding);

			//注意顺序
			ll.addView(ivDes);
			ll.addView(tvDes);

			mContainerDes.addView(ll);

		}//for
		//将安全中的文字描述部分折叠起来
		toggle(false);

	}//refreshHolderView


	/**
	 * 点击事件处理
	 */
	@Override
	public void onClick(View v) {
		// TODO
		toggle(true);
	}


	/**
	 *
	 * @param needAnimation 判断折叠是否需要动画，为true表示需要动画，false表示不需要动画
	 */
	public void toggle(boolean needAnimation)
	{
		if(isOpen){//折叠。即：mContainerDes高度发生变化，应有的高度-->0
			//获得容器的高度
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();
			//实现动画效果的折叠(需要用到扩展包 nineoldandroids-2.4.0.jar)
			int start = measuredHeight;//动画的起点
			int end = 0;//动画的终点
			//折叠
			if(needAnimation)//动画效果的折叠
			{
				doAnimation(start, end);
			}else{//无动画效果的折叠
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}



		}else{//展开。即mContainerDes高度发生变化，0-->应有的高度
			//获得容器的高度
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();
			//实现动画效果的折叠(需要用到扩展包 nineoldandroids-2.4.0.jar)
			int start = 0;//动画的起点
			int end = measuredHeight;//动画的终点
			//展开
			if(needAnimation)//动画效果的展开
			{
				doAnimation(start, end);
			}else{//无动画效果的展开
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}
		}
		//箭头的旋转动画
		if(needAnimation){//需要动画效果折叠和展开时
			if(isOpen){
				//180度到0度旋转
				ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", 180,0);
				animator.start();
			}else{
				//0度到180度旋转
				ObjectAnimator animator = ObjectAnimator.ofFloat(mIvArrow, "rotation", 0,180);
				animator.start();
			}
		}
		//修改状态值
		isOpen = !isOpen;
	}//toggle();

	/**
	 *
	 * @des 实现折叠和展开效果
	 * @param start
	 * @param end
	 *
	 */
	public void doAnimation(int start ,int end)
	{
		ValueAnimator animator = ValueAnimator.ofInt(start,end);
		animator.setDuration(300);   //设置动画的时间
		animator.start();

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator value) {
				int height = (Integer) value.getAnimatedValue();
				//通过LayoutParams修改高度
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = height;
				mContainerDes.setLayoutParams(params);
			}
		});
	}

}//End
