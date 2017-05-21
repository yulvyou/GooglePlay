package base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
/**
 * 一个全局的“盒子”，里面放置的对象，属性，方法都是全局可以调用的
 */
public class BaseApplication extends Application {

	private static Context mContext;
	private static Thread mMainThread;
	private static long mMainThreadId;
	private static Looper mMainLooper;
	private static Handler mHandler;


	/*
	 * 程序的入口
	 */
	@Override
	public void onCreate() {
		//上下文
		mContext = getApplicationContext();
		//主线程
		mMainThread = Thread.currentThread();
		//主线程ID
		mMainThreadId = android.os.Process.myTid();
		//主线程Looper对象
		mMainLooper = getMainLooper();
		//定义一个Handler
		mHandler = new Handler();
		
		super.onCreate();
	}//onCreate
	

	
	public static Context getContext() {
		return mContext;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static long getMainThreadId() {
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper() {
		return mMainLooper;
	}

	public static Handler getHandler() {
		return mHandler;
	}
	
}//End
