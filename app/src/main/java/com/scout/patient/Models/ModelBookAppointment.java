package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelBookAppointment implements Serializable {
    @SerializedName("_id")
    ModelRequestId AppointmentId;
    @SerializedName("appointment_date")
    String AppointmentDate;
    @SerializedName("patient_name")
    String patientName;
    @SerializedName("hospital_name")
    String hospitalName;
    @SerializedName("doctor_name")
    String doctorName;
    @SerializedName("age")
    String Age;
    @SerializedName("disease")
    String Disease;
    @SerializedName("status")
    String Status;
    @SerializedName("serial_no")
    String SerialNumber;
    @SerializedName("patient_id")
    String PatientId;
    @SerializedName("doctor_id")
    String DoctorId;
    @SerializedName("hospital_id")
    String HospitalId;
    @SerializedName("appointment_time")
    String selectedTime;
    @SerializedName("thresholdLimit")
    long thresholdLimit;


    public ModelBookAppointment(String patientName, String doctorName, String hospitalName, String disease, String age, String date , String status, String serialNumber, String patientId, String doctorId, String hospitalId, String selectedTime, long thresholdLimit) {
        this.AppointmentDate = date;
        this.patientName = patientName;
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
        this.Age = age;
        this.Disease = disease;
        this.Status = status;
        this.SerialNumber = serialNumber;
        this.PatientId = patientId;
        this.DoctorId = doctorId;
        this.HospitalId = hospitalId;
        this.selectedTime = selectedTime;
        this.thresholdLimit = thresholdLimit;
    }

    public ModelBookAppointment(String patientName, String disease, String age) {
        this.patientName = patientName;
        this.Age = age;
        this.Disease = disease;
    }

    public String getSelectedTime() { return selectedTime; }

    public ModelRequestId getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(ModelRequestId appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
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

    public String getHospitalId() { return HospitalId; }

    public void setHospitalId(String hospitalId) { HospitalId = hospitalId; }

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
}
