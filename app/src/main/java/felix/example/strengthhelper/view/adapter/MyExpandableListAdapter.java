package felix.example.strengthhelper.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.model.PracticeGroup;
import felix.example.strengthhelper.model.PracticeSub;

/**
 * Created by felix on 15/2/21.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ArrayList<PracticeGroup> groups;


    /*
     * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
     */
    public MyExpandableListAdapter(Context context,
                                   ArrayList<PracticeGroup> groups) {
        this.groups = groups;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            convertView = mLayoutInflater.inflate(R.layout.expandlist_group, parent, false);
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
            convertView = mLayoutInflater.inflate(R.layout.expandlist_child, parent, false);

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
        return true;
    }

}
