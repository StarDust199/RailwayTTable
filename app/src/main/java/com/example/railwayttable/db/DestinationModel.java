package com.example.railwayttable.db;

import com.example.railwayttable.Activity.Station;

public class DestinationModel implements Station {
    private int id;
    private String stacjaKon;
    private boolean isFavorite;
    public DestinationModel() {

    }
    public DestinationModel(int id, String stacjaKon) {
        this.id = id;
        this.stacjaKon = stacjaKon;
        this.isFavorite = false;
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStacjaKon() {
        return stacjaKon;
    }

    public void setStacjaKon(String stacjaPocz) {
        this.stacjaKon = stacjaPocz;
    }

    @Override
    public String getStationName() {
        return stacjaKon;
    }
}
