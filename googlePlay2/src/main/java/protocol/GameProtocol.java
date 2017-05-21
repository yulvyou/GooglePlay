package protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import base.BaseProtocol;
import bean.AppInfoBean;

public class GameProtocol extends BaseProtocol<List<AppInfoBean>> {
	@Override
	public String getInterfaceKey() {
		return "game";
	}

	@Override
	public List<AppInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		/*============泛型解析==============*/
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>(){
		}.getType());
	}
}//End
