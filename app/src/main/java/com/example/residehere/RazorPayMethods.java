package com.example.residehere;

import com.example.residehere.ModelQueryAutocomplete.MainList;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RazorPayMethods {

    @POST("generateOrderId")
    Call<String> generateOrderId(@Body int amount);
}
