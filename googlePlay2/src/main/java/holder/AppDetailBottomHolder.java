package holder;

import java.io.File;

import manager.DownLoadInfo;
import manager.DownloadManager;
import manager.DownloadManager.DownLoadObserver;
import utils.CommonUtils;
import utils.UIUtils;
import views.ProgressButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import base.BaseHolder;
import bean.AppInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements OnClickListener ,DownLoadObserver {
	
	@ViewInject(R.id.app_detail_download_btn_share)
	Button mBtnShare;
	
	@ViewInject(R.id.app_detail_download_btn_favo)
	Button mBtnFavo;
	
	@ViewInject(R.id.app_detail_download_btn_download)
	ProgressButton mProgressButton;

	private AppInfoBean	mData;
	
	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_bottom, null);
		ViewUtils.inject(this,view);
		
		mBtnShare.setOnClickListener(this);
		mBtnFavo.setOnClickListener(this);
		mProgressButton.setOnClickListener(this);
		
		return view;
	}

	/**
	 * DetailActivity.java中调用mAppDetailBottomHolder.setDataAndRefreshHolderView(mData);
	 * setDataAndRefreshHolder方法中会调用refreshHolderView方法
	 */
	@Override
	public void refreshHolderView(AppInfoBean data) {
		mData = data;
		/*======根据不同的下载状态给用户提示=========*/
		DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
		//刷新下载按钮的UI
		refreshProgressBtnUI(info);

	}//refreshHolderView

	/**刷新下载按钮的UI*/
	public void refreshProgressBtnUI(DownLoadInfo info){
		/**
		 状态(编程记录)  	|  给用户的提示(ui展现)
		 ----------------|----------------------
		 未下载			|下载
		 下载中			|显示进度条
		 暂停下载			|继续下载
		 等待下载			|等待中...
		 下载失败 			|重试
		 下载完成 			|安装
		 已安装 			|打开
		 */
		//设置下载按钮的背景为正常状态下的背景
		mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);

		switch (info.state) {
			case DownloadManager.STATE_UNDOWNLOAD: //未下载
				mProgressButton.setText("下载");
				break;

			case DownloadManager.STATE_DOWNLOADING://下载中
				//修改下载按钮的背景为下载中的背景
				mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
				mProgressButton.setProgressEnable(true);
				mProgressButton.setMax(info.max);
				mProgressButton.setCurProgress(info.curProgress);
				int progress = (int) (info.curProgress *100.f / info.max +.5f)	;
				mProgressButton.setText(progress + "%");
				break;

			case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
				mProgressButton.setText("继续下载");
				break;

			case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
				mProgressButton.setText("等待中...");
				break;

			case DownloadManager.STATE_DOWNLOADFAILED://下载失败
				mProgressButton.setText("重试");
				break;


			case DownloadManager.STATE_DOWNLOADED://下载完成
				mProgressButton.setProgressEnable(false);  //取消进度条
				mProgressButton.setText("安装");
				break;

			case DownloadManager.STATE_INSTALLED://已安装
				mProgressButton.setText("打开");
				break;

			default:
				break;
		}//switch
	}//refreshProgressBtnUI

	/**
	 * 几个按钮的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.app_detail_download_btn_download:
			/*======根据不同的下载状态给用户提示=========*/
				DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);
				/**
				 状态(编程记录)      | 用户行为(触发操作)
				 ----------------| -----------------
				 未下载			| 去下载
				 下载中			| 暂停下载
				 暂停下载			| 断点继续下载
				 等待下载			| 取消下载
				 下载失败 			| 重试下载
				 下载完成 			| 安装应用
				 已安装 			| 打开应用
				 */
				switch (info.state) {
					case DownloadManager.STATE_UNDOWNLOAD: //未下载
						doDownLoad(info);
						break;

					case DownloadManager.STATE_DOWNLOADING://下载中
						pauseDownLoad(info);
						break;

					case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
						doDownLoad(info);
						break;

					case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
						cancelDownLoad(info);
						break;

					case DownloadManager.STATE_DOWNLOADFAILED://下载失败
						doDownLoad(info);
						break;

					case DownloadManager.STATE_DOWNLOADED://下载完成
						installApk(info);
						break;

					case DownloadManager.STATE_INSTALLED://已安装
						openApk(info);
						break;


					default:
						break;
				}//switch

				break;


			default:
				break;
		}

	}//onClick

	/**执行下载任务*/
	private void doDownLoad(DownLoadInfo info) {
			/*================根据不同的状态触发不同的操作=============*/
		//		//下载apk放置的目录
		//		String dir = FileUtils.getDir("download");  //  sdcard/android/data/包名/download
		//		File file = new File(dir, mData.packageName +".apk");// sdcard/android/data/包名/come.*****.apk
		//		//保存路径
		//		String savePath = file.getAbsolutePath();   //// sdcard/android/data/包名/come.*****.apk
		//
		//		DownLoadInfo info = new DownLoadInfo();
		//		info.downloadUrl = mData.downloadUrl;
		//		info.savePath = savePath;
		//		info.packageName = mData.packageName;

		DownloadManager.getInstance().downLoad(info);
	}

	/**打开应用*/
	private void openApk(DownLoadInfo info) {
		CommonUtils.openApp(UIUtils.getContext(), info.packageName);
	}

	/**安装应用*/
	private void installApk(DownLoadInfo info) {
		File apkFile = new File(info.savePath);
		CommonUtils.installApp(UIUtils.getContext(), apkFile);
	}

	/**取消下载*/
	private void cancelDownLoad(DownLoadInfo info) {
		//交给DownloadManager处理
		DownloadManager.getInstance().cancel(info);
	}

	/**暂停下载*/
	private void pauseDownLoad(DownLoadInfo info) {
		//交给DownloadManager处理
		DownloadManager.getInstance().pause(info);
	}

	/*=========收到数据改变，更新ui===============*/
	@Override
	public void onDownLoadInfoChange(final DownLoadInfo info) {
		//过滤DownLoadInfo,使对应的APP只对对应是自己的DownLoadInfo信息进行处理
		if(!info.packageName.equals(mData.packageName)){
			return;
		}

		//刷新下载按钮的UI
		UIUtils.postTaskSafely(new Runnable() {
			@Override
			public void run() {
				refreshProgressBtnUI(info);
			}
		});
	}


	/**添加观察者和手动刷新*/      //DetailActivity中有调用
	public void addObserverAndRefresh(){
		DownloadManager.getInstance().addObserver(this);
		//手动刷新
		DownLoadInfo downLoadInfo = DownloadManager.getInstance().getDownLoadInfo(mData);
//		DownloadManager.getInstance().notifyObservers(downLoadInfo);//方式1
		refreshProgressBtnUI(downLoadInfo);//方式2
	}
	
	
}//End
