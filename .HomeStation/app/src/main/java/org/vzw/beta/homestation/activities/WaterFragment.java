package org.vzw.beta.homestation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Water;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Jorciney on 22/03/2016.
 */
public class WaterFragment extends Fragment {
    private static TextView infoTextView;
    public static View view;
    private static String info;
    private static ArrayList<BarDataSet> dataSets = new ArrayList<>();
    private static BarDataSet barDataSet1;
    private static BarChart chart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.water_frag_layout, container, false);
        infoTextView = (TextView) view.findViewById(R.id.water_data_info);

        WaterFragment.updateTextView();
        WaterFragment.setUpChart();

        return view;
    }

    /*Chart*/
    private static void setUpChart() {

        WaterFragment.chart = (BarChart) view.findViewById(R.id.chartWaterGraphicImage);
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

    public static void updateTextView() {
        if (infoTextView != null) {

            infoTextView.setText("");
            info = "\nTotal Entries: " + Utils.dataObjectsWaterDB.size() + "\n\n";
            for (Map.Entry<String, Object> entry : Utils.dataObjectsWaterDB.entrySet()) {
                info += "Date:\t" + (((Water) entry.getValue()).getDate()) + "\t\t\tValue:\t" + (((Water) entry.getValue()).getValue() + "\n");
            }
            infoTextView.setText(info + "\nTotal Entries: " + Utils.dataObjectsWaterDB.size() + "\n");

            WaterFragment.setUpChart();
        }
    }
}
