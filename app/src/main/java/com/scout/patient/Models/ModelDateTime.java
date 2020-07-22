package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelDateTime implements Serializable {
    @SerializedName("date")
    String date;
    @SerializedName("unavailableTimes")
    ArrayList<String> unavailableTimes;

    public ModelDateTime(String date, ArrayList<String> unavailableTimes) {
        this.date = date;
        this.unavailableTimes = unavailableTimes;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getUnavailableTimes() {
        return unavailableTimes;
    }
}
