package com.example.railwayttable;

public class StartStationModel {

    private int id;
    private String stacjaPocz;

    public StartStationModel() {
    }

    public StartStationModel(int id, String stacjaPocz) {
        this.id = id;
        this.stacjaPocz = stacjaPocz;
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
}
