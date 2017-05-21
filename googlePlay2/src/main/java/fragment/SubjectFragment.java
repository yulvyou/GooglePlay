package fragment;

import holder.SubjectHolder;

import java.util.List;

import protocol.SubjectProtocol;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import base.BaseFragment;
import base.BaseHolder;
import base.LoadingPager.LoadedResult;
import base.MyBaseAdapter;
import bean.SubjectInfoBean;
import factory.ListViewFactory;

public class SubjectFragment extends BaseFragment {
	private List<SubjectInfoBean>	mDatas;

	@Override
	public LoadedResult initData() {
		SubjectProtocol protocol = new SubjectProtocol();
		
		try {
			mDatas = protocol.loadData(0);
			return checkState(mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			return LoadedResult.ERROR;
		}
	}//initData

	@Override
	public View initSuccessView() {
		// 返回成功的视图
		ListView listView = ListViewFactory.createListView();
		listView.setAdapter(new SubjectAdapter(listView, mDatas));

		return listView;
	}

	/**适配器*/
	class SubjectAdapter extends MyBaseAdapter<SubjectInfoBean>
	{

		public SubjectAdapter(AbsListView absListView,
							  List<SubjectInfoBean> dataSource) {
			super(absListView, dataSource);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BaseHolder<SubjectInfoBean> getSpecialHolder(int position) {
			return new SubjectHolder();
		}
		
	}//Class.SubjectAdapter
}//End