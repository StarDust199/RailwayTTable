package com.example.railwayttable.Activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;
import com.example.railwayttable.db.DestinationModel;
import com.example.railwayttable.db.StartStationModel;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.StationViewHolder> {

    private List<DestinationModel> stationList;
    private FavoritesActivity.LineDrawingRecyclerViewTouchListener lineDrawingTouchListener;

    public DestinationAdapter(List<DestinationModel> destinationStationList) {
        this.stationList = destinationStationList;

    }


    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_station, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        DestinationModel destinationModel = (DestinationModel) stationList.get(position);
        holder.textStationName.setText(destinationModel.getStationName());



        holder.itemView.setOnClickListener(v -> {

            destinationModel.setSelected(!destinationModel.isSelected());
            notifyItemChanged(position);
        });
        holder.bind(destinationModel);

    }

    public Station getStationAtPosition(int position) {
        if (stationList != null && position >= 0 && position < stationList.size()) {
            return stationList.get(position);
        }
        return null;
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                if (lineDrawingTouchListener != null) {
                    lineDrawingTouchListener.draw(c);
                }
                super.onDraw(c, parent, state);
            }
        });
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