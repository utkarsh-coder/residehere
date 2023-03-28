package com.example.residehere;

import com.example.residehere.ModelGeocoding.Results;
import com.example.residehere.ModelQueryAutocomplete.MainList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("place/queryautocomplete/json")
    Call<MainList> getPlace(@Query("input") String text,
                            @Query("key") String key);

    @GET("geocode/json")
    Call<Results> getLocation(@Query("address") String text,
                           @Query("key") String key);
}
