package com.example.railwayttable.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionModel {
    String  nazwa, numer, stacjaKon, typ;
    Map<String, Map<String, String>> stacje;
    public ConnectionModel() {
    }

    public ConnectionModel(String nazwa, String numer, String stacjaKon, String typ, Map<String, Map<String, String>> stacje) {
        this.nazwa = nazwa;
        this.numer = numer;
        this.stacjaKon = stacjaKon;
        this.typ = typ;
        this.stacje = stacje;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public String getStacjaKon() {
        return stacjaKon;
    }

    public void setStacjaKon(String stacjaKon) {
        this.stacjaKon = stacjaKon;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public List<StationModel> getStations() {
        List<StationModel> stations = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : stacje.entrySet()) {
            String nazwaStacji = entry.getKey();
            Map<String, String> details = entry.getValue();
            String odjazd = details.get("odjazd");
            String przyjazd = details.get("przyjazd");

            StationModel station = new StationModel(nazwaStacji, odjazd, przyjazd);
            stations.add(station);
        }
        return stations;
    }

    public void setStacje(Map<String, Map<String, String>> stacje) {
        this.stacje = stacje;
    }
}
