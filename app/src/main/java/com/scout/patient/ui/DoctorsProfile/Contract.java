package com.scout.patient.ui.DoctorsProfile;

import com.scout.patient.Models.ModelDoctorInfo;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void updateUi(ModelDoctorInfo doctorInfo);

        void onError(String s);
    }
    interface  Presenter{

        String getAvailabilityType(String availabilityType, ArrayList<String> doctorAvailability, DoctorsProfileActivity doctorsProfileActivity);

        String getAvailabilityTime(String avgCheckupTime, ArrayList<String> doctorAvailabilityTime, DoctorsProfileActivity doctorsProfileActivity);

        void getDoctorDetails(String doctorId);

        void onSuccess(ModelDoctorInfo body);

        void onError(String toString);
    }
    interface  Model{

        void getDoctorDetails(String doctorId);
    }
}
