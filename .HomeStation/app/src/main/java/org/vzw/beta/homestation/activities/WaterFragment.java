package org.vzw.beta.homestation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Water;
import org.vzw.beta.homestation.tools.MyElectricityRecyclerViewAdapter;
import org.vzw.beta.homestation.tools.MyWaterRecyclerViewAdapter;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Jorciney on 22/03/2016.
 */
public class WaterFragment extends Fragment {
    public static View view;
    private static ArrayList<BarDataSet> dataSets = new ArrayList<>();
    private static BarDataSet barDataSet1;
    private static BarChart chart;
    private static RecyclerView recyclerView;
    private static RecyclerView.LayoutManager mLayoutManager;
    public static MyWaterRecyclerViewAdapter recyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.water_frag_layout, container, false);

        //RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.water_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new MyWaterRecyclerViewAdapter();
        mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        WaterFragment.updateInfo();
        return view;
    }

    /*Chart*/
    private static void setUpChart() {

        WaterFragment.chart = (BarChart) WaterFragment.view.findViewById(R.id.chartWaterGraphicImage);
        BarData data = new BarData(getXAxisValues());
        getDataSet();
        for (int i = 0; i < WaterFragment.dataSets.size(); i++) {
            data.addDataSet(WaterFragment.dataSets.get(i));
        }
        WaterFragment.chart.setData(data);
        WaterFragment.chart.setDescription("My Chart");
        WaterFragment.chart.animateXY(2000, 2000);
        WaterFragment.chart.invalidate();
    }

    private static void setDataSet(int month, Long value) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float) value, month));
        WaterFragment.barDataSet1 = new BarDataSet(valueSet1, "");
        WaterFragment.barDataSet1.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        WaterFragment.dataSets.add(WaterFragment.barDataSet1);
    }

    private static void getDataSet() {
        WaterFragment.dataSets.clear();
        String date;
        Long value;
        for (Map.Entry<String, Object> entry : Utils.dataObjectsWaterDB.entrySet()) {
            date = (((Water) entry.getValue()).getDate()).split("/")[1];
            value = (((Water) entry.getValue()).getValue());
            setDataSet(Integer.parseInt(date) - 1, value);
        }
    }


    private static ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OUT");
        xAxis.add("NOV");
        xAxis.add("DEZ");

        return xAxis;
    }

    public static void updateInfo() {
        if (WaterFragment.view != null) {
            setUpChart();
        }
    }
}
