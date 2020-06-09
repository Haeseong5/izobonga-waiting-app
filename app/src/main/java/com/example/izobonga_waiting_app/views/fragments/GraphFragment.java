package com.example.izobonga_waiting_app.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.models.Customer;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * -   요일 별 고객 추이 Combined-Chart
 * -  시간대 별 고객추이
 */
public class GraphFragment extends Fragment {
    public static final String GRAPH_FRAGMENT = "GraphFragment";
    private ArrayList<Customer> mCustomers;
    private TextView tvTitle;
    private CombinedChart mChart;

    private final int mCount = 12;
    private String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    private String[] mDays = new String[] {
            "월", "화", "수", "목", "금", "토", "일"
    };
    float[] mTotals = new float[7];
    float[] mChildren = new float[7];
    private final SimpleDateFormat WEEKDAY_FORMAT = new SimpleDateFormat("EE", Locale.KOREA);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomers = getArguments().getParcelableArrayList(GRAPH_FRAGMENT);
            if (mCustomers != null) {
                Log.d(GRAPH_FRAGMENT, String.valueOf(mCustomers.size()));
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        tvTitle = rootView.findViewById(R.id.graph_tv_title);
        mChart = rootView.findViewById(R.id.graph_combined_chart);
        initDataSet();

        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mDays[(int) value % mDays.length];
//                return "";
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
//        data.setValueTypeface(tfLight);

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        mChart.setData(data);
        mChart.invalidate();
        return rootView;
    }

//데이터 전처리
    private void initDataSet(){
        for (int i=0; i<mCustomers.size(); i++){
            switch (WEEKDAY_FORMAT.format(mCustomers.get(i).getTimestamp().toDate())){
                case  "월":
                    mTotals[0] += mCustomers.get(i).getPersonnel();
                    mChildren[0] += mCustomers.get(i).getChild();
                    break;
                case  "화":
                    mTotals[1] += mCustomers.get(i).getPersonnel();
                    mChildren[1] += mCustomers.get(i).getChild();
                    break;
                case  "수":
                    mTotals[2] += mCustomers.get(i).getPersonnel();
                    mChildren[2] += mCustomers.get(i).getChild();
                    break;
                case  "목":
                    mTotals[3] += mCustomers.get(i).getPersonnel();
                    mChildren[3] += mCustomers.get(i).getChild();
                    break;
                case  "금":
                    mTotals[4] += mCustomers.get(i).getPersonnel();
                    mChildren[4] += mCustomers.get(i).getChild();
                    break;
                case  "토":
                    mTotals[5] += mCustomers.get(i).getPersonnel();
                    mChildren[5] += mCustomers.get(i).getChild();
                    break;
                case  "일":
                    mTotals[6] += mCustomers.get(i).getPersonnel();
                    mChildren[6] += mCustomers.get(i).getChild();
                    break;
            }
        }
    }

    //요일별 총 인원 수 - 몇월 며칠날 몇명왔는지 보단 무슨요일에 대기손님이 몰리는지 아는 게 나을듯.
    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<>();
//        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int index = 0; index < mTotals.length; index++) {
            entries1.add(new BarEntry(index, mTotals[index]));
        }

        BarDataSet set1 = new BarDataSet(entries1, "총 인원 수");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

//        BarDataSet set2 = new BarDataSet(entries2, "");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1);
//        d.setBarWidth(barWidth);

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;

    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < mChildren.length; index++){
            entries.add(new Entry(index, mChildren[index]));
        }

        LineDataSet set = new LineDataSet(entries, "아동 인원 수");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

//    private BarData generateBarData() {
//
//        ArrayList<BarEntry> entries1 = new ArrayList<>();
//        ArrayList<BarEntry> entries2 = new ArrayList<>();
//
//        for (int index = 0; index < mCount; index++) {
//            entries1.add(new BarEntry(0, getRandom(25, 25)));
//
//            // stacked
//            entries2.add(new BarEntry(0, new float[]
//                    {
//                        getRandom(13, 12), getRandom(13, 12)
//                    }
//                    ));
//        }
//
//        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
//        set1.setColor(Color.rgb(60, 220, 78));
//        set1.setValueTextColor(Color.rgb(60, 220, 78));
//        set1.setValueTextSize(10f);
//        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//        BarDataSet set2 = new BarDataSet(entries2, "");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
//        float barWidth = 0.45f; // x2 dataset
//        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
//
//        BarData d = new BarData(set1, set2);
//        d.setBarWidth(barWidth);
//
//        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0
//
//        return d;
//
//    }
//    private float getRandom(float range, float startsfrom) {
//        return (float) (Math.random() * range) + startsfrom;
//    }
}

//https://stackoverrun.com/ko/q/12178903