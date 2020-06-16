package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelDoctorInfo {
    @SerializedName("_id")
    String doctorId;
    @SerializedName("username")
    String userName;
    @SerializedName("name")
    String name;
    @SerializedName("specialization")
    String specialization;
    @SerializedName("phone")
    String phoneNo;
    @SerializedName("address")
    String address;
    @SerializedName("salt")
    String salt;
    @SerializedName("hash")
    String hash;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
