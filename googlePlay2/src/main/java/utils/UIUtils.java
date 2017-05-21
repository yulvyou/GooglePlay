package utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import base.BaseApplication;

/**
 *
 * @author Administrator
 * 和UI相关的一些工具类
 */
public class UIUtils {
	/**得到上下文*/
	public static Context getContext()
	{
		return BaseApplication.getContext();
	}//getContext

	/**得到Resouce对象*/
	public static Resources getResources()
	{
		return getContext().getResources();
	}

	/**得到String.xml中的字符串*/
	public static String getString(int resId, Object... formatArgs)
	{
		return getResources().getString(resId,formatArgs);
	}

	/**得到String.xml中的字符串，并且带占位符*/
	public static String getString(int resId)
	{
		return getResources().getString(resId);
	}


	/**得到String.xml中的字符串数组*/
	public static String[] getStringArr(int resId)
	{
		return getResources().getStringArray(resId);
	}

	/**得到colors.xml中的颜色*/
	public static int getColor(int colorId)
	{
		return getResources().getColor(colorId);
	}

	/**得到应用程序的包名*/
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**得到主线程ID*/
	public static long getMainThreadId()
	{
		return BaseApplication.getMainThreadId();
	}

	/**得到主线程Handler*/
	public static Handler getMainThreadHandler()
	{
		return BaseApplication.getHandler();
	}


	/**安全的执行一个任务*/
	public static void postTaskSafely(Runnable task)
	{
		//获得当前线程的ID号
		int curThreadId = android.os.Process.myTid();
		//如果当前线程是主线程
		if(curThreadId == getMainThreadId())
		{
			//直接运行task
			task.run();
		}else
		{
			//如果当前线程不是主线程
			getMainThreadHandler().post(task);
		}
	}//postTaskSafely

	/**延迟执行任务*/
	public static void postTaskDelay(Runnable task , int delayMillis)
	{
		getMainThreadHandler().postDelayed(task, delayMillis);
	}//postTaskDelay

	/**移除任务*/
	public static void removeRask(Runnable task)
	{
		getMainThreadHandler().removeCallbacks(task);
	}

	/**
	 * dip转换为px
	 */
	public static int dipToPx(int dip)
	{
		//密度比  density = px/dip
		//density = dpi/160

		//320x480 density=1  1px=1dp
		//1280x720 density=1  2px=1dp
		float density = getResources().getDisplayMetrics().density;
		int px = (int) (dip*density + .5f);
		return px;
	}//dipToPx

	/**
	 * px转换为dip
	 */
	public static int pxTodip(int px)
	{
		//密度比  density = px/dip
		
		float density = getResources().getDisplayMetrics().density;
		int dip = (int) (px/density + .5f);
		return dip;
	}//dipToPx
}//End
