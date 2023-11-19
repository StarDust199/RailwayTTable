package com.example.railwayttable.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionModel {
    String  nazwa, stacjaKon, typ;
    long numer;

    static Map<String, Map<String, String>> stacje;
    public ConnectionModel() {
    }

    public ConnectionModel(String nazwa, String stacjaKon, String typ, long numer, Map<String, Map<String, String>> stacje) {
        this.nazwa = nazwa;
        this.stacjaKon = stacjaKon;
        this.typ = typ;
        this.numer = numer;
        this.stacje = stacje;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public long getNumer() {
        return numer;
    }


    public void setNumer(long numer) {
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

    public static List<StationModel> getStations() {
        List<StationModel> stations = new ArrayList<>();
        if (stacje != null) {
            for (Map.Entry<String, Map<String, String>> entry : stacje.entrySet()) {
                String nazwaStacji = entry.getKey();
                Map<String, String> details = entry.getValue();
                String odjazd = details.get("odjazd");
                String przyjazd = details.get("przyjazd");

                StationModel station = new StationModel(nazwaStacji, odjazd, przyjazd);
                stations.add(station);
            }
        }
        return stations;
    }
    public static List<StationModel> findConnectionsBetweenStations(String startStation, String endStation, Map<String, Map<String, String>> stacje) {
        List<StationModel> connections = new ArrayList<>();
        if (stacje != null) {
        for (Map.Entry<String, Map<String, String>> entry : ConnectionModel.stacje.entrySet()) {
            String nazwaStacji = entry.getKey();

            if (nazwaStacji.equals(startStation)) {

                Map<String, String> stationDetails = entry.getValue();
                String odjazd = stationDetails.get("odjazd");
                String przyjazd = stationDetails.get("przyjazd");
                StationModel station = new StationModel(nazwaStacji, odjazd, przyjazd);
                connections.add(station);

                for (Map.Entry<String, String> detailEntry : stationDetails.entrySet()) {
                    if (detailEntry.getKey().equals("odjazd") || detailEntry.getKey().equals("przyjazd")) {

                        continue;
                    }

                    String innerStationName = detailEntry.getKey();
                    odjazd = detailEntry.getValue();
                    przyjazd = stationDetails.get(innerStationName + "_przyjazd");
                    StationModel innerStation = new StationModel(innerStationName, odjazd, przyjazd);
                    connections.add(innerStation);

                    if (innerStationName.equals(endStation)) {

                        break;
                    }
                }
            }
        }}
        return connections;
    }

    public static Map<String, Map<String, String>> getStacje() {
        return stacje;
    }

    public void setStacje(Map<String, Map<String, String>> stacje) {
        this.stacje = stacje;
    }
}
