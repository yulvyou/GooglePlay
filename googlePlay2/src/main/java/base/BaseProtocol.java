package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import utils.FileUtils;
import utils.IOUtils;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import conf.Constants;
import conf.Constants.URLS;

public abstract class BaseProtocol<T> {
	/**
	 * 加载数据
	 */
	public T loadData(int index)throws Exception
	{
		/*=============读取本地缓存===============*/
		T localData = getDataFromLocal(index);
		if(localData != null)
		{
			//Log.i("test", "读取了缓存文件---》"+getCacheFile(index).getAbsolutePath());
			return localData;
		}

		/*=============发送网络请求===============*/
		String jsonString = getDataFromNet(index);

		/*=============json解析===============*/
		//gson
//		Gson gson = new Gson();
		T bean = parseJson(jsonString);
		return bean;
	}

	/**从本地缓存中读取数据*/
	private T getDataFromLocal(int index) {

		File cacheFile = getCacheFile(index);
		if(cacheFile.exists())//如果缓存存在
		{
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				//读取插入时间
				String timeTimeMillis = reader.readLine();
				if(System.currentTimeMillis()- Long.parseLong(timeTimeMillis)<Constants.PROTOCOLTIME)
				{
					//读取缓存内容
					String jsonsString = reader.readLine();
					//Json解析内容
					return parseJson(jsonsString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				IOUtils.close(reader);
			}
		}//if

		return null;
	}//getDataFromLocal

	/**获得本地缓存文件*/
	public File getCacheFile(int index)
	{
		//得到目录看是否存在缓存文件
		String dir = FileUtils.getDir("json");// sdcard/Android/data/包名/json
		//子目录   interfacekey + "." +index;
		//如果是详情页 interfacekey + "." +包名
		Map<String , String> extraParmas = getExtraParmas();
		String name = "";
		if(extraParmas == null)//子目录
		{
			name = getInterfaceKey() + "." +index; //interfacekey + "." +index
		}else{//详情页
			for(Map.Entry<String, String> info : extraParmas.entrySet()){
				String key = info.getKey();     //如"packageName"
				String value = info.getValue(); //如“com.it.www
				name = getInterfaceKey() + "." +value;//interfacekey + "." +包名
			}
		}//else

		File cacheFile = new File(dir , name);
		return cacheFile;
	}


	/**
	 * @des 返回额外的参数
	 * @call 默认在基类里面返回是null，但是如果子类需要返回额外的参数的时候，覆写该方法
	 */
	public Map<String , String> getExtraParmas()
	{
		return null;
	}


	/** 从网络中获取数据 */
	public String getDataFromNet(int index) throws Exception
	{
		HttpUtils httpUtils = new HttpUtils();
		//http://localhost:8080/GooglePlayServer/home?index=0
		String url = URLS.BASEURL + getInterfaceKey();
		RequestParams params = new RequestParams();

		//localhost:8080/GoodPlayServer/detail?packageName=com.****
		Map<String ,String> extraParmas = getExtraParmas();
		if(extraParmas==null)
		{
			params.addQueryStringParameter("index", index+"");
		}else{//子类覆写了getExtraParmas方法，返回了额外的参数
			//取出额外的参数
			for(Map.Entry<String, String> info : extraParmas.entrySet()){
				String name = info.getKey();//参数的key
				String value = info.getValue();//参数的value

				params.addQueryStringParameter(name, value);
			}
		}//else

		ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url ,params);
		//TODO 打印网络请求的地址
//		Log.i("test", responseStream.getRequestUrl());

		//获得从服务器获取到的数据
		String jsonString = responseStream.readString();

		/*==========保存网络数据到本地=============*/
		File cacheFile = getCacheFile(index);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(cacheFile));
			//第一行插入时间
			writer.write(System.currentTimeMillis()+"");
			//换行
			writer.write("\r\n");
			//写入json
			writer.write(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(writer);
		}
		return jsonString;
	}

	/**获得网络数据的类型别类*/
	public abstract String  getInterfaceKey();

	/**解析json*/
	public abstract T parseJson(String jsonString);
	
}//End
