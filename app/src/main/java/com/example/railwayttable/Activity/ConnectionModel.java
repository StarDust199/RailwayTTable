package com.example.railwayttable.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectionModel {
    String  nazwa, stacjaKon, typ;
    long numer;
    private String stacja;
    private String godzinaOdjazdu;
    private String godzinaPrzyjazdu;
    private List<String> stacjeList;
    static Map<String, Map<String, String>> stacje;
    public ConnectionModel() {
    }

    public ConnectionModel(String nazwa, String stacjaKon, String typ, long numer, Map<String, Map<String, String>> stacje, List<String> stacjeList,String stacja, String godzinaOdjazdu, String godzinaPrzyjazdu) {
        this.nazwa = nazwa;
        this.stacjaKon = stacjaKon;
        this.typ = typ;
        this.numer = numer;
        this.stacje = stacje;
        this.stacjeList=stacjeList;
        this.stacja = stacja;
        this.godzinaOdjazdu = godzinaOdjazdu;
        this.godzinaPrzyjazdu = godzinaPrzyjazdu;
    }

    public String getStacja() {
        return stacja;
    }

    public void setStacja(String stacja) {
        this.stacja = stacja;
    }

    public String getGodzinaOdjazdu() {
        return godzinaOdjazdu;
    }

    public void setGodzinaOdjazdu(String godzinaOdjazdu) {
        this.godzinaOdjazdu = godzinaOdjazdu;
    }

    public String getGodzinaPrzyjazdu() {
        return godzinaPrzyjazdu;
    }

    public void setGodzinaPrzyjazdu(String godzinaPrzyjazdu) {
        this.godzinaPrzyjazdu = godzinaPrzyjazdu;
    }

    public List<String> getStacjeList() {
        return stacjeList;
    }

    public void setStacjeList(List<String> stacjeList) {
        this.stacjeList = stacjeList;
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

    public String getGodzinaOdjazduNaStacji(String nazwaStacji) {

      return stacje.get(nazwaStacji).get("odjazd");
    }

    public String getGodzinaPrzyjazduNaStacji(String nazwaStacji) {
        return stacje.get(nazwaStacji).get("przyjazd");
    }

    public static Map<String, Map<String, String>> getStacje() {
        return stacje;
    }

    public void setStacje(Map<String, Map<String, String>> stacje) {
        this.stacje = stacje;
    }
}
