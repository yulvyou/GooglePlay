package adpter;

import holder.AppItemHolder;

import java.util.LinkedList;
import java.util.List;

import manager.DownloadManager;
import utils.UIUtils;
import activity.DetailActivity;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import base.BaseHolder;
import base.MyBaseAdapter;
import bean.AppInfoBean;


public class AppItemAdapter extends MyBaseAdapter<AppInfoBean> {//AppInfoBean是适配器中具体装的数据类型

	private List<AppItemHolder> mAppItemHolders = new LinkedList<AppItemHolder>();


	public List<AppItemHolder> getAppItemHolders() {
		return mAppItemHolders;
	}

	public AppItemAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
		super(absListView, dataSource);
	}

	@Override
	public BaseHolder<AppInfoBean> getSpecialHolder(int position) {

		AppItemHolder appItemHolder = new AppItemHolder();

		mAppItemHolders.add(appItemHolder);//保存Adapter里面对应的Holder。主要用于circleProgressView的刷新

		//把APPItemHolder加到观察者集合里面
		DownloadManager.getInstance().addObserver(appItemHolder);

		return appItemHolder;
	}


	/**
	 *@des 点击普通条目对应的事件处理
	 */
	@Override
	public void onNormalItemClick(AdapterView<?> parent, View view,
								  int position, long id) {
		//跳转到详情页
		goToDetailActivity(mDataSource.get(position).packageName);
	}//onNormalItemClick


	/**
	 *@des 跳转到详情页
	 */
	public void goToDetailActivity(String packageName)
	{
		Intent intent = new Intent(UIUtils.getContext() ,DetailActivity.class);
		//因为UIUtils.getContext()中的上下文是getApplicationContext，
		//但这里需要的是activity的上下文，所以需要设置一下flags
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//将包名放进intent
		intent.putExtra("packageName", packageName);
		
		UIUtils.getContext().startActivity(intent);
		
	}//goToDetailActivity
	
	
	
}//End

