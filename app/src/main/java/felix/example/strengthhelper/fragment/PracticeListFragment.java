package felix.example.strengthhelper.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.view.adapter.PracticeListAdapter;

public class PracticeListFragment extends ListFragment {

	private static final String TAG = "PracticeListFragment";
	private ListView mLv;
	private TextView mTv; // listview为空时用于提示用户为空
	private PracticeListAdapter adapter;
	private ArrayList<Practice> mPractices;

	private Callbacks mCallbacks;



	public interface Callbacks {
		/**
		 *  list中某个条目或actionbar右上角的加号被点击时调用
		 * 
		 * @param practice
		 * @param model
		 *            启动PracticeFragment的模式
		 * @param from
		 */
		void onPracticeSelected(Practice practice, int model, int from);
	}

	@Override
	public void onAttach(Activity activity) {
		mCallbacks = (Callbacks) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		mCallbacks = null;
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setRetainInstance(true); // 在旋转屏幕是保持fragment实例(不包括actionbar)
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.practices_title);

		mPractices = PracticeLab.getInstance(getActivity()).getPractices();

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { //未处理
		}

		View v = inflater.inflate(R.layout.fragment_list, container, false);
		mLv = (ListView) v.findViewById(android.R.id.list);
		mTv = (TextView) v.findViewById(android.R.id.empty);
		adapter = new PracticeListAdapter(mPractices,getActivity());
		mLv.setAdapter(adapter);
		mLv.setEmptyView(mTv); // 设置此控件，用于系统自动切换（当list为空时）

		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// 以查看模式打开PracticeFragment
		Practice p = mPractices.get(position);
		mCallbacks.onPracticeSelected(p, PracticeFragment.MODEL_SEE,
				PracticeFragment.FROM_DONOT_CARE);
	}



	@Override
	public void onResume() {
		updateListViewUI();
		super.onResume();
	}

	@Override
	public void onPause() {
        saveData();
        super.onPause();
	}


    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_list_item, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_practice:
			Practice practice = new Practice();
			PracticeLab.getInstance(getActivity()).addPractice(practice);

			updateListViewUI();

			mCallbacks.onPracticeSelected(practice,
					PracticeFragment.MODEL_EDIT, PracticeFragment.FROM_ADD);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

    /**
     * 保存数据
     */
    private void saveData() {
        PracticeLab.getInstance(getActivity()).savePractice();
    }

    /**
     * 更新listview显示的UI
     */
	public void updateListViewUI() {
		adapter.notifyDataSetChanged();
		
	}

}
