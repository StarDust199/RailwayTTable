package com.example.railwayttable.Response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Connection implements Parcelable {
    private String startStation;
    private String endStation;
    private String departureTime;
    private String arrivalTime;


    protected Connection(Parcel in) {
        startStation = in.readString();
        endStation = in.readString();
        departureTime = in.readString();
        arrivalTime = in.readString();
    }

    public static final Creator<Connection> CREATOR = new Creator<Connection>() {
        @Override
        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(startStation);
        dest.writeString(endStation);
        dest.writeString(departureTime);
        dest.writeString(arrivalTime);
    }
}
