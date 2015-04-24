package felix.example.strengthhelper.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import felix.example.strengthhelper.R;
import felix.example.strengthhelper.model.Practice;
import felix.example.strengthhelper.model.PracticeGroup;
import felix.example.strengthhelper.model.PracticeLab;
import felix.example.strengthhelper.utils.DateFormatUtil;
import felix.example.strengthhelper.view.MyMarkerView;

/**
 * Created by felix on 15/2/28.
 */
public class LineChartFragment extends Fragment {

    private static final String TAG = "LineChartFragment";
    private static final String EXTRA_PRACTICE_ID = "felix.example.strengthhelper.practice_id";
    private static final String CHART_MODEL = "felix.example.strengthhelper.LineChartFragment_chartmodel";
    public static final int CHART_MODEL_WEEK = 0;
    public static final int CHART_MODEL_MONTH = 1;
    public static final int CHART_MODEL_YEAR = 2;


    private UUID mPracticeId;
    private LineChart mLineChart;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    private Typeface mTf; //字体
    private ArrayList<PracticeGroup> mGroups;
    private Date mStartDate;
    private Date mEndDate;

    public static LineChartFragment newInstance(UUID practiceID, int chartModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PRACTICE_ID, practiceID);
        bundle.putSerializable(CHART_MODEL, chartModel);
        Log.i(TAG, practiceID + " " + chartModel);
        LineChartFragment fragment = new LineChartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linechart, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.chart);
        // 自定义字体
        mTf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");
        // 生产数据
        LineData data = getChartData(36, 100);
        setupChart(mLineChart, data, getActivity().getResources().getColor(R.color.chart_bg));
    }

    /**
     * 解析接收到的参数
     */
    private void parseArguments() {
        if (getArguments() != null) {
            mPracticeId = (UUID) getArguments().getSerializable(
                    EXTRA_PRACTICE_ID);
            Practice practice = PracticeLab.getInstance(getActivity()).getPractice(mPracticeId);
            mGroups = PracticeLab.getInstance(getActivity())
                    .switch2Group(practice);
        }
    }

    // 设置显示的样式
    void setupChart(LineChart chart, LineData data, int color) {
        // if enabled, the chart will always start at zero on the y-axis
        chart.setStartAtZero(true);

        // disable the drawing of values into the chart
        chart.setDrawYValues(true);

        chart.setDrawBorder(false);

        if (getDataFromGroups() && (getPracticeTitle() != null)) {
            String chart_desc = getActivity().getResources().getString(R.string.chart_desc_title,
                    getPracticeTitle(),
                    DateFormatUtil.date2String(mStartDate, "yyyy-MM-dd"),
                    DateFormatUtil.date2String(mEndDate, "yyyy-MM-dd"));
            chart.setDescription(chart_desc);// 数据描述
        } else {
            // no description text
            chart.setDescription("");// 数据描述
        }


        // enable / disable grid background
        chart.setDrawGridBackground(false); // 是否显示表格颜色
        chart.setGridColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        chart.setGridWidth(1.25f);// 表格线的线宽

        // enable touch gestures
        chart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        chart.setDragEnabled(true);// 是否可以拖拽
        chart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);//
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        // set the marker to the chart
        chart.setMarkerView(mv);

        chart.setBackgroundColor(color);// 设置背景

        chart.setValueTypeface(mTf);// 设置字体

        // add data
        chart.setData(data); // 设置数据

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend(); // 设置标示，就是那个一组y的value的

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);// 样式
        l.setFormSize(6f);// 字体
        l.setTextColor(Color.WHITE);// 颜色
        l.setTypeface(mTf);// 字体

        YLabels y = chart.getYLabels(); // y轴的标示
        y.setTextColor(Color.WHITE);
        y.setTypeface(mTf);
        y.setLabelCount(4); // y轴上的标签的显示的个数

        XLabels x = chart.getXLabels(); // x轴显示的标签
        x.setTextColor(Color.WHITE);
        x.setTypeface(mTf);

        // animate calls invalidate()...
        chart.animateX(1500); // 立即执行的动画,x轴
    }

    /**
     * 得到训练的名称
     * @return
     */
    private String getPracticeTitle() {
        return PracticeLab.getInstance(getActivity()).getPractice(mPracticeId).getTitle();
    }

    /**
     * 生成图表的数据
     *
     * @param count
     * @param range
     * @return
     */
    private LineData getChartData(int count, float range) {
        ArrayList<String> xVals = new ArrayList<String>();
        // y轴的数据
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        setChartXYVals(xVals, yVals);


        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet set1 = new LineDataSet(yVals, getActivity().getResources().getString(R.string.chart_yVal_title));
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f); // 线宽
        set1.setCircleSize(3f);// 显示的圆形大小
        set1.setColor(Color.WHITE);// 显示颜色
        set1.setCircleColor(Color.WHITE);// 圆形的颜色
        set1.setHighLightColor(Color.WHITE); // 高亮的线的颜色

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }

    /**
     * 设置图表 xy轴的数据
     *
     * @param xVals
     * @param yVals
     */
    private void setChartXYVals(ArrayList<String> xVals, ArrayList<Entry> yVals) {
        for (int i = 0; i < mGroups.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            Date date = mGroups.get(i).getDay();
            String dayStr = DateFormatUtil.date2String(date, "dd");
            String dateStr = DateFormatUtil.date2String(date, "yyyy-MM-dd");
            xVals.add(dayStr);
            float val = mGroups.get(i).getTotalNum();
            yVals.add(new Entry(val, i, dateStr));
        }
    }

    /**
     * 从mGroups中获取感兴趣的信息
     */
    private boolean getDataFromGroups() {
        if (mGroups != null && mGroups.size() > 0) {
            mStartDate = mGroups.get(0).getDay();
            mEndDate = mGroups.get(mGroups.size() - 1).getDay();
            return true;
        }
        return false;
    }
}
