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
import org.vzw.beta.homestation.beans.Fuel;
import org.vzw.beta.homestation.tools.Utils;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Jorciney on 22/03/2016.
 */
public class FuelFragment extends Fragment {
    private static TextView infoTextView;
    public static View view;
    private static String info;
    private static ArrayList<BarDataSet> dataSets = new ArrayList<>();
    private static BarDataSet barDataSet1;
    private static BarChart chart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fuel_frag_layout, container, false);
        infoTextView = (TextView) view.findViewById(R.id.fuel_data_info);

        FuelFragment.updateTextView();
        FuelFragment.setUpChart();

        return view;
    }

    /*Chart*/
    private static void setUpChart() {

        FuelFragment.chart = (BarChart) view.findViewById(R.id.chartFuelGraphicImage);
        BarData data = new BarData(getXAxisValues());
        getDataSet();
        for (int i = 0; i < FuelFragment.dataSets.size(); i++) {
            data.addDataSet(FuelFragment.dataSets.get(i));
        }
        FuelFragment.chart.setData(data);
        FuelFragment.chart.setDescription("My Chart");
        FuelFragment.chart.animateXY(2000, 2000);
        FuelFragment.chart.invalidate();
    }

    private static void setDataSet(int month, Long value) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry((float) value, month));
        FuelFragment.barDataSet1 = new BarDataSet(valueSet1, "");
        FuelFragment.barDataSet1.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        FuelFragment.dataSets.add(FuelFragment.barDataSet1);
    }

    private static void getDataSet() {
        FuelFragment.dataSets.clear();
        String date;
        Long value;
        for (Map.Entry<String, Object> entry : Utils.dataObjectsFuelDB.entrySet()) {
            date = (((Fuel) entry.getValue()).getDate()).split("/")[1];
            value = (((Fuel) entry.getValue()).getValue());
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
            info = "\nTotal Entries: " + Utils.dataObjectsFuelDB.size() + "\n\n";
            for (Map.Entry<String, Object> entry : Utils.dataObjectsFuelDB.entrySet()) {
                info += "Date:\t" + (((Fuel) entry.getValue()).getDate()) + "\t\t\tValue:\t" + (((Fuel) entry.getValue()).getValue() + "\n");
            }
            infoTextView.setText(info + "\nTotal Entries: " + Utils.dataObjectsFuelDB.size() + "\n");
            FuelFragment.setUpChart();
        }
    }
}
