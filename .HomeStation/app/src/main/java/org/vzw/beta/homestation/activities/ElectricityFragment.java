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
import org.vzw.beta.homestation.beans.Electricity;
import org.vzw.beta.homestation.tools.MyElectricityRecyclerViewAdapter;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Jorciney on 22/03/2016.
 */
public class ElectricityFragment extends Fragment {
    public static View view;
    private static String info;
    private static ArrayList<BarDataSet> dataSets = new ArrayList<>();
    private static BarDataSet barDataSet1;
    private static BarChart chart;
    private static RecyclerView recyclerView;
    private static RecyclerView.LayoutManager mLayoutManager;
    public static MyElectricityRecyclerViewAdapter recyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.electricity_frag_layout, container, false);

        //RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.electricity_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new MyElectricityRecyclerViewAdapter();
        mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);


        ElectricityFragment.updateInfo();

        return view;
    }

    /*Chart*/
    private static void setUpChart() {
        ElectricityFragment.chart = (BarChart) ElectricityFragment.view.findViewById(R.id.chartElectricityGraphicImage);
        BarData data = new BarData(getXAxisValues());
        getDataSet();
        for (int i = 0; i < ElectricityFragment.dataSets.size(); i++) {
            data.addDataSet(ElectricityFragment.dataSets.get(i));
        }
        ElectricityFragment.chart.setData(data);
        ElectricityFragment.chart.setDescription("My Electricity Chart");
        ElectricityFragment.chart.animateXY(2000, 2000);
        ElectricityFragment.chart.invalidate();
    }

    private static void setDataSet(int month, Long value) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float) value, month));
        ElectricityFragment.barDataSet1 = new BarDataSet(valueSet1, "");
        ElectricityFragment.barDataSet1.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        ElectricityFragment.dataSets.add(ElectricityFragment.barDataSet1);
    }

    private static void getDataSet() {
        ElectricityFragment.dataSets.clear();
        String date;
        Long value;
        for (Map.Entry<String, Object> entry : Utils.dataObjectsElectricityDB.entrySet()) {
            date = (((Electricity) entry.getValue()).getDate()).split("/")[1];
            value = (((Electricity) entry.getValue()).getValue());
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
        if (ElectricityFragment.view != null) {
            setUpChart();
        }
    }
}
