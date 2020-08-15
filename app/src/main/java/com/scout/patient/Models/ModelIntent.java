package com.scout.patient.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelIntent implements Serializable {
    private ModelBookAppointment bookAppointmentData;
    private ModelDoctorInfo doctorProfileInfo;
    private ArrayList<ModelRequestId> listOfDoctors;
    private Boolean isIntentFromHospital;
    private String id,name;

    public ModelBookAppointment getBookAppointmentData() {
        return bookAppointmentData;
    }

    public void setBookAppointmentData(ModelBookAppointment bookAppointmentData) {
        this.bookAppointmentData = bookAppointmentData;
    }

    public ModelDoctorInfo getDoctorProfileInfo() {
        return doctorProfileInfo;
    }

    public void setDoctorProfileInfo(ModelDoctorInfo doctorProfileInfo) {
        this.doctorProfileInfo = doctorProfileInfo;
    }

    public ArrayList<ModelRequestId> getListOfDoctors() {
        return listOfDoctors;
    }

    public void setListOfDoctors(ArrayList<ModelRequestId> listOfDoctors) {
        this.listOfDoctors = listOfDoctors;
    }

    public Boolean getIntentFromHospital() {
        return isIntentFromHospital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIntentFromHospital() {
        return isIntentFromHospital;
    }

    public void setIntentFromHospital(Boolean intentFromHospital) {
        isIntentFromHospital = intentFromHospital;
    }
}
