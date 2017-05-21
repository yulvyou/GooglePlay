package protocol;

import android.util.Log;
import base.BaseProtocol;
import base.LoadingPager.LoadedResult;
import bean.HomeBean;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import conf.Constants.URLS;

public class HomeProtocol extends BaseProtocol<HomeBean>{
	/**获得网络请求数据的类别*/
	@Override
	public String getInterfaceKey() {
		return "home";
	}

	/**解析Json*/
	@Override
	public HomeBean parseJson(String result) {
		//gson
		Gson gson = new Gson();
		HomeBean homeBean = gson.fromJson(result, HomeBean.class);
		return homeBean;
	}

//	public HomeBean loadData(int index)throws Exception
//	{
//		//发送网络请求
//		HttpUtils httpUtils = new HttpUtils();
//		//http://localhost:8080/GooglePlayServer/home?index=0
//		String url = URLS.BASEURL+"home";
//		RequestParams params = new RequestParams();
//		params.addQueryStringParameter("index", index+"");
//		ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url ,params);
//		//获得从服务器获取到的数据
//		String result = responseStream.readString();
//		
//		//gson
//		Gson gson = new Gson();
//		HomeBean homeBean = gson.fromJson(result, HomeBean.class);
//			
//		return homeBean;
//	}
	
}//end
