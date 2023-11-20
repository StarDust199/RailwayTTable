package com.example.railwayttable.Activity;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConnectionModel {
    String  nazwa, stacjaKon, typ;
    long numer;
    private String stacja;
    private String godzinaOdjazdu;
    private String godzinaPrzyjazdu;
    private List<String> stacjeList;
    private boolean expanded;
    static Map<String, Map<String, String>> stacje;
    public ConnectionModel() {
    }

    public ConnectionModel(String nazwa, String stacjaKon, String typ, long numer, Map<String, Map<String, String>> stacje, List<String> stacjeList,String stacja, String godzinaOdjazdu, String godzinaPrzyjazdu) {
        this.nazwa = nazwa;
        this.stacjaKon = stacjaKon;
        this.typ = typ;
        this.numer = numer;
        ConnectionModel.stacje = stacje;
        this.stacjeList=stacjeList;
        this.stacja = stacja;
        this.godzinaOdjazdu = godzinaOdjazdu;
        this.godzinaPrzyjazdu = godzinaPrzyjazdu;
        expanded = false;
    }
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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
    public static class GodzinaOdjazduComparator implements Comparator<ConnectionModel> {
        @Override
        public int compare(ConnectionModel connection1, ConnectionModel connection2) {
            String godzinaOdjazdu1 = connection1.getGodzinaOdjazdu();
            String godzinaOdjazdu2 = connection2.getGodzinaOdjazdu();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date date1 = sdf.parse(godzinaOdjazdu1);
                Date date2 = sdf.parse(godzinaOdjazdu2);

                assert date1 != null;
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }


    public static Map<String, Map<String, String>> getStacje() {
        return stacje;
    }

    public void setStacje(Map<String, Map<String, String>> stacje) {
        ConnectionModel.stacje = stacje;
    }
}
