package factory;

import utils.UIUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

/**
 *
 * 获得listView
 *
 */
public class ListViewFactory {

	public static ListView createListView(){
		//创建一个listView
		ListView listView = new ListView(UIUtils.getContext());
		//对listview进行一些简单的设置
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		return listView;
	}
}//End
