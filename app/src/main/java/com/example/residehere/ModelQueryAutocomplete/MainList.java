package com.example.residehere.ModelQueryAutocomplete;

import java.util.ArrayList;

public class MainList {
    String status;
    ArrayList<ListClass> predictions;

    public String getStatus() {
        return status;
    }

    public ArrayList<ListClass> getPredictions() {
        return predictions;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPredictions(ArrayList<ListClass> predictions) {
        this.predictions = predictions;
    }
}
