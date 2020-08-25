package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelDoctorInfo implements Serializable {
    @SerializedName("_id")
    ModelRequestId doctorId;
    @SerializedName("name")
    String name;
    @SerializedName("schedule")
    String schedule;
    @SerializedName("url")
    String url;
    @SerializedName("hospital_name")
    String hospitalName;
    @SerializedName("email")
    String email;
    @SerializedName("phone_no")
    String phone_no;
    @SerializedName("address")
    String address;
    @SerializedName("department")
    String department;
    @SerializedName("career_history")
    String careerHistory;
    @SerializedName("learning_history")
    String learningHistory;
    @SerializedName("average_checkup_time")
    String AvgCheckupTime;
    @SerializedName("availability_type")
    String AvailabilityType;
    @SerializedName("doctor_availability")
    ArrayList<String> DoctorAvailability;
    @SerializedName("doctor_availability_time")
    ArrayList<String> DoctorAvailabilityTime;
    @SerializedName("hospital_id")
    ModelRequestId hospitalObjectId;
    @SerializedName("hospital_string_id")
    String hospitalStringId;
    @SerializedName("unAvailableDates")
    ArrayList<ModelDateTime> unAvailableDates;

    public ModelDoctorInfo(String name, String email, String phone_no, String address, String department, String careerHistory, String learningHistory, String avgCheckupTime, String availabilityType, ArrayList<String> doctorAvailability, ArrayList<String> doctorAvailabilityTime, String hospitalId) {
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
        this.address = address;
        this.department = department;
        this.careerHistory = careerHistory;
        this.learningHistory = learningHistory;
        AvgCheckupTime = avgCheckupTime;
        AvailabilityType = availabilityType;
        DoctorAvailability = doctorAvailability;
        DoctorAvailabilityTime = doctorAvailabilityTime;
        this.hospitalStringId = hospitalId;
    }

    public ArrayList<ModelDateTime> getUnAvailableDates() { return unAvailableDates; }

    public ModelDoctorInfo(String name, String email, String phone_no, String address, String department, String careerHistory, String learningHistory, String avgCheckupTime, String availabilityType, ArrayList<String> doctorAvailability, ArrayList<String> doctorAvailabilityTime) {
        this.name = name;
        this.email = email;
        this.phone_no = phone_no;
        this.address = address;
        this.department = department;
        this.careerHistory = careerHistory;
        this.learningHistory = learningHistory;
        AvgCheckupTime = avgCheckupTime;
        AvailabilityType = availabilityType;
        DoctorAvailability = doctorAvailability;
        DoctorAvailabilityTime = doctorAvailabilityTime;
    }

    public String getSchedule() { return schedule; }

    public String getUrl() {
        return url;
    }

    public String getHospitalStringId() {
        return hospitalStringId;
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

    public String getHospitalName() { return hospitalName; }

    public void setName(String name) {
        this.name = name;
    }

    public ModelRequestId getHospitalObjectId() {
        return hospitalObjectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getAvgCheckupTime() {
        return AvgCheckupTime;
    }

    public String getAvailabilityType() {
        return AvailabilityType;
    }

    public ArrayList<String> getDoctorAvailability() {
        return DoctorAvailability;
    }

    public ArrayList<String> getDoctorAvailabilityTime() {
        return DoctorAvailabilityTime;
    }
}
