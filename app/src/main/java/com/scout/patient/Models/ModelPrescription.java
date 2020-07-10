package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelPrescription {
    @SerializedName("_id")
    String PrescriptionId;
    @SerializedName("Symptoms")
    String Symptoms;
    @SerializedName("Tests")
    String Tests;
    @SerializedName("Medicines")
    String Medicines;
    @SerializedName("Remarks")
    String Remarks;

    public String getPrescriptionId() {
        return PrescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        PrescriptionId = prescriptionId;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }

    public String getMedicines() {
        return Medicines;
    }

    public void setMedicines(String medicines) {
        Medicines = medicines;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
