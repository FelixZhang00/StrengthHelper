package felix.example.strengthhelper.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.activity.PracticeSubActivity;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeGroup;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.utils.DateFormatUtil;
import felix.example.strengthhelper.utils.Logger;
import felix.example.strengthhelper.utils.PromptManager;
import felix.example.strengthhelper.view.adapter.MyExpandableListAdapter;

public class PracticeSubFragment extends Fragment {

    private static final String TAG = "PracticeSubFragment";

    private ExpandableListView mExlist;
    private TextView mTvTotalTitle;
    private MyExpandableListAdapter mAdapter;
    private Practice mPractice;

    private ArrayList<PracticeGroup> mGroups;

    //mTvTotalTitle内显示的数据
    private int mTotalAll = 0;
    private Date mStartDate;
    private Date mEndDate;

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

        parseArguments();

    }

    private void parseArguments() {
        if (getArguments() != null) {
            UUID id = (UUID) getArguments().getSerializable(
                    PracticeSubActivity.EXTRA_PRACTICE_ID);
            mPractice = PracticeLab.getInstance(getActivity()).getPractice(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setActionbar();

        View view = inflater.inflate(R.layout.practicesub_list, container,
                false);
        init(view);
        return view;
    }

    private void setActionbar() {
        // 设置actionbar 左边的返回键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            if (NavUtils.getParentActivityName(getActivity()) != null) { // 只有当此activity设置了home才让左上角的返回键有效
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
            // 设置当前的fragment拥有菜单
            setHasOptionsMenu(true);
        }
        getActivity().getActionBar().setTitle(mPractice.getTitle());
    }

    private void init(View view) {
        refreshGroups();
        mAdapter = new MyExpandableListAdapter(getActivity(), mGroups);
        mExlist = (ExpandableListView) view.findViewById(R.id.ex_list);
        mExlist.setAdapter(mAdapter);
        // mExlist.setGroupIndicator(null);// 将控件默认的左边箭头去掉
        mExlist.setOnChildClickListener(new ExpandableListChildClickListener());

        TextView emptyTv = (TextView) view.findViewById(R.id.empty);
        mExlist.setEmptyView(emptyTv);

        mTvTotalTitle = (TextView) view.findViewById(R.id.tv_practicesub_list_total_title);
        if (mGroups != null && mGroups.size() > 0) {
            setTotalTitle();
        }

    }

    /**
     * 设置开始行总数数据
     */
    private void setTotalTitle() {
        getDataFromGroups();
        String totalTitle = getString(R.string.tv_practicesub_list_title,
                DateFormatUtil.date2String(mStartDate, "yyyy-MM-dd"),
                DateFormatUtil.date2String(mEndDate, "yyyy-MM-dd"),
                mTotalAll);
        mTvTotalTitle.setText(totalTitle);
    }


    class ExpandableListChildClickListener implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
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
                            refreshGroups();
                            mAdapter.setGroups(mGroups);
                            mAdapter.notifyDataSetChanged();
                            sendResult(Activity.RESULT_OK);
                        }
                    });

            return true;
        }
    }

    /**
     * 重新获取Groups
     */
    private void refreshGroups() {
        mGroups = PracticeLab.getInstance(getActivity())
                .switch2Group(mPractice);
    }

    /**
     * 从mGroups中获取感兴趣的信息
     */
    private void getDataFromGroups() {
        mStartDate = mGroups.get(0).getDay();
        mEndDate = mGroups.get(mGroups.size() - 1).getDay();
        for (PracticeGroup group : mGroups) {
            mTotalAll += group.getTotalNum();
        }
    }

    private void sendResult(int resultOk) {
        getActivity().setResult(resultOk);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 系统提供的左上角的返回菜单
//                Toast.makeText(getActivity(), "android.R.id.home", 0).show();
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
