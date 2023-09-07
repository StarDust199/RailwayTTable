package com.example.railwayttable.Service;

import com.example.railwayttable.Response.Connection;
import com.example.railwayttable.Response.TravelStop;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST ( "/travelstops/name")
    Call<List<TravelStop>> getStation(
            @Query("name") String name,
            @Query("travelStopId") String travelStopId,
            @Header("Authorization") String authHeader
    );

    @POST("/travel")
    Call<List<Connection>> getTrip(
            @Query("startId") String startId,
            @Query("destId") String destId,
            @Header("Authorization") String authHeader
    );
}
