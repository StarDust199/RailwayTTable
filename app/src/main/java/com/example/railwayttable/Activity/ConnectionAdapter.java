package com.example.railwayttable.Activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
