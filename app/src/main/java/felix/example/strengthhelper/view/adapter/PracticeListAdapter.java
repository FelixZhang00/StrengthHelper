package felix.example.strengthhelper.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.model.Practice;

/**
 * Created by felix on 15/2/20.
 */
public class PracticeListAdapter extends ArrayAdapter<Practice> {

    //显示的时间格式
    private String DATE_FORMAT = "yyyy-MM-dd EEE HH:mm";
    private ArrayList<Practice> mPractices;
    private Context mContext;

    public PracticeListAdapter(ArrayList<Practice> practices, Context context) {
        super(context, 0, practices);
        mPractices = practices;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // convertView = LayoutInflater.from(getActivity()).inflate(
            // R.layout.list_item_crime, null);
            convertView = View.inflate(mContext,
                    R.layout.list_item_practice, null);

        }
        TextView tvTitle = (TextView) convertView
                .findViewById(R.id.tv_practice_title);
        Practice practice = mPractices.get(position);
        if (practice.isTitleEmpty()) {
            tvTitle.setText(R.string.practice_title_empty);
        } else {
            tvTitle.setText(practice.getTitle());
        }

        TextView tvTarget = (TextView) convertView
                .findViewById(R.id.tv_practice_target);
        tvTarget.setText("" + practice.getTargetNum());

        TextView tvDeadline = (TextView) convertView
                .findViewById(R.id.tv_practice_enddate);
        Date endDate = practice.getEndDate();
        if (endDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            tvDeadline.setText(dateFormat.format(endDate));
        }

        TextView tvRemainder = (TextView) convertView
                .findViewById(R.id.tv_practice_remainder);
        int num = practice.getRemainder();
        if (num >= 0) {
            tvRemainder.setText(num + "");
            int color = mContext.getResources().getColor(R.color.practice_num_normal);
            tvRemainder.setTextColor(color);
        } else {
            tvRemainder.setText(-num + "");
            int color = mContext.getResources().getColor(R.color.practice_num_over);
            tvRemainder.setTextColor(color);
        }
        return convertView;
    }

}
