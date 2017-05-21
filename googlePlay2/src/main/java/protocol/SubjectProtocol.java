package protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import base.BaseProtocol;
import bean.SubjectInfoBean;

public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>> {

	@Override
	public String getInterfaceKey() {
		return "subject";
	}

	@Override
	public List<SubjectInfoBean> parseJson(String jsonString) {
		//解析json
		Gson gson = new Gson();
		return gson.fromJson(jsonString,
				new TypeToken<List<SubjectInfoBean>>(){}.getType());
		
	}//parseJson


}//End
