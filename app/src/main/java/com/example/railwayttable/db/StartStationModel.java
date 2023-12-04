package com.example.railwayttable.db;

import com.example.railwayttable.Activity.Station;

public class StartStationModel implements Station {

    private int id;
    private String stacjaPocz;
    private boolean isFavorite;

    public StartStationModel() {
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    public StartStationModel(int id, String stacjaPocz) {
        this.id = id;
        this.stacjaPocz = stacjaPocz;
        this.isFavorite = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStacjaPocz() {
        return stacjaPocz;
    }

    public void setStacjaPocz(String stacjaPocz) {
        this.stacjaPocz = stacjaPocz;
    }

    @Override
    public String getStationName() {
        return null;
    }
}
