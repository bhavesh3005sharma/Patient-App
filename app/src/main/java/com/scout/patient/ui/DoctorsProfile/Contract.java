package com.scout.patient.ui.DoctorsProfile;

import java.util.ArrayList;

public class Contract {
    interface  View{

    }
    interface  Presenter{

        String getAvailabilityType(String availabilityType, ArrayList<String> doctorAvailability, DoctorsProfileActivity doctorsProfileActivity);

        String getAvailabilityTime(String avgCheckupTime, ArrayList<String> doctorAvailabilityTime, DoctorsProfileActivity doctorsProfileActivity);
    }
}
