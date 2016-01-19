package nl.antonsteenvoorden.ikpmd.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import nl.antonsteenvoorden.ikpmd.R;
import nl.antonsteenvoorden.ikpmd.model.Module;

/**
 * Created by Anton on 19/01/2016.
 */
public class BarChartFragment extends Fragment {
    private CombinedChart barChart;
    View rootView;
    Context context;

    List<Entry> lineEntries;
    ArrayList<BarEntry> barEntries;

    public static BarChartFragment newInstance() {
        BarChartFragment fragment = new BarChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        super.onCreate(savedInstanceState);

        context = rootView.getContext();
        lineEntries = new ArrayList<>();
        barEntries = new ArrayList<BarEntry>();

        barChart = (CombinedChart) rootView.findViewById(R.id.chart);
        initBarChart();
        getData();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
    private void initBarChart() {
        barChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        barChart.setClickable(false);

        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setDrawHighlightArrow(false);
        barChart.setDrawBorders(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDescriptionColor(Color.WHITE);
        barChart.setDrawGridBackground(false);
        barChart.setDescription("Punten gehaald per periode");
    }
    private void getData() {
        lineEntries.clear();
        barEntries.clear();
        for(int i = 0; i < 4; i++) {
            int ectsTmp = 0;
            int ectsReceivedTmp = 0;
            for(Module module : Module.getPeriod(i+1)) {
                ectsTmp += module.getEcts();
                if(module.getGrade()>=5.5) {
                    ectsReceivedTmp+=module.getEcts();
                }
            }
            lineEntries.add(new Entry(ectsTmp,i));
            barEntries.add(new BarEntry(ectsReceivedTmp, i));
        }
        setData();
    }

    private void setData(){

        // LINE DATA
        LineData line = new LineData();
        LineDataSet lineDataSet = new LineDataSet(lineEntries,"Line DataSet");
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        lineDataSet.setValueTextColor(Color.WHITE);
        line.addDataSet(lineDataSet);

        // BAR DATA
        BarData bar = new BarData();
        BarDataSet set = new BarDataSet(barEntries, "Bar DataSet");
        set.setColor(Color.rgb(0 ,188,186));
        set.setValueTextColor(Color.WHITE);
        bar.addDataSet(set);

        // ADD data to the chart
        String[] xValues = {"1","2","3","4"};
        CombinedData data = new CombinedData(xValues);
        data.setData(line);
        data.setData(bar);

        barChart.setData(data);
        barChart.invalidate();
    }
}
