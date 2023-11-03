package com.example.railwayttable.Activity;

public class ConnectionModel {
    String nazwa, stacjePosrednie[], typ, numer, peron, stacjKon;

    public ConnectionModel() {
    }

    public ConnectionModel(String nazwa, String[] stacjePosrednie, String typ, String numer, String peron, String stacjKon) {
        this.nazwa = nazwa;
        this.stacjePosrednie = stacjePosrednie;
        this.typ = typ;
        this.numer = numer;
        this.peron = peron;
        this.stacjKon = stacjKon;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String[] getStacjePosrednie() {
        return stacjePosrednie;
    }

    public void setStacjePosrednie(String[] stacjePosrednie) {
        this.stacjePosrednie = stacjePosrednie;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public String getPeron() {
        return peron;
    }

    public void setPeron(String peron) {
        this.peron = peron;
    }

    public String getStacjKon() {
        return stacjKon;
    }

    public void setStacjKon(String stacjKon) {
        this.stacjKon = stacjKon;
    }
}
