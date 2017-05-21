package base;

import java.util.LinkedList;
import java.util.List;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity{
	//用来存储打开了的activity
	private List<Activity> activities = new LinkedList<Activity>();

//	private long mPreTime;

	private Activity mCurActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();			// 初始化activity，比如得到由其他Activity传过来的数据
		initView(); 	// 初始化界面,必须实现
		initActionBar();// 初始化ActionBar
		initData();		// 初始化数据（在初始化数据方法中，加载完数据后有一个刷新界面的操作）
		initListener();	// 初始化监听事件，比如Tabs的监听等等

		activities.add(this);

	}//onCreate


	@Override
	protected void onResume() {
		mCurActivity = this; //最上层的一个的activity
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}


	/**得到最上层的activity*/
	public Activity getCurActivity(){
		return mCurActivity;
	}

//	/**连续点击两次返回按钮退出程序*/
//	@Override
//	public void onBackPressed() {
//		if(this instanceof MainActivity)//只用于MainActivity情况下
//		{
//			if(System.currentTimeMillis() - mPreTime > 2000){//间隔大于2s
//				Toast.makeText(getApplicationContext(), "再按一次，退出程序", Toast.LENGTH_SHORT).show();
//				mPreTime = System.currentTimeMillis();
//				return;
//			}
//		}
//		super.onBackPressed();//等价于finish（）
//	}



	public void init() {

	}
	/**必须实现*/
	public abstract void initView();

	public void initActionBar() {
	}

	public void initData() {
	}

	public void initListener() {
	}

	/**
	 * 完全退出
	 */
	public void exit()
	{
		for(Activity activity : activities)
		{
			activity.finish();
		}
	}//exit
	
	
}//End
