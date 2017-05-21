package bean;

import java.util.List;

public class AppInfoBean {
	
	public long		id;				// 应用的id
	public String	name;			// 应用的名字
	public String	packageName;	// 应用的包名
	public String	iconUrl;		// 应用的图标地址
	public float	stars;			// 应用的评分
	public long		size;			// 应用的长度
	public String	downloadUrl;	// 应用的下载地址
	public String	des;			// 应用的描述

	/*==========应用详情中的一些字段===========*/
	public String					author;		  	// 应用作者
	public String					date;			// 应用更新时间
	public String					downloadNum;	// 应用下载量
	public String					version;		// 应用版本号

	public List<AppInfoSafeBean>	safe;			// 应用安全部分
	public List<String>				screen;			// 应用的截图


	public class AppInfoSafeBean{
		public String	safeDes;		// 安全描述
		public int		safeDesColor;	// 安全描述部分的文字颜色
		public String	safeDesUrl;		// 安全描述图标的URL
		public String	safeUrl;		// 安全图标的URL
		
	}//AppInfoSafeBean


	@Override
	public String toString() {
		return "AppInfoBean [id=" + id + ", name=" + name + ", packageName="
				+ packageName + ", iconUrl=" + iconUrl + ", stars=" + stars
				+ ", size=" + size + ", downloadUrl=" + downloadUrl + ", des="
				+ des + ", author=" + author + ", date=" + date
				+ ", downloadNum=" + downloadNum + ", version=" + version
				+ ", safe=" + safe + ", screen=" + screen + "]";
	}
	
}//End
