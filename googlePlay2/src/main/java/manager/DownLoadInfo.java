package manager;

public class DownLoadInfo {
	public String savePath;
	public String downloadUrl;
	public int state = DownloadManager.STATE_UNDOWNLOAD; //默认状态就是未下载
	public String packageName; //包名
	public long max;          //进度条的最大值
	public long curProgress; //当前的进度
	
	public Runnable task;
}//End
