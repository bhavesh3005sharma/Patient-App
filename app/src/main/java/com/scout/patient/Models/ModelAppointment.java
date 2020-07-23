package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelAppointment {
    @SerializedName("_id")
    ModelRequestId AppointmentId;
    @SerializedName("appointment_date")
    String AppointmentDate;
    @SerializedName("appointment_time")
    String AppointmentTime;
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
    ModelRequestId PatientId;
    @SerializedName("doctor_id")
    ModelRequestId DoctorId;
    @SerializedName("hospital_id")
    ModelRequestId HospitalId;

    public ModelRequestId getAppointmentId() {
        return AppointmentId;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAge() {
        return Age;
    }

    public String getDisease() {
        return Disease;
    }

    public String getStatus() {
        return Status;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public ModelRequestId getPatientId() {
        return PatientId;
    }

    public ModelRequestId getDoctorId() {
        return DoctorId;
    }

    public ModelRequestId getHospitalId() {
        return HospitalId;
    }
}
