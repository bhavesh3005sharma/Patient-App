package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelRequestId implements Serializable {

    @SerializedName("$oid")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
