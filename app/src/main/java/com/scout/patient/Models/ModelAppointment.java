package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelAppointment {
    @SerializedName("_id")
    String AppointmentId;
    @SerializedName("AppointmentDate")
    String AppointmentDate;
    @SerializedName("Disease")
    String Disease;
    @SerializedName("Status")
    String Status;
    @SerializedName("SerialNumber")
    String SerialNumber;
    @SerializedName("PatientId")
    String PatientId;
    @SerializedName("DoctorId")
    String DoctorId;

    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
    }
}
