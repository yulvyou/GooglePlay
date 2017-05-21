package base;

import utils.UIUtils;
import android.view.View;
import android.widget.TextView;

import com.example.googleplay2.R;

public abstract class BaseHolder<T> {
	//定义根布局
	public View mHolderView;

	public View getHolderView() {
		return mHolderView;
	}


	private T mData;

	public BaseHolder()
	{
		//初始化根布局和孩子对象
		mHolderView = initHolderView();
		//绑定Tag（绑定自身，因为自身有布局的孩子对象）
		mHolderView.setTag(this);
	}

	/**
	 * @des 设置数据和刷新显示数据
	 * @call 需要加载数据和刷新数据的时候调用
	 */
	public void setDataAndRefreshHolderView(T data)
	{
		//得到数据(ps:这里mData用来保存数据用)
		mData = data;
		//刷新显示
		refreshHolderView(data);

	}//setDataAndRefreshHolderView


	/**初始化根布局和孩子对象*/
	public abstract View initHolderView();


	/**
	 * @des 刷新显示Holder视图
	 * @call setDataAndRefreshHolderView调用的时候就被调用
	 */
	public abstract void refreshHolderView(T data);


}//End
