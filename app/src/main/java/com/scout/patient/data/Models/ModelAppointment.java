package com.scout.patient.data.Models;

import com.google.gson.annotations.SerializedName;

public class ModelAppointment {
    @SerializedName("_id")
    ModelRequestId AppointmentId;
    @SerializedName("AppointmentDate")
    String AppointmentDate;
    @SerializedName("patient_name")
    String patientName;
    @SerializedName("hospital_name")
    String hospitalName;
    @SerializedName("doctor_name")
    String doctorName;
    @SerializedName("age")
    String Age;
    @SerializedName("Disease")
    String Disease;
    @SerializedName("Status")
    String Status;
    @SerializedName("SerialNumber")
    String SerialNumber;
    @SerializedName("PatientId")
    ModelRequestId PatientId;
    @SerializedName("DoctorId")
    ModelRequestId DoctorId;

    @SerializedName("PrescriptionId")
    ModelRequestId PrescriptionId;

    public ModelAppointment(String patientName, String doctorName, String hospitalName, String disease, String age, String date ,String status, String serialNumber, ModelRequestId patientId, ModelRequestId doctorId, ModelRequestId prescriptionId) {
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
        this.PrescriptionId = prescriptionId;
    }

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

    public ModelRequestId getPatientId() {
        return PatientId;
    }

    public void setPatientId(ModelRequestId patientId) {
        PatientId = patientId;
    }

    public ModelRequestId getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(ModelRequestId doctorId) {
        DoctorId = doctorId;
    }

    public ModelRequestId getPrescriptionId() {
        return PrescriptionId;
    }

    public void setPrescriptionId(ModelRequestId prescriptionId) {
        PrescriptionId = prescriptionId;
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
}
