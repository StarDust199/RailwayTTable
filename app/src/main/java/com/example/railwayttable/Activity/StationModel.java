package com.example.railwayttable.Activity;

import java.util.List;

public class StationModel {
    private String nazwaStacji;
    private String odjazd;
    private String przyjazd;
    List<StationModel> stations;

    public List<StationModel> getStations() {
        return stations;
    }

    public void setStations(List<StationModel> stations) {
        this.stations = stations;
    }

    public StationModel() {
    }

    public StationModel(String nazwaStacji, String odjazd, String przyjazd) {
        this.nazwaStacji = nazwaStacji;
        this.odjazd = odjazd;
        this.przyjazd = przyjazd;
    }

    public String getNazwaStacji() {
        return nazwaStacji;
    }

    public void setNazwaStacji(String nazwaStacji) {
        this.nazwaStacji = nazwaStacji;
    }

    public String getOdjazd() {
        return odjazd;
    }

    public void setOdjazd(String odjazd) {
        this.odjazd = odjazd;
    }

    public String getPrzyjazd() {
        return przyjazd;
    }

    public void setPrzyjazd(String przyjazd) {
        this.przyjazd = przyjazd;
    }
}
