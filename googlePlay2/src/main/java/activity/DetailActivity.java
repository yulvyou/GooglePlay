package activity;

import manager.DownloadManager;
import holder.AppDetailBottomHolder;
import holder.AppDetailDesHolder;
import holder.AppDetailInfoHolder;
import holder.AppDetailPicHolder;
import holder.AppDetailSafeHolder;
import protocol.DetailProtocol;
import utils.UIUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import base.BaseActivity;
import base.LoadingPager;
import base.LoadingPager.LoadedResult;
import bean.AppInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DetailActivity extends BaseActivity {

	private String mPackageName;
	private AppInfoBean	mData;
	
	@ViewInject(R.id.app_detail_bottom)
	FrameLayout mContainerBottom;
	
	@ViewInject(R.id.app_detail_des)
	FrameLayout mContainerDes;
	
	@ViewInject(R.id.app_detail_info)
	FrameLayout mContainerInfo;
	
	@ViewInject(R.id.app_detail_pic)
	FrameLayout mContainerPic;
	
	@ViewInject(R.id.app_detail_safe)
	FrameLayout mContainerSafe;
	
	private LoadingPager	mLoadingPager;
	private AppDetailBottomHolder	mAppDetailBottomHolder;//onPause 和
	//onResume 会用到所以在这里定义

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//初始化activity
//		init();
//		initActionBar();
//		LoadingPager loadingPager = new LoadingPager(UIUtils.getContext()) {
//
//			@Override
//			public LoadedResult initData() {
//				//加载数据
//				return onInitData();
//			}
//
//			@Override
//			public View initSuccessView() {
//				//初始化成功页面
//				return onLoadSuccessView();
//			}
//		};
//
//		//触发数据加载
//		loadingPager.loadData();
//
//		setContentView(loadingPager);
//
//	}//onCreate


	/**
	 * 初始化activity，得到由其他Activity传过来的数据
	 */
	@Override
	public void init()
	{
		mPackageName = getIntent().getStringExtra("packageName");
	}

	@Override
	public void initView() {
		mLoadingPager = new LoadingPager(UIUtils.getContext()) {

			@Override
			public LoadedResult initData() {
				//加载数据
				return onInitData();   //移交给onInitData完成，最终由onInitData
				//中的DetailProtocol完成数据加载
			}

			@Override
			public View initSuccessView() {
				//初始化成功页面
				return onLoadSuccessView();
			}
		};

		setContentView(mLoadingPager);
	}


	/**
	 * 初始化ActionBar
	 */
	@Override
	public void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("GooglePlay");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public void initData() {
		//触发数据加载
		mLoadingPager.loadData();  //这个方法在加载完数据后还会刷新界面的
//		onInitData();
//		mLoadingPager.refreshUI();
		super.initData();
	}


	/**
	 * 真正加载数据
	 */
	public LoadedResult onInitData()
	{
		//发起网络请求
		DetailProtocol protocol = new DetailProtocol(mPackageName);

		try {
			mData = protocol.loadData(0);
			if(mData == null)
			{
				return LoadedResult.ERROR;
			}
			return LoadedResult.SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}

	}//onInitData


	/**
	 * 加载成功界面
	 */
	public View onLoadSuccessView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.activity_detail, null);

		ViewUtils.inject(this,view);

		/*=========================填充内容==========================*/
		//1、信息部分
		AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder();
		mContainerInfo.addView(appDetailInfoHolder.getHolderView());
		//设置数据并刷新HolderView
		appDetailInfoHolder.setDataAndRefreshHolderView(mData);

		//2、安全部分
		AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder();
		mContainerSafe.addView(appDetailSafeHolder.getHolderView());
		appDetailSafeHolder.setDataAndRefreshHolderView(mData);

		//3、截图部分
		AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder();
		mContainerPic.addView(appDetailPicHolder.getHolderView());
		appDetailPicHolder.setDataAndRefreshHolderView(mData);

		//4、描述部分
		AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder();
		mContainerDes.addView(appDetailDesHolder.getHolderView());
		appDetailDesHolder.setDataAndRefreshHolderView(mData);

		//5、下载部分

		mAppDetailBottomHolder = new AppDetailBottomHolder();
		mContainerBottom.addView(mAppDetailBottomHolder.getHolderView());
		mAppDetailBottomHolder.setDataAndRefreshHolderView(mData);

		//将appDetailBottomHolder添加到DownloadManager的downLoadObservers中去
		DownloadManager.getInstance().addObserver(mAppDetailBottomHolder);

		return view;
	}

	@Override
	protected void onPause() {
		//界面不可见的时候移除观察者
		if(mAppDetailBottomHolder != null){
			DownloadManager.getInstance().deleteObserver(mAppDetailBottomHolder);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		//界面恢复可见时添加观察者
		if(mAppDetailBottomHolder != null){
			//开启监听的时候，手动的去获取一下最新的状态
			mAppDetailBottomHolder.addObserverAndRefresh();
		}
		super.onResume();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:     //点击返回
				finish();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}//End
