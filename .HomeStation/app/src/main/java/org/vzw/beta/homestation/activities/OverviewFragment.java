package org.vzw.beta.homestation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Electricity;
import org.vzw.beta.homestation.beans.Fuel;
import org.vzw.beta.homestation.beans.Gas;
import org.vzw.beta.homestation.beans.Water;
import org.vzw.beta.homestation.tools.Utils;
import android.graphics.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by user109 on 25/03/2016.
 */
public class OverviewFragment extends Fragment {
    private View view;
    private TextView infoValueTextView;
    private TextView infoCategoriesText;
    private PieChart pieChart;
    private PieDataSet dataset;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.energy_overview_frag, container, false);
        infoValueTextView = (TextView) view.findViewById(R.id.overview_data_info_values);
        infoCategoriesText=(TextView) view.findViewById(R.id.overview_data_info_categories);
        infoCategoriesText.setText("Electricity:\nWater:\nGas:\nFuel:\n");
        infoValueTextView.setText("");
        pieChart = (PieChart) view.findViewById(R.id.overview_chart_pie);
        setUpChart();
        return view;
    }

    private void setUpChart() {
        dataset = new PieDataSet(getEntries(), "Consumption");
        PieData data = new PieData(getLabels(), dataset); // initialize Piedata
        pieChart.setData(data); //set data into chart
        pieChart.setDescription("Total Consumption");
        int[]color= {Color.rgb(139, 195, 74), Color.rgb(255, 193, 7), Color.rgb(38, 198, 218), Color.rgb(239, 83, 80),
                Color.rgb(53, 194, 209)};


        dataset.setColors(color);
        pieChart.animateY(2000);
        pieChart.animateX(2000);
        pieChart.setCenterTextSizePixels((float)40);
        pieChart.setDescriptionTextSize((float) 18);
    }


    public ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Electricity");
        labels.add("Water");
        labels.add("Gas");
        labels.add("Fuel");
        return labels;
    }

    public ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(getValueDBData("Electricity").floatValue(), 0));
        entries.add(new Entry(getValueDBData("Water").floatValue(), 1));
        entries.add(new Entry(getValueDBData("Gas").floatValue(), 2));
        entries.add(new Entry(getValueDBData("Fuel").floatValue(), 3));

        return entries;
    }

    public Long getValueDBData(String str) {
        String info="";
        Long value = (long) 0;
        if (str.equals("Electricity")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsElectricityDB.entrySet()) {
                value += (((Electricity) entry.getValue()).getValue());
            }
        } else if (str.equals("Water")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsWaterDB.entrySet()) {
                value += (((Water) entry.getValue()).getValue());
            }
        } else if (str.equals("Gas")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsGasDB.entrySet()) {
                value += (((Gas) entry.getValue()).getValue());
            }
        } else if (str.equals("Fuel")) {
            for (Map.Entry<String, Object> entry : Utils.dataObjectsFuelDB.entrySet()) {
                value += (((Fuel) entry.getValue()).getValue());
            }
        }
        info+=value+"\n";
        infoValueTextView.append(info);
        return value;

    }
}
