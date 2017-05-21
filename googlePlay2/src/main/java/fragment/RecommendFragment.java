package fragment;

import java.util.List;
import java.util.Random;

import protocol.RecommendProtocol;
import utils.UIUtils;
import views.flyinout.ShakeListener;
import views.flyinout.ShakeListener.OnShakeListener;
import views.flyinout.StellarMap;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import base.BaseFragment;
import base.LoadingPager.LoadedResult;

public class RecommendFragment extends BaseFragment {

	private List<String>	mDatas;
	private ShakeListener	mShakeListener;

	@Override
	public LoadedResult initData() {
		// 执行耗时操作(真正加载数据)
		RecommendProtocol protocol = new RecommendProtocol();

		try {
			mDatas = protocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}//initData

	@Override
	public View initSuccessView() {
		// 返回成功的视图
		//StellarMap是一个自定义的开源代码
		final StellarMap stellarMap = new StellarMap(UIUtils.getContext());

		//设置adapter
		final RecommendAdapter adapter = new RecommendAdapter();
		stellarMap.setAdapter(adapter);

		//设置第一页的时候显示，若不设置，默认是不显示的
		stellarMap.setGroup(0, true);

		//设置把屏幕拆成多少个格子（这里将其设置为300个格子）
		stellarMap.setRegularity(15, 20);

		//加入摇一摇切换
		mShakeListener = new ShakeListener(UIUtils.getContext());
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			@Override
			public void onShake() {
				// 获得当前显示的组
				int groupIndex = stellarMap.getCurrentGroup();
				//摇一摇后加一
				groupIndex = (groupIndex+1)%adapter.getGroupCount();
				stellarMap.setGroup(groupIndex, true);
			}
		});

		return stellarMap;
	}

	@Override
	public void onResume() {
		if(mShakeListener != null)
		{
			mShakeListener.resume();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if(mShakeListener != null)
		{
			mShakeListener.pause();
		}
		super.onPause();
	}


	/**
	 *
	 * 适配器
	 */
	class RecommendAdapter implements StellarMap.Adapter
	{
		//定义每组的条数(每页显示多少条数据)
		private static final int	PAGER_SIZE	= 15;

		/**有多少组*/
		@Override
		public int getGroupCount() {
			int groupCount = mDatas.size()/PAGER_SIZE;
			//如果不能整除，还有余数的情况
			if(mDatas.size()%PAGER_SIZE>0)
			{
				groupCount++;
			}
			return groupCount;
		}

		/**每组多少条数据*/
		@Override
		public int getCount(int group) {

			if(group == getGroupCount()-1){//来到了最后一组
				//判断是否有余数
				if(mDatas.size()%PAGER_SIZE>0)
				{
					return mDatas.size()%PAGER_SIZE;
				}
			}//if

			return PAGER_SIZE;
		}

		/**返回具体的View
		 *
		 * group:代表第几组
		 * position:代表在这组中的第几个位置
		 *
		 */
		@Override
		public View getView(int group, int position, View convertView) {

			TextView tv = new TextView(UIUtils.getContext());

			int index = group*PAGER_SIZE + position;
			tv.setText(mDatas.get(index));

			//random对象
			Random random = new Random();
			//字体随机大小
			tv.setTextSize(random.nextInt(6)+10);//10-16
			//颜色随机
			int alpha = 255;
			int red = random.nextInt(180)+30; //20-210
			int green = random.nextInt(180)+30; //20-210
			int blue = random.nextInt(180)+30; //20-210
			int argb = Color.argb(alpha, red, green, blue);
			tv.setTextColor(argb);
			
			
			return tv;
		}
		
		/***/
		@Override
		public int getNextGroupOnPan(int group, float degree) {
			// TODO Auto-generated method stub
			return 0;
		}

		/***/
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}//class.RecommendAdapter
	
}//End