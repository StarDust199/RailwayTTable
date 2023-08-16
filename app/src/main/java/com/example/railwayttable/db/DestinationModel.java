package com.example.railwayttable.db;

public class DestinationModel {
    private int id;
    private String stacjaKon;
    public DestinationModel() {

    }
    public DestinationModel(int id, String stacjaKon) {
        this.id = id;
        this.stacjaKon = stacjaKon;
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
}
