package base;

import factory.ThreadPoolFactory;
import holder.LoadMoreHolder;

import java.util.ArrayList;
import java.util.List;

import utils.UIUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import conf.Constants;

public abstract class MyBaseAdapter<T> extends BaseAdapter implements OnItemClickListener {

	public List<T> mDataSource = new ArrayList<T>();
	//定义一些跟加载更多有关的常量(这两个常量的值必须相邻)
	public static final int	VIEWTYPE_LOADMORE	= 0;
	public static final int	VIEWTYPE_NORMAL		= 1;
	private LoadMoreHolder	mLoadMoreHolder;
	private LoadMoreTask	mLoadMoreTask;
	private AbsListView	mAbsListView;

	public MyBaseAdapter(AbsListView absListView ,List<T> dataSource) {
		super();
		absListView.setOnItemClickListener(this);
		mDataSource = dataSource;
		mAbsListView = absListView;
	}

	@Override
	public int getCount() {
		if(mDataSource!=null)
		{
			return mDataSource.size()+1;//+1这个单位是为了留个加载更多用的
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mDataSource!=null)
		{
			return mDataSource.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*==============listView里面可以显示几种viewType begin===============*/
	@Override
	public int getViewTypeCount() {
		// TODO
		return super.getViewTypeCount()+1;//2
	}

	@Override
	public int getItemViewType(int position) {
		// 如果滑到底部，那么对应的viewType是加载更多
		if(position == getCount()-1)//滑到底部
		{
			return VIEWTYPE_LOADMORE;  //getView中会用到
		}
		return getNomalViewType(position);
	}

	/**
	 * @des 返回普通View的viewType
	 * @call 子类其实可以复写该方法，添加更多的viewType
	 */
	public int getNomalViewType(int position)
	{
		return VIEWTYPE_NORMAL;  //getView中会用到
	}

	/*==============listView里面可以显示几种viewType end===============*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		BaseHolder<T> holder = null;
		//初始化布局决定根布局
		//convertView，这个就代表着可以复用的view对象，当然这个对象也可能为空，
		//当它为空的时候，表示该条目view第一次创建
		if(convertView==null)
		{
			if(getItemViewType(position)==VIEWTYPE_LOADMORE)//如果是加载更多的类型
			{												//（List的最后一个）
				//创建一个加载更多的holder
				holder = (BaseHolder<T>) getLoadMoreHolder();
				//Log.i("test", "创建了一个加载更多的Holder");
			}
			else{
				//创建Holder
				holder = getSpecialHolder(position);
				//Log.i("test", "创建了一个SpecialHolder");
			}
		}
		else
		{
			//直接从convertView里面取得holder
			holder = (BaseHolder<T>) convertView.getTag();
		}

		/*===============数据展示================*/
		if(getItemViewType(position)==VIEWTYPE_LOADMORE)//如果是加载更多的类型
		{
			if(hasLoadMore())//如果有加载更多
			{
				//去开始执行加载更多
				performLoadMore();
			}else{
				mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.STATE_NONE);
			}
		}
		else//普通情况
		{
			//得到数据和刷新显示数据
			holder.setDataAndRefreshHolderView(mDataSource.get(position));
		}
		//返回根布局
		return holder.mHolderView;
	}//getView

	/**得到加载更多的Holder*/
	private LoadMoreHolder getLoadMoreHolder() {
		if(mLoadMoreHolder==null)//如果mLoadMoreHolder为空则重新创建一个
		{
			mLoadMoreHolder = new LoadMoreHolder();
		}
		return mLoadMoreHolder;
	}

	/**
	 * @des 滑到底之后应该去拉取更多的数据
	 * @call 滑到低的时候
	 */
	private void performLoadMore() {
		if(mLoadMoreTask==null)//如果mLoadMoreTask为空就执行
		{
			//修改loadMore当前的视图为加载中
			int state = LoadMoreHolder.STATE_LOADING;
			mLoadMoreHolder.setDataAndRefreshHolderView(state);
			//利用线程池加载数据
			mLoadMoreTask = new LoadMoreTask();
			ThreadPoolFactory.getNormalPool().execute(mLoadMoreTask);
		}//if
	}

	/**
	 * @des 决定有没有加载更多，默认是返回true ，但是子类可以覆写此方法，如果子类返回的是false ，就不会去加载更多
	 * @call getView中滑动的时候会调用
	 * @return
	 */
	private boolean hasLoadMore() {
		return true;
	}


	class LoadMoreTask implements Runnable
	{
		@Override
		public void run() {
			List<T> loadMoreDatas = null;

			/*=================根据加载更多的数据，处理loadmore的状态 begin=============*/
			int state = LoadMoreHolder.STATE_LOADING;
			try {
				//真正开始请求网络加载数据
				loadMoreDatas = onLoadMore();
				//得到返回数据，处理结果
				if(loadMoreDatas==null)//如果没有数据
				{
					state = LoadMoreHolder.STATE_NONE;
				}else{
					if(loadMoreDatas.size()<Constants.PAGESIZE)//如果得到的数据小于每次规定加载的最大数时
					{										   //说明没有加载更多
						state = LoadMoreHolder.STATE_NONE;
					}else
					{
						state = LoadMoreHolder.STATE_LOADING;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				//加载数据出现异常则重试
				state = LoadMoreHolder.STATE_RETRY;
			}

			//定义一个中转的临时变量，因为在安全刷新loadMore和listview视图的时候参数需要用final类型的
			final int tempState = state;
			final List<T> tempLoadMoreDatas = loadMoreDatas;
			/*=================根据加载更多的数据，处理loadmore的状态 End==============*/

			//安全的刷新loadmore和listview视图
			UIUtils.postTaskSafely(new Runnable() {
				@Override
				public void run() {
					//刷新loadmore视图
					mLoadMoreHolder.setDataAndRefreshHolderView(tempState);
					//刷新listview视图  ，返回加载更多后得到的数据加到原数据源中mDatas.addAll()
					if(tempLoadMoreDatas!=null)
					{
						//如果加载更多得到的数据不为空这追加到原数据源中
						mDataSource.addAll(tempLoadMoreDatas);
						//刷新listview
						notifyDataSetChanged();
					}
				}//run
			});
			//数据加载完了之后将任务置空
			mLoadMoreTask = null;
		}//run

	}//class.LoadMoreTask

	/**
	 * @des 真正加载更多的方法，可有可无，定义成public方法，如果子类有加载更多，再去覆写该方法
	 * @call 滑到底的时候
	 */
	public  List<T> onLoadMore()throws Exception
	{
		return null;
	};


	/**
	 * @des 返回具体的BaseHolder的子类
	 * @call getView方法中如果没有convertView的时候被创建
	 */
	public abstract BaseHolder<T> getSpecialHolder(int position) ;


	/*==============处理item的点击事件=======================*/
	/**
	 * 处理item的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {

		if(mAbsListView instanceof ListView)//如果mAbsListView 是ListView类型的，position则需要
		{									//减去该ListView之前的其他元素。如首页的轮播图
			position = position - ((ListView)mAbsListView).getHeaderViewsCount();
		}

		if(getItemViewType(position)==VIEWTYPE_LOADMORE)
		{
			//重新加载更多
			performLoadMore();
		}else
		{
			onNormalItemClick(parent, view, position, id);
		}

	}//onItemClick

	/**
	 *@des 点击普通条目对应的事件处理
	 *@call 如果子类需要处理item的点击事件就直接覆写此方法
	 */
	public void onNormalItemClick(AdapterView<?> parent, View view, int position,
								  long id) {
		
	}//onNormalItemClick
	
	
}//End

