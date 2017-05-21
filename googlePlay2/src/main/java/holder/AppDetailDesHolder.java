package holder;

import utils.UIUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;
import base.BaseHolder;
import bean.AppInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.ObjectAnimator;

public class AppDetailDesHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
	@ViewInject(R.id.app_detail_des_tv_author)
	TextView mTvAuthor;
	
	@ViewInject(R.id.app_detail_des_iv_arrow)
	ImageView mIvArrow;
	
	@ViewInject(R.id.app_detail_des_tv_des)
	TextView mTvDes;
	
	//判断是否折叠或展开
	private boolean isOpen = true;

	//描述部分的高度
	private int mTvDesMeasuredHeight;

	private AppInfoBean	mData;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_des, null);
		//注入
		ViewUtils.inject(this,view);
		view.setOnClickListener(this);


		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		mData = data;

		mTvAuthor.setText(data.author);
		mTvDes.setText(data.des);

		//这里主要是用来得到描述部分的高度，之所以用这个方法是因为容器的 measure方法只能得到默认行（布局文件中定义的行数）的高度
		//设置默认状态为折叠状态
		mTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mTvDesMeasuredHeight = mTvDes.getMeasuredHeight();

				//默认折叠
				toggle(false);
				//如果不移除，一会高度变成7行的时候，mTvDesMeasureHeight就会变
				mTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}

		});

	}//refreshHolderView

	/*
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		toggle(true);
	}//onClick

	private void toggle(boolean needAnimation)
	{
		if(isOpen){//折叠
			/*
			 * mTvDes高度发生变化
			 * 应有的高度-->7行的高度
			 */
			int start = mTvDesMeasuredHeight;
			int end = getShortHeight(7,mData);//设置为7行的高度，和设置内容

			if(needAnimation){
				//动画实现折叠
				ObjectAnimator.ofInt(mTvDes, "height", start,end).start();
			}else{
				mTvDes.setHeight(end);
			}


		}else{//展开
			int start = getShortHeight(7,mData);//设置为7行的高度，和设置内容
			int end = mTvDesMeasuredHeight;

			if(needAnimation){
				//动画实现折叠
				ObjectAnimator.ofInt(mTvDes, "height", start, end).start();
			}else{
				mTvDes.setHeight(end);
			}
		}//else
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
		//改变折叠或展开的状态
		isOpen = !isOpen;
	}//toggle

	/**
	 *设置短的高度 ，和设置内容
	 * @param i  设置的 行
	 * @param data 内容
	 * @return
	 */
	private int getShortHeight(int i, AppInfoBean data) {
		TextView tempTextView = new TextView(UIUtils.getContext());//临时的textView
		tempTextView.setLines(7);//设置textView的行数
		tempTextView.setText(data.des);//设置内容

		//测绘
		tempTextView.measure(0, 0);
		
		int measuredHeight = tempTextView.getMeasuredHeight();
		
		return measuredHeight;
	}//getShortHeight
	
	
}//End
