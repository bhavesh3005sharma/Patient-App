package com.scout.patient.data.Models;

import com.google.gson.annotations.SerializedName;

public class ModelRequestId {

    @SerializedName("$oid")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
