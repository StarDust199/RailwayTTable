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

        holder.name.setText(connectionModel.getNazwa());
        holder.type.setText(connectionModel.getTyp());
        holder.numer.setText(String.valueOf(connectionModel.getNumer()));
        setTrainImage(holder.imageView, connectionModel.getTyp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, numer;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            numer = itemView.findViewById(R.id.numer);
            imageView = itemView.findViewById(R.id.TrainIcon);
        }
    }

    private void setTrainImage(ImageView imageView, String trainType) {
        if ("IC".equals(trainType)) {
            imageView.setImageResource(R.drawable.icons8_line_50_green);
        } else if ("Regio".equals(trainType)) {
            imageView.setImageResource(R.drawable.icons8_line_50_orange);
        } else {

            imageView.setImageResource(R.drawable.mapa);
        }
    }
}
