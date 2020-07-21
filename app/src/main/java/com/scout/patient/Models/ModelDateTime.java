package com.scout.patient.Models;

import java.util.ArrayList;

public class ModelDateTime {
    String date;
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
