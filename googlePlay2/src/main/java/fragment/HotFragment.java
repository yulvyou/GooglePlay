package fragment;

import java.util.List;
import java.util.Random;

import protocol.HotProtocol;
import utils.UIUtils;
import views.FlowLayout;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import base.BaseFragment;
import base.LoadingPager.LoadedResult;

public class HotFragment extends BaseFragment {

	private List<String>	mDatas;

	@Override
	public LoadedResult initData() {
		// 执行加载数据操作
		HotProtocol protocol = new HotProtocol();

		try {
			mDatas = protocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		// 返回成功的视图
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		//创建流式布局(View包下自定义的 布局)
		FlowLayout fl = new FlowLayout(UIUtils.getContext());

		for(String data : mDatas)
		{
			TextView tv = new TextView(UIUtils.getContext());
			tv.setText(data);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(16);
			int padding = UIUtils.dipToPx(5);
			tv.setPadding(padding, padding, padding, padding);
			tv.setGravity(Gravity.CENTER);

			//tv.setBackgroundResource（R.drawable.shape_hot_fl_tv);

			/*==========normalDrawable begin==========*/
			GradientDrawable normalDrawable = new GradientDrawable();

			//得到随机颜色
			Random random = new Random();
			int alpha = 255;
			int green = random.nextInt(190) + 30; //30-220
			int red = random.nextInt(190) + 30; //30-220
			int blue = random.nextInt(190) + 30; //30-220
			int argb = Color.argb(alpha, red, green, blue);

			//设置填充颜色
			normalDrawable.setColor(argb);

			//设置圆角半径
			normalDrawable.setCornerRadius(UIUtils.dipToPx(6));
			/*==========normalDrawable end==========*/


			/*==========pressedDrawable begin==========*/
			GradientDrawable pressedDrawable = new GradientDrawable();
			pressedDrawable.setColor(Color.RED);
			pressedDrawable.setCornerRadius(UIUtils.dipToPx(6));


			/*==========pressedDrawable end==========*/

			//设置一个状态图片，用代码设置selector
			//注意：！！！！！设置状态图的顺序不能反
			StateListDrawable stateListDrawable = new StateListDrawable();
			//设置按钮按下时的状态图
			stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
			//设置按钮没有按下去的状态图
			stateListDrawable.addState(new int[]{}, normalDrawable);




			tv.setBackgroundDrawable(stateListDrawable);

			tv.setClickable(true);
			//往流式布局中添加孩子
			fl.addView(tv);
		}
		
		scrollView.addView(fl);
		
		return scrollView;
	}
//	@Override
//	@Nullable
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		
//		TextView tv = new TextView(UIUtils.getContext());
//		tv.setText(this.getClass().getSimpleName());
//		
//		
//		return tv;
//	}//onCreateView
	
	
}//End