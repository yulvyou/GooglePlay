package holder;

import utils.UIUtils;
import android.view.View;
import android.widget.LinearLayout;
import base.BaseHolder;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoadMoreHolder extends BaseHolder<Integer> {

	@ViewInject(R.id.item_loadmore_container_loading)
	LinearLayout mContainerLoading;
	
	@ViewInject(R.id.item_loadmore_container_retry)
	LinearLayout mContainerRetry;
	
	//加载中的状态值
	public static final int	STATE_LOADING	= 0;
	//加载失败重试的状态值
	public static final int	STATE_RETRY		= 1;
	//加载为空的状态值
	public static final int	STATE_NONE		= 2;

	@Override
	public View initHolderView() {
		// TODO
		View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);

		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void refreshHolderView(Integer state) {
		// 将加载更多和加载失败重试的视图隐藏
		mContainerLoading.setVisibility(View.GONE);
		mContainerRetry.setVisibility(View.GONE);
		switch (state) {
			case STATE_LOADING://正在加载
				mContainerLoading.setVisibility(View.VISIBLE);
				break;
			case STATE_RETRY://加载失败重试
				mContainerRetry.setVisibility(View.VISIBLE);
				break;
			case STATE_NONE://加载的结果为空则不变
				break;
			
			default:
				break;
		}
		
	}//refreshHolderView

}//End
