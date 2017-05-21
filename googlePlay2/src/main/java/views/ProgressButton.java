package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class ProgressButton extends Button {
	
	private boolean mProgressEnable;  //控制是否有progressbar

	private long mMax = 100;  //进度条最大值
	private long mCurProgress;  //进度条当前的值

	private Drawable mProgressDrawable;

	public ProgressButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ProgressButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**设置是否设置进度条*/
	public void setProgressEnable(boolean progressEnable) {
		mProgressEnable = progressEnable;
	}

	/**设置进度条的最大值*/
	public void setMax(long max) {
		mMax = max;
	}

	/**设置当前的进度条值  ， 并且进行重绘操作*/
	public void setCurProgress(long curProgress) {
		mCurProgress = curProgress;//修改当前进度条的值

		invalidate();  //重绘
	}

	/**设置progressButton的进度背景*/
	public void setProgressDrawable(Drawable progressDrawable) {
		mProgressDrawable = progressDrawable;
	}





	@Override
	protected void onDraw(Canvas canvas) {

		if(mProgressEnable)
		{
			Drawable drawable = new ColorDrawable(Color.BLUE);

			int left = 0;
			int top = 0;
			int right = (int) (mCurProgress * 1.0f /mMax *getMeasuredWidth() + .5f) ;
			int bottom = getBottom();
			drawable.setBounds(left, top, right, bottom);   //一定要写的，告知绘制的范围
			drawable.draw(canvas);
		}

		super.onDraw(canvas);//绘制文本，背景等
		
	}//onDraw
	
}//End
