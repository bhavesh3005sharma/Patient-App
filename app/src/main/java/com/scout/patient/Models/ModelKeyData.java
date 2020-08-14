package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelKeyData {
    @SerializedName("url")
    String imageUrl;
    @SerializedName("name")
    String Name;
    @SerializedName("_id")
    ModelRequestId id;
    boolean isHospital = false;

    public ModelKeyData(String imageUrl, String name, ModelRequestId id) {
        this.imageUrl = imageUrl;
        Name = name;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return Name;
    }

    public ModelRequestId getId() {
        return id;
    }

    public void setHospital(boolean hospital) {
        isHospital = hospital;
    }

    public boolean isHospital() {
        return isHospital;
    }
}
