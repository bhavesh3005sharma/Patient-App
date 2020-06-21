package com.scout.patient.data.Models;

import com.google.gson.annotations.SerializedName;

public class ModelPatientInfo {
    @SerializedName("_id")
    ModelRequestId patientId;
    @SerializedName("email")
    String email;
    @SerializedName("weight")
    String weight;

    public ModelPatientInfo(String email, String weight, String name, String DOB, String phoneNo, String address, String bloodGroup, String medicalHistory) {
        this.email = email;
        this.weight = weight;
        this.name = name;
        this.DOB = DOB;
        this.phoneNo = phoneNo;
        this.address = address;
        this.bloodGroup = bloodGroup;
        MedicalHistory = medicalHistory;
    }

    @SerializedName("name")
    String name;
    @SerializedName("dob")
    String DOB;
    @SerializedName("phone_no")
    String phoneNo;
    @SerializedName("address")
    String address;
    @SerializedName("blood_group")
    String bloodGroup;
    @SerializedName("medical_history")
    String MedicalHistory;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalHistory() {
        return MedicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        MedicalHistory = medicalHistory;
    }

    public ModelRequestId getPatientId() {
        return patientId;
    }

    public void setPatientId(ModelRequestId patientId) {
        this.patientId = patientId;
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

}
