package com.example.railwayttable.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PlannerAdapter extends FirestoreRecyclerAdapter<Planner, PlannerAdapter.PlannerViewHolder>{

    Context context;
    public PlannerAdapter(@NonNull FirestoreRecyclerOptions<Planner> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PlannerViewHolder holder, int position, @NonNull Planner planner) {

        holder.txtTitle.setText(planner.title);
        holder.txtcomment.setText(planner.Description);
        holder.txtstamp.setText(Utility.timeStampToString(planner.timestamp));

    }

    @NonNull
    @Override
    public PlannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_note_item, parent, false);
        return new PlannerViewHolder(view);
    }

    public class PlannerViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitle, txtcomment, txtstamp;

    public PlannerViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtView_title);
        txtcomment = itemView.findViewById(R.id.txtComment);
        txtstamp = itemView.findViewById(R.id.timestamp);
    }
}
}