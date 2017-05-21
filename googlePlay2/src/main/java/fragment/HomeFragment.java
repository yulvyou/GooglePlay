package fragment;

import holder.AppItemHolder;
import holder.PictureHolder;

import java.util.List;

import manager.DownloadManager;

import protocol.HomeProtocol;
import utils.UIUtils;
import adpter.AppItemAdapter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import base.BaseFragment;
import base.LoadingPager.LoadedResult;
import bean.AppInfoBean;
import bean.HomeBean;
import factory.ListViewFactory;

public class HomeFragment extends BaseFragment {
	//listView的数据源（APP的基本的信息）
	private List<AppInfoBean> mDatas;
	//轮播图
	private List<String> mPictures;

	private HomeProtocol	mHomeProtocol;
	private HomeAdapter	mAdapter;

	@Override
	public LoadedResult initData() {

		mHomeProtocol = new HomeProtocol();
		//加载数据
		try {
			HomeBean homeBean = mHomeProtocol.loadData(0);
			LoadedResult state = checkState(homeBean);
			if(state != LoadedResult.SUCCESS)//如果不成功，就直接返回，走到这说明homeBean是OK的
			{
				return state;
			}

			state = checkState(homeBean.list);
			if(state != LoadedResult.SUCCESS)//如果不成功，就直接返回，走到这说明homeBean.list是OK的
			{
				return state;
			}

			//赋值
			mDatas = homeBean.list;
			mPictures = homeBean.picture;

			return LoadedResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			//出现异常返回错误
			return LoadedResult.ERROR;
		}

	}//initData


	@Override
	public View initSuccessView() {
		// 返回成功的视图
		//创建一个listView,并做一些常规的配置
		ListView listView = ListViewFactory.createListView();
		//创建一个PictureHolder
		PictureHolder pictureHolder = new PictureHolder();
		//触发加载数据
		pictureHolder.setDataAndRefreshHolderView(mPictures);

		View headView = pictureHolder.getHolderView();
		listView.addHeaderView(headView);

		//设置Adapter

		mAdapter = new HomeAdapter(listView,mDatas);
		listView.setAdapter(mAdapter);
		return listView;
	}//initSuccessView


	/**HomeAdapter*/
	public class HomeAdapter extends AppItemAdapter
	{
		public HomeAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
			super(absListView, dataSource);
		}


		@Override
		public List<AppInfoBean> onLoadMore() throws Exception {
			// 真正加载更多的数据
			/*===========协议简单封装之后=============*/
			//加载数据
			HomeBean homeBean = mHomeProtocol.loadData(mDatas.size());
			if(homeBean == null)
			{
				return null;
			}
			if(homeBean.list == null || homeBean.list.size() == 0)
			{
				return null;
			}
			return homeBean.list;
		}//onLoadMore

//		/**
//		 *@des 点击普通条目对应的事件处理
//		 */
//		@Override
//		public void onNormalItemClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			// TODO Auto-generated method stub
//			Toast.makeText(UIUtils.getContext(), mDatas.get(position).name, Toast.LENGTH_SHORT).show();
//		}
//
	}//class.HomeAdapter


	@Override
	public void onPause() {
		//移除监听
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
		//重新添加监听
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

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		if(convertView==null)
//		{
//			//创建Holder
//			holder = new ViewHolder();
//			//初始化根布局
//			convertView = View.inflate(UIUtils.getContext(), R.layout.item_tmp, null);
//			//绑定Tag
//			convertView.setTag(holder);
//			//初始化孩子对象
//			holder.mTvTmp1 = (TextView) convertView.findViewById(R.id.tmp_tv_1);
//			holder.mTvTmp2 = (TextView) convertView.findViewById(R.id.tmp_tv_2);
//		}
//		else
//		{
//			//直接从convertView里面取得holder
//			holder = (ViewHolder) convertView.getTag();
//		}
//		//得到数据
//		String data = mDatas.get(position);
//		//刷新显示数据
//		holder.mTvTmp1.setText("front"+data);
//		holder.mTvTmp2.setText("rear"+data);
//
//
//		//返回根布局
//		return convertView;
//	}//getView
//
//
//	class ViewHolder
//	{
//		TextView mTvTmp1;
//		TextView mTvTmp2;
//	}//class.ViewHolder
//
//
//	}//class.HomeAdapter


