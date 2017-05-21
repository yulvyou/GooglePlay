package protocol;

import java.util.List;

import base.BaseProtocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HotProtocol extends BaseProtocol<List<String>> {

	@Override
	public String getInterfaceKey() {
		return "hot";
	}

	@Override
	public List<String> parseJson(String jsonString) {
		//解析Json
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<String>>(){}.getType());
	}

}
