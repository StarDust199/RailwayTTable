package com.example.railwayttable.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;
import com.example.railwayttable.db.StartStationModel;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private List<? extends Station> stationList;

    public StationAdapter(List<? extends Station> stationList) {
        this.stationList = stationList;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }
    public Station getStationAtPosition(int position) {
        if (stationList != null && position >= 0 && position < stationList.size()) {
            return stationList.get(position);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        StartStationModel startStationModel = (StartStationModel) stationList.get(position);
        holder.textStationName.setText(startStationModel.getStationName());
        holder.bind(startStationModel);
    }

    @Override
    public int getItemCount() {
        return stationList != null ? stationList.size() : 0;
    }

    static class StationViewHolder extends RecyclerView.ViewHolder {
        private TextView textStationName;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            textStationName = itemView.findViewById(R.id.stationName);
        }

        public void bind(Station station) {
            textStationName.setText(station.getStationName());
        }
    }
}