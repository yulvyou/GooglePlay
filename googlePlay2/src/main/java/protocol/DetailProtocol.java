package protocol;

import java.util.HashMap;
import java.util.Map;

import base.BaseProtocol;
import bean.AppInfoBean;

import com.google.gson.Gson;

public class DetailProtocol extends BaseProtocol<AppInfoBean> {

	private String mPackageName;
	

	public DetailProtocol(String packageName) {
		super();
		mPackageName = packageName;
	}

	
	@Override
	public String getInterfaceKey() {
		// TODO Auto-generated method stub
		return "detail";
	}

	
	@Override
	public AppInfoBean parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, AppInfoBean.class);
	}
	
	
	@Override
	public Map<String, String> getExtraParmas() {
		Map<String ,String> extraParmas = new HashMap<String , String>();
		extraParmas.put("packageName", mPackageName);
		return extraParmas;
	}
	
	
}//End
