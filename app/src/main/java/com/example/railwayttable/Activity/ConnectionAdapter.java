package com.example.railwayttable.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;

import java.util.ArrayList;
import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.MyViewHolder>  {
    Context context;
    ArrayList<ConnectionModel> list;
    AdapterView.OnItemClickListener onItemClickListener;
    private int displayedItems = 4;

    private int ostatnioOtwartaPozycja = RecyclerView.NO_POSITION;


    public void setList(List<ConnectionModel> newList) {
        list = (ArrayList<ConnectionModel>) newList;
        notifyDataSetChanged();
    }



    public ConnectionAdapter(Context context, ArrayList<ConnectionModel> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = (AdapterView.OnItemClickListener) listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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
        holder.godzPrzy.setText(connectionModel.getGodzinaPrzyjazdu());
        setTrainImage(holder.imageView, connectionModel.getTyp());

        boolean isExpanded = connectionModel.isExpanded();
        holder.expandedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.typ.setText(connectionModel.getTyp());
        holder.numer.setText(String.valueOf(connectionModel.getNumer()));
        holder.stacKon.setText(connectionModel.getStacjaKon());
        holder.name.setText(connectionModel.getNazwa());



        holder.itemView.setOnClickListener(view -> {
            int pozycja = holder.getAdapterPosition();

            if (pozycja != RecyclerView.NO_POSITION) {
                if (pozycja != ostatnioOtwartaPozycja) {
                    if (ostatnioOtwartaPozycja != RecyclerView.NO_POSITION) {
                        list.get(ostatnioOtwartaPozycja).setExpanded(false);
                        list.get(ostatnioOtwartaPozycja).setStationExpanded(false);
                        notifyItemChanged(ostatnioOtwartaPozycja);
                    }

                    holder.expandedLayout.setVisibility(View.VISIBLE);
                    connectionModel.setExpanded(true);







                    ostatnioOtwartaPozycja = pozycja;
                } else {
                    holder.expandedLayout.setVisibility(View.GONE);
                    connectionModel.setExpanded(false);
                    connectionModel.setStationExpanded(false);
                    ostatnioOtwartaPozycja = RecyclerView.NO_POSITION;
                }

                notifyItemChanged(pozycja);
            }
        });
    }
    @Override
    public int getItemCount() {
        int limit = Math.min(list.size(), displayedItems);
        return limit;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView godzOjd, godzPrzy, name,numer, typ, stacKon, stations;
        ImageView imageView;
        ListView listStations;
        private LinearLayout expandedLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            godzOjd = itemView.findViewById(R.id.textGodzinaOdj);
            godzPrzy = itemView.findViewById(R.id.textGodzinaPrzy);
            imageView = itemView.findViewById(R.id.imageTyp);
            name = itemView.findViewById(R.id.name);
            numer= itemView.findViewById(R.id.numer);
            typ = itemView.findViewById(R.id.Typ);
            stacKon = itemView.findViewById(R.id.stationEnd);
            expandedLayout=itemView.findViewById(R.id.expandedLayout);


        }
    }

    private void setTrainImage(ImageView imageView, String trainType) {
        if ("IC".equals(trainType)) {
            imageView.setImageResource(R.drawable.icn);
        } else if ("R".equals(trainType)) {
            imageView.setImageResource(R.drawable.r);
        } else if ("TLK".equals(trainType)){

            imageView.setImageResource(R.drawable.tlk);
        } else{
            imageView.setImageResource(R.drawable.eicn);
        }
    }
}
