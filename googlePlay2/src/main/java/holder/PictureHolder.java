package holder;

import java.util.List;

import utils.BitmapHelper;
import utils.UIUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import base.BaseHolder;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import conf.Constants.URLS;

public class PictureHolder extends BaseHolder<List<String>> {
	
	@ViewInject(R.id.item_home_picture_pager)
	ViewPager mViewPager;
	
	//小点
	@ViewInject(R.id.item_home_picture_container_indicator)
	LinearLayout mContainerIndicator;
	//图片名称数组
	private List<String>	mDatas;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
		//注入找孩子
		ViewUtils.inject(this,view);
		return view;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void refreshHolderView(List<String> datas) {
		mDatas = datas;
		mViewPager.setAdapter(new PictureAdapter());

		//添加小点
		for(int i = 0 ; i<datas.size(); i++)
		{
			View indicatorView = new View(UIUtils.getContext());
			indicatorView.setBackgroundResource(R.drawable.indicator_normal);
			//设置点的大小
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dipToPx(5),UIUtils.dipToPx(5));
			//调整点之间的间距
			//左边距
			params.leftMargin = UIUtils.dipToPx(5);
			//下边距
			params.bottomMargin = UIUtils.dipToPx(5);

			mContainerIndicator.addView(indicatorView,params);

			//默认选中效果
			if(i==0){
				indicatorView.setBackgroundResource(R.drawable.indicator_selected);
			}
		}//for
		//监听滑动事件
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				position = position%mDatas.size();//为了循环轮播

				for (int i = 0; i < mDatas.size(); i++) {
					//得到孩子
					View indicatorView = mContainerIndicator.getChildAt(i);
					//还原背景
					indicatorView.setBackgroundResource(R.drawable.indicator_normal);
					if(i==position)
					{
						indicatorView.setBackgroundResource(R.drawable.indicator_selected);
					}
				}//for

			}//onPageSelected

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
			}
		});

		//设置当前的Item为总数的一般count/2
		int diff = Integer.MAX_VALUE/2%mDatas.size();
		int index = Integer.MAX_VALUE/2;
		mViewPager.setCurrentItem(index - diff);

		//自动轮播
		final AutoScrollTask autoScrollTask = new AutoScrollTask();
		autoScrollTask.start();

		//用户触摸的时候移除自动轮播的任务
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						autoScrollTask.stop();
						break;
					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						autoScrollTask.start();
						break;

					default:
						break;
				}
				return false;
			}
		});

	}//refreshHolderView

	class PictureAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			if(mDatas != null)
			{
				return Integer.MAX_VALUE;//为了实现无限轮播修改成Integer.MAX_VALUE
				//return mDatas.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			position = position % mDatas.size();//为了循环轮播

			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ScaleType.FIT_XY);

			iv.setImageResource(R.drawable.ic_default);

			BitmapHelper.display(iv, URLS.IMAGEBASEURL+mDatas.get(position));

			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}//Class.PictureAdapter

	/**自动轮播任务*/
	class AutoScrollTask implements Runnable{
		/**开始轮播*/
		public void start()
		{
			UIUtils.postTaskDelay(this, 2000);
		}
		/**停止轮播*/
		public void stop()
		{
			UIUtils.removeRask(this);
		}

		@Override
		public void run() {
			int item = mViewPager.getCurrentItem();
			item++;
			mViewPager.setCurrentItem(item);
			//一轮任务执行完了之后再此开始任务，以实现无限轮播效果
			start();
		}//run 
		
	}//Class.AutoScrollTask
	
	
	
}//End
