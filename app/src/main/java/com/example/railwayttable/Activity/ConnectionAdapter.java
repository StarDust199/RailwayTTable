package com.example.railwayttable.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;

import java.util.ArrayList;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.MyViewHolder> {
    Context context;
    ArrayList<ConnectionModel> list;

    public ConnectionAdapter(Context context, ArrayList<ConnectionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ConnectionModel connectionModel = list.get(position);

        holder.godzOjd.setText(connectionModel.getGodzinaOdjazdu());
        holder.type.setText(connectionModel.getTyp());
        holder.godzPrzy.setText(connectionModel.getGodzinaPrzyjazdu());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView godzOjd, type, godzPrzy;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            godzOjd = itemView.findViewById(R.id.textGodzinaOdj);
            type = itemView.findViewById(R.id.typ);
            godzPrzy = itemView.findViewById(R.id.textGodzinaPrzy);

        }
    }

    private void setTrainType(TextView typeTextView, String trainType) {
        if ("IC".equals(trainType)) {
            typeTextView.setBackgroundResource(R.drawable.icons8_rectangle_ic);
        } else if ("Regio".equals(trainType)) {
            typeTextView.setBackgroundResource(R.drawable.icons8_rectangle_regio);
        } else {
            typeTextView.setBackgroundResource(R.drawable.icons8_rectangle_eic);
        }
    }
}
