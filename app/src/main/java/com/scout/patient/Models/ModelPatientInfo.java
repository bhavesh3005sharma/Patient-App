package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelPatientInfo {
    @SerializedName("_id")
    String patientId;
    @SerializedName("username")
    String userName;
    @SerializedName("name")
    String name;
    @SerializedName("dob")
    String DOB;
    @SerializedName("phone")
    String phoneNo;
    @SerializedName("address")
    String address;
    @SerializedName("salt")
    String salt;
    @SerializedName("hash")
    String hash;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
