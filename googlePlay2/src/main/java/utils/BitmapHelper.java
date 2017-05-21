package utils;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
	
	public static BitmapUtils mBitmaoUtils;
	
	static {
		mBitmaoUtils = new BitmapUtils(UIUtils.getContext());
	}
	
	public static <T extends View> void display(T container, String uri) {
		mBitmaoUtils.display(container, uri);
    }
	
}//End
