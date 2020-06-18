package com.scout.patient.data.Models;

import com.google.gson.annotations.SerializedName;

public class ModelAmbulanceBooking {
    @SerializedName("AmbulanceId")
    String AmbulanceId;
    @SerializedName("PatientAddress")
    String PatientAddress;
    @SerializedName("PatientPhone")
    String PatientPhone;
    @SerializedName("BookingStatus")
    String BookingStatus;
    @SerializedName("BookingTime")
    String BookingTime;
    @SerializedName("JourneyStatus")
    String JourneyStatus;
    @SerializedName("PatientId")
    String PatientId;

    public String getAmbulanceId() {
        return AmbulanceId;
    }

    public void setAmbulanceId(String ambulanceId) {
        AmbulanceId = ambulanceId;
    }

    public String getPatientAddress() {
        return PatientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        PatientAddress = patientAddress;
    }

    public String getPatientPhone() {
        return PatientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        PatientPhone = patientPhone;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }

    public String getJourneyStatus() {
        return JourneyStatus;
    }

    public void setJourneyStatus(String journeyStatus) {
        JourneyStatus = journeyStatus;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }
}
