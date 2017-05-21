package manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import utils.CommonUtils;
import utils.FileUtils;
import utils.IOUtils;
import utils.UIUtils;
import bean.AppInfoBean;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import conf.Constants.URLS;
import factory.ThreadPoolFactory;

/**
 * @des 下载管理器  ， 需要时刻记录当前的状态,并且暴露当前状态
 * @author Administrator
 *
 */
public class DownloadManager {
	public static DownloadManager instance;

	//一些下载相关的状态值
	public static final int			STATE_UNDOWNLOAD		= 0;	//未下载
	public static final int			STATE_DOWNLOADING		= 1;	//下载中
	public static final int			STATE_PAUSEDOWNLOAD		= 2;	//暂停下载
	public static final int			STATE_WAITINGDOWNLOAD	= 3;	//等待下载
	public static final int			STATE_DOWNLOADFAILED	= 4;	//下载失败
	public static final int			STATE_DOWNLOADED		= 5;	//下载完成
	public static final int			STATE_INSTALLED			= 6;    //已安装

	//记录正在下载的一些downLoadInfo
	public Map<String, DownLoadInfo>	mDownLoadInfoMaps = new HashMap<String, DownLoadInfo>();

	//Constructor
	private void DownloadManager(){

	}
	//实现单例
	public static DownloadManager getInstance(){
		if(instance == null){

			synchronized(DownloadManager.class){
				if(instance == null){
					instance = new DownloadManager();
				}
			}
		}
		return instance;
	}//getInstance


	/**
	 * @des 用户点击了下载按钮
	 * @param data
	 */
	public void downLoad(DownLoadInfo info) {
		//将需要传过来的downLoadIfo存进mDownLoadInfoMaps中
		mDownLoadInfoMaps.put(info.packageName, info);

		/**当前状态 ： 未下载*/
		info.state = STATE_UNDOWNLOAD;
		//通知观察者数据发生改变（主要是为了刷新UI）
		notifyObservers(info);

		/**当前状态 ： 等待下载
		 * 相当于默认状态，当下载的任务大于规定的任务数的时候，下载任务将自动的等待，所以这里修改
		 * 状态为等待下载
		 */
		info.state = STATE_WAITINGDOWNLOAD;
		//通知观察者数据发生改变
		notifyObservers(info);

		//得到线程池，执行任务
		DownloadTask task = new DownloadTask(info);
		info.task = task;//downInfo 身上的task赋值
		ThreadPoolFactory.getDownLoadlPool().execute(task);

	}//downLoad


	class DownloadTask implements Runnable{

		DownLoadInfo mInfo;

		public DownloadTask(DownLoadInfo info){
			super();
			this.mInfo = info;
		}

