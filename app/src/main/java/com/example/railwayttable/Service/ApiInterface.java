package com.example.railwayttable.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("getStation") // Endpoint pobierania informacji o stacjach, dostosuj do swojego API
    Call<ResponseBody> getStation();

    // Metoda POST do wyszukiwania przystanków podróży na podstawie nazwy
    @POST(" https://wbnet-demo.pkpik.pl:444/admapi/search/travelstops/name") // Endpoint wyszukiwania przystanków, dostosuj do swojego API
    Call<ResponseBody> searchTravelStops(@Query("name") String stationName);
}
