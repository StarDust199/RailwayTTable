package com.example.railwayttable.Activity;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    public CustomArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {

        return !getItem(position).equals("Nie znaleziono stacji");
    }
}
