package fragment;

import holder.AppItemHolder;

import java.util.List;

import manager.DownloadManager;

import factory.ListViewFactory;
import fragment.AppFragment.AppAdapter;

import protocol.GameProtocol;
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
import base.MyBaseAdapter;
import base.LoadingPager.LoadedResult;
import bean.AppInfoBean;

public class GameFragment extends BaseFragment {

	private GameProtocol	mProtocol;
	private List<AppInfoBean>	mDatas;

	GameAdapter mAdapter;
	
	@Override
	public LoadedResult initData() {
		// 执行耗时操作
		//创建协议
		mProtocol = new GameProtocol();
		try {
			//加载数据
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
		mAdapter = new GameAdapter(listView,mDatas);
		listView.setAdapter(mAdapter);

		return listView;
	}//initSuccessView

	/**适配器*/
	class GameAdapter extends AppItemAdapter
	{

		public GameAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
			super(absListView, dataSource);
		}

		/**加载更多*/
		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			return mProtocol.loadData(mDatas.size());
		}
	}//Class.GameAdapter


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