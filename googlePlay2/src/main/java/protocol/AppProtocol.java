package protocol;

import java.util.List;

import android.util.Log;
import base.BaseProtocol;
import bean.AppInfoBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class AppProtocol extends BaseProtocol<List<AppInfoBean>> {

	@Override
	public String getInterfaceKey() {
		return "app";
	}

	@Override
	public List<AppInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		
		/*============泛型解析==============*/
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>(){
		}.getType());
	}

}//End
