package com.example.railwayttable.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = " https://wbnet-demo.pkpik.pl:444/admapi/search/travelstops/name"; // Zmień to na adres URL swojego REST API
    private static Retrofit retrofit;
    private static RetrofitClient retrofitClient;
    private RetrofitClient()  {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    }

    private static synchronized RetrofitClient getInstance(){
        if (retrofitClient==null){
            retrofitClient=new RetrofitClient();
        }
      return retrofitClient;
    }
}