		//真正发起网络请求下载apk
		@Override
		public void run() {
			/**当前状态 ： 下载中*/
			mInfo.state = STATE_DOWNLOADING;
			//通知观察者数据发生改变
			notifyObservers(mInfo);

			try {
//				//下载apk放置的目录
//				String dir = FileUtils.getDir("download");  //  sdcard/android/data/包名/download
//				File file = new File(dir, data.packageName +".apk");// sdcard/android/data/包名/come.*****.apk
//				//保存路径
//				String savePath = file.getAbsolutePath();   //// sdcard/android/data/包名/come.*****.apk

				long initRange = 0;   //初始化下载的开始点
				File saveApk = new File(mInfo.savePath);
				if(saveApk.exists()){
					initRange = saveApk.length();  //未下载完成的apk的已经下载的长度
				}
				//修改已下载的值
				mInfo.curProgress = initRange;

				//相关参数
				String url = URLS.DOWNLOADBASEURL;
				HttpUtils httpUtils = new HttpUtils();

				RequestParams params = new RequestParams();
				params.addQueryStringParameter("name", mInfo.downloadUrl);
				params.addQueryStringParameter("range", initRange+"");
				ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);

				if (responseStream.getStatusCode() == 200) // 200表示请求成功
				{
					InputStream in = null;
					FileOutputStream out = null;
					boolean isPause = false;  //用于记录是否是暂停

					try {
						in = responseStream.getBaseStream();
						File saveFile = new File(mInfo.savePath);
						out = new FileOutputStream(saveFile,true);//If {@code append} is true and the file already exists,
						//it will be appended to

						byte[] buffer = new byte[1024];
						int len = -1;

						while ((len = in.read(buffer)) != -1) {
							if(mInfo.state == STATE_PAUSEDOWNLOAD){//如果状态被修改为暂停状态就跳出循环
								isPause = true;
								break;
							}

							out.write(buffer, 0, len);
							//修改当前的进度
							mInfo.curProgress +=len;

							/*====当前状态 ： 下载中=====*/
							mInfo.state = STATE_DOWNLOADING;
							//通知观察者数据发生改变
							notifyObservers(mInfo);
						}//while

						if(isPause){//用户暂停了下载
							/*====当前状态 ： 暂停下载=====*/
							mInfo.state = STATE_PAUSEDOWNLOAD;
							//通知观察者数据发生改变
							notifyObservers(mInfo);
						}else{//下载完成
							/*====当前状态 ： 下载完成=====*/
							mInfo.state = STATE_DOWNLOADED;
							//通知观察者数据发生改变
							notifyObservers(mInfo);
						}

					} finally {
						IOUtils.close(out);
						IOUtils.close(in);
					}
				} else {
					/**当前状态 ： 下载失败*/
					mInfo.state = STATE_DOWNLOADFAILED;
					//通知观察者数据发生改变
					notifyObservers(mInfo);
				}

			} catch (Exception e) {
				e.printStackTrace();
				/**当前状态 ： 下载失败*/
				mInfo.state = STATE_DOWNLOADFAILED;
				//通知观察者数据发生改变
				notifyObservers(mInfo);
			}
		}//run
	}//Class.DownLoadTask


	/**
	 * 根据AppinfoBean生成一个DownLoadInfo，进行一些常规的赋值
	 */
	public DownLoadInfo generateDownLoadInfo(AppInfoBean data)
	{
		//下载apk放置的目录
		String dir = FileUtils.getDir("download");  //  sdcard/android/data/包名/download
		File file = new File(dir, data.packageName +".apk");// sdcard/android/data/包名/come.*****.apk
		//保存路径
		String savePath = file.getAbsolutePath();   //// sdcard/android/data/包名/come.*****.apk

		//初始化一个downLoadInfo
		DownLoadInfo info = new DownLoadInfo();

		//相关赋值
		info.savePath = savePath;
		info.downloadUrl = data.downloadUrl;
		info.packageName = data.packageName;
		info.max = data.size;
		info.curProgress = 0;
		return info;
	}
	/**
	 * @des 暴露当前状态，也就是需要提供downLoadInfo
	 * @call 外界需要知道最新的state的时候
	 */
	public DownLoadInfo getDownLoadInfo(AppInfoBean data){

		//已安装
		if(CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)){
			//生成一个DownloadInfo类型的info并进行赋值
			DownLoadInfo info = generateDownLoadInfo(data);
			info.state = STATE_INSTALLED;//已安装
			return info;
		}

		//下载完成
		//生成一个DownloadInfo类型的info并进行赋值
		DownLoadInfo info = generateDownLoadInfo(data);
		File saveApk = new File(info.savePath);
		if(saveApk.exists()){//如果存在下载目录中
			if(saveApk.length() == data.size){//且大小与数据库中的大小相等
				info.state = STATE_DOWNLOADED; //下载完成
				return info;
			}
		}//if

		/**
		 下载中
		 暂停下载
		 等待下载
		 下载失败
		 */
		DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.packageName);
		if(downLoadInfo != null){  //如果不为空，说明在mDownLoadInfoMaps中有data.packageName相应的下载
			return downLoadInfo;	   //进而说明它是下载中，暂停下载，等待下载，下载失败 中的一种
		}

		//未下载
		DownLoadInfo tempInfo = generateDownLoadInfo(data);
		tempInfo.state = STATE_UNDOWNLOAD; //未下载
		return tempInfo;

	}//getDownLoadInfo

	/**暂停下载*/
	public void pause(DownLoadInfo info) {
		/*====当前状态 ： 暂停下载=====*/
		info.state = STATE_PAUSEDOWNLOAD;
		//通知观察者数据发生改变
		notifyObservers(info);
	}


	/**取消下载*/
	public void cancel(DownLoadInfo info) {
		Runnable task = info.task;
		//找到线程池，移除任务
		ThreadPoolFactory.getDownLoadlPool().removeTask(task);
		/*====当前状态 ： 暂停下载=====*/
		info.state = STATE_UNDOWNLOAD;
		//通知观察者数据发生改变
		notifyObservers(info);
	}


	/*=========自定义观察者设计模式===============*/
	public interface DownLoadObserver{
		void onDownLoadInfoChange(DownLoadInfo info);
	}

	List<DownLoadObserver> downLoadObservers = new LinkedList<DownLoadObserver>();

	/**添加观察者*/     //在DetailActivity中有用到
	public void addObserver(DownLoadObserver observer){
		if(observer == null){
			throw new NullPointerException("observer == null");
		}
		synchronized (this) {
			if(!downLoadObservers.contains(observer))
				downLoadObservers.add(observer);
		}
	}

	/**删除观察者*/
	public synchronized void deleteObserver(DownLoadObserver observer)
	{
		downLoadObservers.remove(observer);
	}

	/**通知观察者数据发生改变*/
	public void notifyObservers(DownLoadInfo info){
		for(DownLoadObserver observer : downLoadObservers){
			observer.onDownLoadInfoChange(info);
		}
	}
	

}//End
