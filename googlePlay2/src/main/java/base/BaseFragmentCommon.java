package base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragmentCommon extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		init();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		return initView();
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		initData();
		initListener();
		
		super.onActivityCreated(savedInstanceState);
	}

	
	/**
	 * 初始化view
	 * 而且是必须实现，但是不知道具体实现，所以定义成抽象方法
	 * */
	public abstract View initView() ;


	/**主要拿到出入Fragment的一些参数*/
	public void init() {
		// TODO 
		
	}//initListener
	
	/***/
	public void initData() {
		// TODO 
		
	}//initData
	
	/***/
	public void initListener() {
		// TODO 
		
	}//initListener

}//End
