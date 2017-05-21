package base;

import utils.UIUtils;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.googleplay2.R;

import factory.ThreadPoolFactory;

public abstract class LoadingPager extends FrameLayout {
	//页面状态所代表的常量值
	public static final int STATE_NONE = -1;  //默认状态
	public static final int STATE_LOADING = 0; //正在请求网络
	public static final int STATE_EMPTY = 1;
	public static final int STATE_ERROR = 2;
	public static final int STATE_SUCCESS = 3;

	//定义当前页面状态(默认设置为加载状态） ，loadData（）触发加载的时候会对mCurState进行修改
	public int mCurState = STATE_NONE;

	private View mLoadingView;
	private View mErrorView;
	private View mEmptyView;
	private View mSuccessView;

	public LoadingPager(Context context) {
		super(context);
		//初始化常见的View
		//① 加载页面
		// ② 错误页面
		// ③ 空页面
		initCommonView();
	}
	/**
	 * @des 初始化常规视图（加载页面，错误页面，空页面）
	 * @call LoadingPager 初始化的时候调用
	 */
	private void initCommonView()
	{
		mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
		//添加到FrameLayout中
		this.addView(mLoadingView);

		mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
		//如果加载失败则点击重新加载按钮，重新触发数据加载
		mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重新触发加载
				loadData();
			}
		});
		//添加到FrameLayout中
		this.addView(mErrorView);

		mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
		//添加到FrameLayout中
		this.addView(mEmptyView);

		//根据当前状态显示不同的view
		refreshUI();

	}//initCommonView

	/**
	 *@des 根据当前状态显示不同的view
	 *@call LoadingPager初始化和加载数据完成的时候调用
	 */
	public void refreshUI()
	{
//		if(mCurState==STATE_LOADING)
//		{
//			//显示loading视图
//			mLoadingView.setVisibility(View.VISIBLE);
//		}else{
//			//隐藏loading视图
//			mLoadingView.setVisibility(View.GONE);
//		}

		//mCurState: loadData（）触发加载的时候会对mCurState进行修改
		//控制Loaging 视图
		mLoadingView.setVisibility((mCurState==STATE_LOADING)||(mCurState==STATE_NONE)?View.VISIBLE:View.GONE);
		//控制 empty视图
		mEmptyView.setVisibility((mCurState==STATE_EMPTY)?View.VISIBLE:View.GONE);
		//控制 error视图
		mErrorView.setVisibility((mCurState==STATE_ERROR)?View.VISIBLE:View.GONE);

		//控制success视图
		if(mSuccessView ==null && mCurState==STATE_SUCCESS)
		{
			//创建成功视图
			mSuccessView = initSuccessView();
			//添加到FrameLayout中
			this.addView(mSuccessView);
		}
		if(mSuccessView !=null)
		{
			//控制success视图
			mSuccessView.setVisibility((mCurState==STATE_SUCCESS)?View.VISIBLE:View.GONE);
		}

	}//refreshUI

	//数据加载流程
	/**
	 ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
	 ② 异步加载数据  -->显示加载视图
	 ③ 处理加载结果
	 ① 成功-->显示成功视图
	 ② 失败
	 ① 数据为空-->显示空视图
	 ② 数据加载失败-->显示加载失败的视图
	 */

	/**
	 * @des 触发加载数据
	 * @call 暴露给外界调用，其实就是外界触发加载数据
	 * @call 在MainActivity，DetailActivity中有调用，
	 */
	public void loadData()//该方法最终主要实现是交个initData（）实现，initData又交个具体对象实现
	{
		//如果加载成功，那就无需再次加载
		if(mCurState!=STATE_SUCCESS&&mCurState!=STATE_LOADING)
		{
			//如果没有正在加载并且没有加载成功，那么就将状态设置为正在加载状态然后刷新界面

			//保证每次执行的时候一定是加载中视图，而不是上次的mCurState
			int state = STATE_LOADING;
			mCurState = state;
			refreshUI();
			//② 异步加载数据。开启一个线程
			//new Thread(new LoadDataTask()).start();
			//从线程池工厂得到线程池，并且执行任务
			ThreadPoolFactory.getNormalPool().execute(new LoadDataTask());
		}

	}//loadData

	class LoadDataTask implements Runnable
	{
		@Override
		public void run()
		{
			//② 异步加载数据（initData方法最终移交给具体的LoadingPager对象实现该方法）
			LoadedResult tempState = initData();
			//处理加载结果
			mCurState = tempState.getState();
			//安全的刷新UI
			UIUtils.postTaskSafely(new Runnable(){

				@Override
				public void run() {
					//刷新页面
					refreshUI();
				}

			});
		}
	}//LoadDataTask.Class


	/**
	 * @des 真正实现异步加载数据，必须实现，但是不知道具体实现，所以定义成为抽象方法，让其子类具体实现
	 * @call loadData()方法被调用的时候
	 */
	public abstract LoadedResult initData();

	/**
	 * @des 返回成功视图
	 * @call refreshUI中有调用
	 * @call 正在加载数据完成之后，并且数据加载成功，我们必须告知具体的成功视图
	 *
	 */
	public abstract View initSuccessView() ;

	/**枚举出加载数据的状态结果*/
	public enum LoadedResult
	{
		SUCCESS(STATE_SUCCESS),ERROR(STATE_ERROR),EMPTY(STATE_EMPTY);
		int state;

		public int getState() {
			return state;
		}
		
		private LoadedResult(int state)
		{
			this.state = state;
		}
	}
}//End
