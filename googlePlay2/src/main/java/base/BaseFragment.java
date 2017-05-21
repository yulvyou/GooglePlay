package base;

import java.util.List;
import java.util.Map;

import utils.UIUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import base.LoadingPager.LoadedResult;
//--------------------ps:这里Fragment引用的包是v4包下的Fragment----------------
public abstract class BaseFragment extends Fragment {

	private LoadingPager mLoadingPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if(mLoadingPager==null)
		{//第一次加载
			mLoadingPager = new LoadingPager(UIUtils.getContext()){

				@Override
				public LoadedResult initData() {
					// 移交给BaseFragment的子类处理
					return BaseFragment.this.initData();
				}

				@Override
				public View initSuccessView() {
					// 移交给BaseFragment的子类处理
					return BaseFragment.this.initSuccessView();
				}
			};
		}//if
//		else //TODO
//		{//第二次获得LoadingPager，先将原来的LoadingPager移除，然后从新return一个
//			Log.i("test", "第二次加载");
//			((ViewGroup) mLoadingPager.getParent()).removeView(mLoadingPager);
//		}
//		Log.i("test", "return");
		return mLoadingPager;
	}

	public LoadingPager getLoadingPager() {
		return mLoadingPager;
	}

	/**
	 * @des 真正实现异步加载数据，必须实现，但是不知道具体实现，所以定义成为抽象方法，让其子类具体实现
	 * @des 它是LoadingPager同名方法
	 * @call loadData()方法被调用的时候
	 */
	public abstract LoadedResult initData();

	/**
	 * @des 返回成功视图，必须实现，但是不知道具体实现，所以定义成为抽象方法，让其子类具体实现
	 * @des 它是LoadingPager同名方法
	 * @call 正在加载的数据完成后，并且数据加载成功，我们必须告知具体的成功视图
	 */
	public abstract View initSuccessView();


	/**
	 *
	 * @param obj 网络数据jSon化后的对象
	 * @return
	 */
	public LoadedResult checkState(Object obj)
	{
		if(obj == null)
		{
			return LoadedResult.EMPTY;
		}
		//如果数据是List类型的
		if(obj instanceof List)
		{
			if(((List) obj).size()==0)
			{
				return LoadedResult.EMPTY;
			}
		}
		//如果数据是map类型的
		if(obj instanceof Map)
		{
			if(((Map) obj).size()==0)
			{
				return LoadedResult.EMPTY;
			}
		}
		return LoadedResult.SUCCESS;
	}//checkState
	
}//End
