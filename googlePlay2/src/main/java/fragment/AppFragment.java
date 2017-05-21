package fragment;

import factory.ListViewFactory;
import fragment.GameFragment.GameAdapter;
import holder.AppItemHolder;

import java.util.List;

import manager.DownloadManager;

import protocol.AppProtocol;
import utils.UIUtils;
import adpter.AppItemAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import base.BaseFragment;
import base.BaseHolder;
import base.LoadingPager.LoadedResult;
import base.MyBaseAdapter;
import bean.AppInfoBean;

public class AppFragment extends BaseFragment {

	private List<AppInfoBean>	mDatas;
	private AppProtocol	mProtocol;

	AppAdapter mAdapter;
	
	@Override
	public LoadedResult initData() {
		mProtocol = new AppProtocol();
		
		try {
			mDatas = mProtocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}//initData

	
	
	@Override
	public View initSuccessView() {
		// 返回成功的视图
		//创建一个listView,并做一些常规的配置
		ListView listView = ListViewFactory.createListView();

		//添加Adapter
		mAdapter = new AppAdapter(listView,mDatas);
		listView.setAdapter(mAdapter);

		return listView;
	}//initSuccessView

	/**适配器*/
	class AppAdapter extends AppItemAdapter
	{

		public AppAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
			super(absListView, dataSource);
		}

		/**加载更多*/
		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			return mProtocol.loadData(mDatas.size());
		}
	}//Class.AppAdapter


	@Override
	public void onPause() {
		//重新添加监听
		if(mAdapter != null){
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for(AppItemHolder appItemHolder : appItemHolders){
				DownloadManager.getInstance().deleteObserver(appItemHolder);
			}
		}

		//手动刷新（重新获取状态和刷新界面）

		super.onPause();
	}


	@Override
	public void onResume() {
		//移除监听
		if(mAdapter != null){
			List<AppItemHolder> appItemHolders = mAdapter.getAppItemHolders();
			for(AppItemHolder appItemHolder : appItemHolders){
				DownloadManager.getInstance().addObserver(appItemHolder);
			}
			//手动刷新（重新获取状态和刷新界面）
			mAdapter.notifyDataSetChanged();  //这里Adapter会自动刷新界面，不需要写刷新界面的方法了
		}
		super.onResume();
	}
}//End