package com.example.railwayttable.Activity;

public class StationModel {
    private String nazwaStacji;
    private String odjazd;
    private String przyjazd;

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
