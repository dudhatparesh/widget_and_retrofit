package com.enggdream.widgetapp;

import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Paresh on 4/13/2017.
 */

public interface ApiService {

    String ENDPOINT = "https://api.connectbtc.com/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
            .build();

    @GET("/stats/hashrate/graph")
    Call<List<Data>> getData();
}
