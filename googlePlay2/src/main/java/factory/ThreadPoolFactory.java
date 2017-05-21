package factory;

import manager.ThreadPoolProxy;

/**
 *
 * @des 得到线程池
 *
 */
public class ThreadPoolFactory {
	//定义一个普通的线程池
	static ThreadPoolProxy mNormalPool;
	//定义一个下载的线程池
	static ThreadPoolProxy mDownLoadPool;

	/**得到一个普通的线程池*/
	public static ThreadPoolProxy getNormalPool()
	{
		if(mNormalPool==null)
		{
			synchronized (ThreadPoolProxy.class)
			{
				if(mNormalPool==null)
				{
					mNormalPool = new ThreadPoolProxy(5, 5, 3000);
				}
			}//synchronized
		}//if
		return mNormalPool;
	}//getNormalPool


	/**得到一个下载的的线程池*/
	public static ThreadPoolProxy getDownLoadlPool()
	{
		if(mDownLoadPool==null)
		{
			synchronized (ThreadPoolProxy.class)
			{
				if(mDownLoadPool==null)
				{
					mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
				}
			}//synchronized
		}//if
		return mDownLoadPool;
	}//getNormalPool
}//End
