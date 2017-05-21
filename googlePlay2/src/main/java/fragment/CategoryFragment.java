package fragment;

import holder.CategoryHolder;
import holder.CategoryItemHolder;
import holder.CategoryTitleHolder;

import java.util.List;

import protocol.CategoryProtocol;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import base.BaseFragment;
import base.BaseHolder;
import base.LoadingPager.LoadedResult;
import base.MyBaseAdapter;
import bean.CategoryInfoBean;
import factory.ListViewFactory;

public class CategoryFragment extends BaseFragment {
	List<CategoryInfoBean> mDatas;
	@Override
	public LoadedResult initData() {
		// 执行耗时操作,真正加载数据
		CategoryProtocol protocol = new CategoryProtocol();

		try {
			mDatas = protocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		// 返回成功的视图
		ListView listView = ListViewFactory.createListView();
		listView.setAdapter(new CategoryAdapter(listView,mDatas));
		return listView;
	}

	class CategoryAdapter extends MyBaseAdapter<CategoryInfoBean>
	{

		public CategoryAdapter(AbsListView absListView,
							   List<CategoryInfoBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<CategoryInfoBean> getSpecialHolder(int position) {
			CategoryInfoBean categoryInfoBean = mDatas.get(position);
			// 如果是title -->titleHolder
			if(categoryInfoBean.isTitle)
			{
				return new CategoryTitleHolder();
			}else{
				//如果不是title -->itemHolder
				return new CategoryItemHolder();
			}
		}//getSpecialHolder

		//得到view的类型种类
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1; //2+1=3（默认是2种，复写后加1种）
		}

		//获得当前view的类型
		@Override
		public int getNomalViewType(int position) {//0,1,2(范围：0到getviewTypeCount()-1)
			CategoryInfoBean categoryInfoBean = mDatas.get(position);
			if(categoryInfoBean.isTitle)
			{
				return super.getNomalViewType(position)+1;//2
			}else{
				return super.getNomalViewType(position);//1
			}
		}//getNomalViewType
		
		
	}//Class.CategoryAdapter
}//End