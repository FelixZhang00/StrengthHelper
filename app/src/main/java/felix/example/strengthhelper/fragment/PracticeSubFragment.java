package felix.example.strengthhelper.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.activity.PracticeSubActivity;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeGroup;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.model.PracticeSub;
import felix.example.strengthhelper.utils.Logger;
import felix.example.strengthhelper.utils.PromptManager;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PracticeSubFragment extends Fragment {

	private static final String TAG = "PracticeSubFragment";

	private ExpandableListView mExlist;
	private MyExpandableListAdapter mAdapter;
	private Practice mPractice;

	private ArrayList<PracticeGroup> mGroups;

	public static PracticeSubFragment newInstance(UUID practiceID) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(PracticeSubActivity.EXTRA_PRACTICE_ID,
				practiceID);
		PracticeSubFragment fragment = new PracticeSubFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true); // 在旋转屏幕是保持fragment实例(不包括actionbar)

		if (getArguments() != null) {
			UUID id = (UUID) getArguments().getSerializable(
					PracticeSubActivity.EXTRA_PRACTICE_ID);
			mPractice = PracticeLab.getInstance(getActivity()).getPractice(id);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置actionbar 左边的返回键
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			if (NavUtils.getParentActivityName(getActivity()) != null) { // 只有当此activity设置了home才让左上角的返回键有效
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			// 设置当前的fragment拥有菜单
			setHasOptionsMenu(true);
		}

		View view = inflater.inflate(R.layout.practicesub_list, container,
				false);
		init(view);
		return view;
	}

	private void init(View view) {
		getActivity().getActionBar().setTitle(mPractice.getTitle());

		mGroups = PracticeLab.getInstance(getActivity())
				.switch2Group(mPractice);
		mAdapter = new MyExpandableListAdapter(getActivity(), mGroups);
		mExlist = (ExpandableListView) view.findViewById(R.id.ex_list);
		mExlist.setAdapter(mAdapter);
		// mExlist.setGroupIndicator(null);// 将控件默认的左边箭头去掉
		mExlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, final int childPosition, long id) {
				PromptManager.showInfoDialog(getActivity(), "是否删此条记录", null,
						"确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								int index = PracticeLab.getInstance(
										getActivity()).findPracticeSubIndex(
										groupPosition, childPosition, mGroups);
								Logger.i(TAG, "the index : " + index);
								PracticeLab.getInstance(getActivity())
										.removePracticeSub(mPractice.getId(),
												index);
								mGroups = PracticeLab
										.getInstance(getActivity())
										.switch2Group(mPractice);
								mAdapter.setGroups(mGroups);
								mAdapter.notifyDataSetChanged();
								sendResult(Activity.RESULT_OK);
							}
						});

				return true;
			}
		});
	}

	private void sendResult(int resultOk) {
		getActivity().setResult(resultOk);
	}

	class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private Context context;
		ArrayList<PracticeGroup> groups;

		/*
		 * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
		 */
		public MyExpandableListAdapter(Context context,
				ArrayList<PracticeGroup> groups) {
			this.groups = groups;
			this.context = context;
		}

		public void setGroups(ArrayList<PracticeGroup> groups) {
			this.groups = groups;
		}

		@Override
		public int getGroupCount() {

			return groups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groups.get(groupPosition).getChilds().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition).getChilds().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * 获取一级列表view对象
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.expandlist_group, parent, false);
			}
			PracticeGroup group = groups.get(groupPosition);
			TextView tv_date = (TextView) convertView
					.findViewById(R.id.tv_expand_group_date);
			TextView tv_total_num = (TextView) convertView
					.findViewById(R.id.tv_expand_group_total);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEE");
			tv_date.setText(dateFormat.format(group.getDay()));
			tv_total_num.setText("" + group.getTotalNum());

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.expandlist_child, parent, false);
			}
			PracticeSub practiceSub = groups.get(groupPosition).getChilds()
					.get(childPosition);
			TextView tv_time = (TextView) convertView
					.findViewById(R.id.tv_expand_child_time);
			TextView tv_num = (TextView) convertView
					.findViewById(R.id.tv_expand_child_num);
			LinearLayout ll = (LinearLayout) convertView
					.findViewById(R.id.ll_expand_child);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			tv_time.setText(dateFormat.format(practiceSub.getDate()));
			tv_num.setText("" + practiceSub.getNum());

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 系统提供的左上角的返回菜单
			// Toast.makeText(getActivity(), "android.R.id.home", 0).show();
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				// NavUtils.navigateUpFromSameTask(getActivity());
				getActivity().finish();
			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
