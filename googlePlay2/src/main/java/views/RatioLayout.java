package views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.googleplay2.R;

/**
 *
 * 与图片宽高比相关的一个类
 *
 */
public class RatioLayout extends FrameLayout {
	private float				mPicRatio		= 0f;				// 图片的宽高比 2.43
	public static final int	RELATIVE_WIDTH	= 0;				// 控件宽度固定,已知图片的宽高比,求控件的高度
	public static final int	RELATIVE_HEIGHT	= 1;				// 控件高度固定,已知图片的宽高比,求控件的宽度
	private int					mRelative		= RELATIVE_WIDTH;

	public void setPicRatio(float picRatio) {
		mPicRatio = picRatio;
	}

	public void setRelative(int relative) {
		mRelative = relative;
	}

	public RatioLayout(Context context) {
		this(context, null);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

		mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0);

		mRelative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);

		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 控件宽度固定,已知图片的宽高比,求控件的高度
		int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);

		// 控件高度固定,已知图片的宽高比,求控件的宽度
		int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (parentWidthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_WIDTH) {// 控件宽度固定,已知图片的宽高比,求控件的高度
			// 得到父容器的宽度
			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
			// 得到孩子的宽度
			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
			// 控件的宽度/控件的高度 = mPicRatio;

			// 计算孩子的高度
			int childHeight = (int) (childWidth / mPicRatio + .5f);

			// 计算父容器的高度
			int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();

			// 主动测绘孩子.固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

			// 设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);

		} else if (parentHeightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_HEIGHT) {
			// 控件高度固定,已知图片的宽高比,求控件的宽度
			// 得到父亲的高度
			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

			// 得到孩子的高度
			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();

			// 控件的宽度/控件的高度 = mPicRatio;
			// 计算控件宽度
			int childWidth = (int) (childHeight * mPicRatio + .5f);

			// 得到父亲的宽度
			int parentWidth = childWidth + getPaddingRight() + getPaddingLeft();

			// 主动测绘孩子.固定孩子的大小
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

			// 设置自己的测试结果
			setMeasuredDimension(parentWidth, parentHeight);

		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		}

	}
}//End


//public class RatioLayout extends FrameLayout {
//
//	//图片的宽高比,2.43
//	private float mPicRatio = 0f; 		//图片的宽高比 2.43
//
//
//
//
//	public RatioLayout(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		//添加属性（这里用到的是自定义属性）
//		TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RatioLayout);
//
//		typedArray.getFloat(R.styleable.RatioLayout_picRatio, 0);
//
//		//类型数组回收
//		typedArray.recycle();
//	}
//
//	public RatioLayout(Context context) {
//		super(context,null);
//		// TODO Auto-generated constructor stub
//	}
//
//	/**测绘*/
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//		// 控件宽度固定，已知图片的宽高比，求控件的高度
//		int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
//		// 控件高度固定，已知图片的宽高比，求控件的宽度
//		int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//		if(parentWidthMode == MeasureSpec.EXACTLY)//控件宽度固定，已知图片的宽高比，求控件的高度 ,MeasureSpec.EXACTLY 是参数指定的模式
//		{
//			//得到父容器的宽度
//			int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//			//得到孩子的宽度（ImageView的宽度）
//			int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
//
//			//计算孩子的高度（控件的宽度/控件的高度 = mPicRatio)
//			int childHeight = (int) (childWidth/mPicRatio+ .5f);
//
//			//计算父容器的高度
//			int parentHeight = childHeight + getPaddingBottom() + getPaddingTop();
//
//			//主动测绘孩子
//			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
//			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
//			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
//
//			//设置自己的测试结果
//			setMeasuredDimension(parentWidth, parentHeight);
//
//		}else if(parentHeightMode == MeasureSpec.EXACTLY)// 控件高度固定，已知图片的宽高比，求控件的宽度
//		{
//			//得到父容器的高度
//			int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//			//得到孩子的高度（ImageView的高度）
//			int childHeight = parentHeight - getPaddingBottom() - getPaddingTop();
//
//			//计算孩子的宽度（控件的宽度/控件的高度 = mPicRatio)
//			int childWidth = (int) (childHeight*mPicRatio+ .5f);
//
//			//计算父容器的宽度
//			int parentWidth = childWidth + getPaddingRight() + getPaddingLeft();
//
//			//主动测绘孩子
//			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
//			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
//			measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
//
//			//设置自己的测试结果
//			setMeasuredDimension(parentWidth, parentHeight);
//		}
//		else{
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		}
//	}//onMeasure
//	
//	
//}//End
