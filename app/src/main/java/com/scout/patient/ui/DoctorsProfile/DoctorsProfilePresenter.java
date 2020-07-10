package com.scout.patient.ui.DoctorsProfile;

import com.scout.patient.R;
import com.scout.patient.ui.DoctorsProfile.Contract;

import java.util.ArrayList;

public class DoctorsProfilePresenter implements Contract.Presenter {
    Contract.View mainView;

    public DoctorsProfilePresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public String getAvailabilityType(String availabilityType_, ArrayList<String> doctorAvailability, DoctorsProfileActivity context) {
        String availabilityType=availabilityType_;
        if (availabilityType.equals(context.getString(R.string.weekly)))
            availabilityType+="\nWeekdays : ";
        if (availabilityType.equals(context.getString(R.string.monthly)))
            availabilityType+="\nDates : ";
        for(int i=0; i<doctorAvailability.size();i++){
            availabilityType+=doctorAvailability.get(i);
            if (i!=doctorAvailability.size()-1)
                availabilityType+=", ";
        }
        return availabilityType;
    }

    @Override
    public String getAvailabilityTime(String avgCheckupTime, ArrayList<String> doctorAvailabilityTime, DoctorsProfileActivity context) {
        String availabilityTime= context.getString(R.string.checkup_time)+" : "+avgCheckupTime+"\n";
        availabilityTime+=context.getString(R.string.availability_time)+" : ";
        for(int i=0; i<doctorAvailabilityTime.size();i++) {
            availabilityTime += doctorAvailabilityTime.get(i);
            if (i != doctorAvailabilityTime.size() - 1)
                availabilityTime += ", ";
        }
        return availabilityTime;
    }
}
