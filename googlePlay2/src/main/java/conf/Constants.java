package conf;

import utils.LogUtils;

/**
 *
 * 常量文件
 *
 */
public class Constants {

	public static final int	DEBUGLEVEL	= LogUtils.LEVEL_ALL;	// LEVEL_ALL：显示所有的日志
	// LEVEL_OFF：关闭所有的日志
	//分页的条数
	public static final int		PAGESIZE		= 20;
	//缓存的过期时间
	public static final int	PROTOCOLTIME	= 5 * 60 * 1000;			// 5分钟

	public static final class URLS {
		public static final String	BASEURL	= "http://192.168.28.1:8080/GooglePlayServer/";
		//服务器URL
//		public static final String	BASEURL	= "http://172.16.123.6:8080/GooglePlayServer/";
		//图片URL
		public static final String  IMAGEBASEURL = BASEURL+"image?name=";
		//下载URL
		public static final String DOWNLOADBASEURL = BASEURL +"download";
		
		
	}

}// End
