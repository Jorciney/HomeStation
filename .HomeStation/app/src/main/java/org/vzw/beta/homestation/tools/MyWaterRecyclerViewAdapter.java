package org.vzw.beta.homestation.tools;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.vzw.beta.homestation.R;
import org.vzw.beta.homestation.beans.Electricity;
import org.vzw.beta.homestation.beans.Water;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jorciney on 5/05/2016.
 */
public class MyWaterRecyclerViewAdapter extends RecyclerView.Adapter<MyWaterRecyclerViewAdapter.EnergyViewHolder> {

    public MyWaterRecyclerViewAdapter() {
    }

    /**
     * Recycle Viewer ViewHolder
     */
    public static class EnergyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView dateTextView;
        private TextView amountTextView;
        private ImageView imageView;

        EnergyViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.energy_cardView);
            dateTextView = (TextView) v.findViewById(R.id.energy_data_date);
            amountTextView = (TextView) v.findViewById(R.id.energy_data_value);
            imageView = (ImageView) v.findViewById(R.id.energy_data_image);
        }

    }

    @Override
    public EnergyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_energy_recyclerview, parent, false);
        EnergyViewHolder viewHolder = new EnergyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EnergyViewHolder energyViewHolder, int position) {
        if(position %2==0){
            energyViewHolder.cardView.setBackgroundColor(Color.parseColor("#FFEBF8FA"));
        }else{
            energyViewHolder.cardView.setBackgroundColor(Color.WHITE);
        }
            List<Water> listWater = new ArrayList<>();
            for (Map.Entry<String, Object> entry : Utils.dataObjectsWaterDB.entrySet()) {
                listWater.add((Water) entry.getValue());
            }
        Collections.sort(listWater, new Comparator<Water>() {
            @Override
            public int compare(Water lhs, Water rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
            energyViewHolder.dateTextView.setText(listWater.get(position).getDate());
            energyViewHolder.amountTextView.setText(listWater.get(position).getValue() + "");
            energyViewHolder.imageView.setImageResource(R.drawable.water_icon);
    }

    /**
     * getItemCount
     */
    @Override
    public int getItemCount() {
        return Utils.dataObjectsWaterDB.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
