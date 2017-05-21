package holder;

import com.example.googleplay2.R;

import utils.UIUtils;
import android.view.View;
import base.BaseHolder;

public class MenuHolder extends BaseHolder<Object> {

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
		return view;
	}

	@Override
	public void refreshHolderView(Object data) {
		// TODO Auto-generated method stub
		
	}

}//End
