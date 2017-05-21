package holder;

import java.io.File;

import manager.DownLoadInfo;
import manager.DownloadManager;
import manager.DownloadManager.DownLoadObserver;
import utils.BitmapHelper;
import utils.CommonUtils;
import utils.StringUtils;
import utils.UIUtils;
import views.CircleProgressView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseHolder;
import bean.AppInfoBean;

import com.example.googleplay2.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import conf.Constants.URLS;

public class AppItemHolder extends BaseHolder<AppInfoBean> implements OnClickListener,DownLoadObserver{
	
	//这里相当于findViewById（）；
	@ViewInject(R.id.item_appinfo_iv_icon)
	ImageView	mIvIcon;

	@ViewInject(R.id.item_appinfo_rb_stars)
	RatingBar	mRbstars;

	@ViewInject(R.id.item_appinfo_tv_des)
	TextView	mTvDes;

	@ViewInject(R.id.item_appinfo_tv_size)
	TextView	mTvsize;

	@ViewInject(R.id.item_appinfo_tv_title)
	TextView	mTvTitle;

	@ViewInject(R.id.item_appinfo_circleprogressview)
	CircleProgressView	mCircleProgressView;

	private AppInfoBean	mData;

	@Override
	public View initHolderView() {

		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
		//注入
		ViewUtils.inject(this,view);

		mCircleProgressView.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {

		//清除复用convertView之后的circleProgress效果
		mCircleProgressView.setCurProgress(0);

		mData = data;
		//设置描述，大小，title
		mTvDes.setText(data.des);
		mTvsize.setText(StringUtils.formatFileSize(data.size));
		mTvTitle.setText(data.name);

		//设置图片
		mIvIcon.setImageResource(R.drawable.ic_default);//默认图片
		String uri = URLS.IMAGEBASEURL+data.iconUrl;
		BitmapHelper.display(mIvIcon,uri);

		//设置评分星星
		mRbstars.setRating(data.stars);

		/*======根据不同的下载状态给用户提示=========*/
		DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
		//刷新下载按钮的UI
		refreshCircleProgressViewUI(info);

	}


	/**刷新圆圈进度条  （APPItemHolder的refreshHolderView将用到）*/
	private void refreshCircleProgressViewUI(DownLoadInfo info) {
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

		switch (info.state) {
			case DownloadManager.STATE_UNDOWNLOAD: //未下载
				mCircleProgressView.setNote("下载");
				mCircleProgressView.setIcon(R.drawable.ic_download);
				break;

			case DownloadManager.STATE_DOWNLOADING://下载中
				//修改下载按钮的背景为下载中的背景
				mCircleProgressView.setProgressEnable(true);
				mCircleProgressView.setMax(info.max);
				mCircleProgressView.setCurProgress(info.curProgress);
				int progress = (int) (info.curProgress *100.f / info.max +.5f)	;
				mCircleProgressView.setNote(progress + "%");
				mCircleProgressView.setIcon(R.drawable.ic_pause);
				break;

			case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
				mCircleProgressView.setNote("继续下载");
				mCircleProgressView.setIcon(R.drawable.ic_resume);
				break;

			case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
				mCircleProgressView.setNote("等待中...");
				mCircleProgressView.setIcon(R.drawable.ic_pause);
				break;

			case DownloadManager.STATE_DOWNLOADFAILED://下载失败
				mCircleProgressView.setNote("重试");
				mCircleProgressView.setIcon(R.drawable.ic_redownload);
				break;


			case DownloadManager.STATE_DOWNLOADED://下载完成
				mCircleProgressView.setProgressEnable(false);  //取消进度条
				mCircleProgressView.setNote("安装");
				mCircleProgressView.setIcon(R.drawable.ic_install);
				break;

			case DownloadManager.STATE_INSTALLED://已安装
				mCircleProgressView.setNote("打开");
				mCircleProgressView.setIcon(R.drawable.ic_install);
				break;

			default:
				break;
		}//switch
	}

	/**点击事件*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.item_appinfo_circleprogressview:
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
		}//switch
	}//onclick

	/**执行下载任务*/
	private void doDownLoad(DownLoadInfo info) {
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
	/**注意：初始化的时候把AppItemHolder加到观察者集合里面*/
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
				refreshCircleProgressViewUI(info);
			}
		});
	}//onDownLoadInfoChange

}//End



////定义根布局
//	public View mHolderView;
//	//做holder需要持有孩子对象
//	TextView mTvTmp1;
//	TextView mTvTmp2;
//
//	private String mData;
//
//	public HomeHolder()
//	{
//		//初始化根布局和孩子对象
//		mHolderView = initHolderView();
//		//绑定Tag（绑定自身，因为自身有布局的孩子对象）
//		mHolderView.setTag(this);
//	}
//
//	/**初始化根布局和孩子对象*/
//	@Override
//	public View initHolderView()
//	{
//		//初始化根布局
//		View view = View.inflate(UIUtils.getContext(), R.layout.item_tmp, null);
//		//初始化孩子对象
//		mTvTmp1 = (TextView) view.findViewById(R.id.tmp_tv_1);
//		mTvTmp2 = (TextView) view.findViewById(R.id.tmp_tv_2);
//		return view;
//	}
//
//
//	/**刷新显示*/
//	@Override
//	public void refreshHolderView(String data) 
//	{
//		this.mTvTmp1.setText("front"+data);
//		this.mTvTmp2.setText("rear"+data);
//	}
//	