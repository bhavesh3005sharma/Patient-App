package com.scout.patient.Models;

import com.google.gson.annotations.SerializedName;

public class ModelDepartment {
    @SerializedName("department_Name")
    String departmentName;
    @SerializedName("description")
    String description;

    public ModelDepartment(String departmentName, String description) {
        this.departmentName = departmentName;
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDescription() {
        return description;
    }
}
