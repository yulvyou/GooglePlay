package factory;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import base.BaseFragment;
import fragment.AppFragment;
import fragment.CategoryFragment;
import fragment.GameFragment;
import fragment.HomeFragment;
import fragment.HotFragment;
import fragment.RecommendFragment;
import fragment.SubjectFragment;

/**
 * Fragment的工厂类(即：得到fragment）
 *
 * @author Administrator
 *
 */
public class FragmentFactory {

	public static final int FRAGMENT_HOME = 0;
	public static final int FRAGMENT_APP = 1;
	public static final int FRAGMENT_GAME = 2;
	public static final int FRAGMENT_SUBJECT = 3;
	public static final int FRAGMENT_RECOMMEND = 4;
	public static final int FRAGMENT_CATEGORY = 5;
	public static final int FRAGMENT_HOT = 6;

	//定义用于缓存Fragment的hashMap
//	Map<Integer, Fragment> cachesFragmentMap = new HashMap<Integer, Fragment>();

	//使用SparseArrayCompat定义一个定义用于缓存Fragment的cachesFragmentMap
	static SparseArrayCompat<BaseFragment> cachesFragmentMap = new SparseArrayCompat<BaseFragment>();

	/** 得到fragment */
	public static BaseFragment getFragment(int position)
	{
		BaseFragment fragment = null;

		//如果缓存中有有响应的Fragment则不再重新创建
		BaseFragment tmpFragment = cachesFragmentMap.get(position);
		if(tmpFragment!=null)
		{
			fragment = tmpFragment;
			return fragment;
		}

//		//如果缓存中有有响应的Fragment则不再重新创建
//		if(cachesFragmentMap.containsKey(position))
//		{
//			fragment = cachesFragmentMap.get(position);
//		}
		switch (position) {
			case FRAGMENT_HOME://主页
				fragment = new HomeFragment();

//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_APP://应用
				fragment = new AppFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_GAME://游戏
				fragment = new GameFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_SUBJECT://主题
				fragment = new SubjectFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_RECOMMEND://推荐
				fragment = new RecommendFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_CATEGORY://分类
				fragment = new CategoryFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			case FRAGMENT_HOT://排行
				fragment = new HotFragment();
//			Log.i("test", "创建了"+position+"号Fragment");
				break;
			default:
				break;
		}
		//将对应的Fragment的保存到cachesFragmentMap中
		cachesFragmentMap.put(position, fragment);
		
		return fragment;
	}//getFragment()
}// End
