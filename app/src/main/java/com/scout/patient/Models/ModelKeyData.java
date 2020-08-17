package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelKeyData implements Serializable {
    @SerializedName("url")
    String imageUrl;
    @SerializedName("name")
    String Name;
    @SerializedName("_id")
    ModelRequestId id;
    boolean isHospital = false;
    String strId;

    public ModelKeyData(String name, String imageUrl,  String id, boolean isHospital) {
        this.imageUrl = imageUrl;
        this.Name = name;
        this.strId = id;
        this.isHospital = isHospital;
    }

    public ModelKeyData(String name, String imageUrl,  String id) {
        this.imageUrl = imageUrl;
        this.Name = name;
        this.strId = id;
    }

    public String getStrId() {
        return strId;
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
