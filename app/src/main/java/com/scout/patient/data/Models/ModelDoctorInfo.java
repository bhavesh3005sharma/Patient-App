package com.scout.patient.data.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelDoctorInfo implements Serializable {
    @SerializedName("_id")
    ModelRequestId doctorId;
    @SerializedName("email")
    String userName;
    @SerializedName("name")
    String name;
    @SerializedName("specialization")
    String specialization;
    @SerializedName("phone_no")
    String phoneNo;
    @SerializedName("address")
    String address;
    @SerializedName("working_places")
    ArrayList<String> workingPlaces;
    @SerializedName("career_history")
    String careerHistory;
    @SerializedName("learning_history")
    String learningHistory;

    public ArrayList<String> getWorkingPlaces() {
        return workingPlaces;
    }

    public void setWorkingPlaces(ArrayList<String> workingPlaces) {
        this.workingPlaces = workingPlaces;
    }

    public String getCareerHistory() {
        return careerHistory;
    }

    public void setCareerHistory(String careerHistory) {
        this.careerHistory = careerHistory;
    }

    public String getLearningHistory() {
        return learningHistory;
    }

    public void setLearningHistory(String learningHistory) {
        this.learningHistory = learningHistory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ModelRequestId getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(ModelRequestId doctorId) {
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
}
