package com.example.googleplay2;

import holder.MenuHolder;
import utils.UIUtils;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseActivity;
import base.BaseFragment;
import base.LoadingPager;
import base.LoadingPager.LoadedResult;

import com.astuetz.PagerSlidingTabStripExtends;

import factory.FragmentFactory;

//import android.support.v4.app.ActionBarDrawerToggle;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity {

	private PagerSlidingTabStripExtends	mTabs;
	private ViewPager				mViewPager;
	private String[]				mMainTitles;
	private DrawerLayout			mDrawerLayout;
	private ActionBarDrawerToggle	mToggle;
	private FrameLayout	mMain_menu;
	
	private long mPreTime;

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		// 初始化view组件
//		initview();
//		// 初始化actionBar
//		initActionBar();
//		// 初始化ActionBar的开关
//		initActionBarToggle();
//		// 初始化Data
//		initData();
//		// TODO 初始化Tabs滑动的事件监听
//		initListener();
//	}

	/** 初始化View */
	@Override
	public void initView() {
		//添加布局
		setContentView(R.layout.activity_main);

		mMain_menu = (FrameLayout) findViewById(R.id.main_menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawlayout);

		mTabs = (PagerSlidingTabStripExtends) findViewById(R.id.main_tabs);
		mViewPager = (ViewPager) findViewById(R.id.main_viewpager);


	}//initView

	/** 初始化actionbar 和  初始化ActionBar的开关 */
	@Override
	public void initActionBar() {
		// 获得actionBar
		ActionBar actionBar = getActionBar();
		// 设置logo
		actionBar.setLogo(R.drawable.ic_launcher);
		// 设置标题
		actionBar.setTitle("GooglePlay");
		// 显示返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);

		// 初始化ActionBar的开关
		initActionBarToggle();
	}

	/** 初始化ActionBarToggle */
	private void initActionBarToggle() {
		mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_am, R.string.open, R.string.close);

		// 同步状态的方法
		mToggle.syncState();
		// 设置mDrawerLayout拖动的监听
		mDrawerLayout.setDrawerListener(mToggle);
	}// initActionBarToggle

	/** 初始化数据 */
	@Override
	public void initData() {
		// 获得数据源
		mMainTitles = UIUtils.getStringArr(R.array.main_title);
		// 一个PagerAdapter
		// MainPagerAdapter adapter = new MainPagerAdapter();
		// 一个FragmentAdapter -----（会缓存Fragment）
		// MainFragmentPagerAdapter adapter = new
		// MainFragmentPagerAdapter(getSupportFragmentManager());
		// 一个FragmentStatePagerAdapter -----（不会会缓存Fragment)
		MainFragmentStatePagerAdapter adapter = new MainFragmentStatePagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(adapter);// Adapter自定义的类
		// 将viewPager和mTabs绑定
		mTabs.setViewPager(mViewPager);

		/*=======向侧滑菜单中装数据mMain_menu========*/
		MenuHolder menuHolder = new MenuHolder();
		mMain_menu.addView(menuHolder.getHolderView());
		menuHolder.setDataAndRefreshHolderView(null);
	}

	/** TODO 初始化Tabs滑动的事件监听 */
	@Override
	public void initListener() {
		// TODO
		mTabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 完成触发加载
				BaseFragment fragment = FragmentFactory.getFragment(position);
				if (fragment != null) {
					// 触发加载数据
					fragment.getLoadingPager().loadData();
				}
			}// onPageSelected

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}// initListnener

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				// mToggle控制打开关闭drawLayout
				mToggle.onOptionsItemSelected(item);
				break;

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	/** 定义一个扩展的PagerAdapter */
	class MainPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mMainTitles != null) {
				return mMainTitles.length;
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TextView tv = new TextView(UIUtils.getContext());
			tv.setText(mMainTitles[position]);
			container.addView(tv);
			return tv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		/*
		 * 必须要重写此方法
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}
	}// Class HomeAdapter

	/** 定义一个扩展的FragmentPagerAdapter */
	class MainFragmentPagerAdapter extends FragmentPagerAdapter {

		public MainFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 创建fragment
			Fragment fragment = FragmentFactory.getFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mMainTitles != null)
				return mMainTitles.length;
			return 0;
		}

		/*
		 * 必须要重写此方法
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}
	}// MainFragmentPagerAdapter

	/** 定义一个扩展的FragmentStatePagerAdapter */
	class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

		public MainFragmentStatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 创建fragment
			Fragment fragment = FragmentFactory.getFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mMainTitles != null)
				return mMainTitles.length;
			return 0;
		}

		/*
		 * 必须要重写此方法
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mMainTitles[position];
		}
	}// MainFragmentStatePagerAdapter


	/**连续点击两次返回按钮退出程序*/
	@Override
	public void onBackPressed() {
		if(this instanceof MainActivity)//只用于MainActivity情况下
		{
			if(System.currentTimeMillis() - mPreTime > 2000){//间隔大于2s
				Toast.makeText(getApplicationContext(), "再按一次，退出程序", Toast.LENGTH_SHORT).show();
				mPreTime = System.currentTimeMillis();
				return;
			}
		}
		super.onBackPressed();//等价于finish（）
	}
	
}// End
