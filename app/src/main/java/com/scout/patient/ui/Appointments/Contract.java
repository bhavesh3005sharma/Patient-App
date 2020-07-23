package com.scout.patient.ui.Appointments;

import android.content.Context;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelRequestId;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void notifyAdapter();

        void onError(String message);

        void addDataToList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex);
    }
    interface  Presenter{

        void loadAppointments(int startingIndex);

        void loadAppointmentsIdsList(Context context);

        void onError(String message);

        void onSuccessIdsList(ArrayList<ModelRequestId> appointmentIdsList);

        void onSuccessAppointmentsList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex);
    }

    interface Model{

        void getAppointmentsIdsList(Context context);

        void getAppointmentsList(ArrayList<ModelRequestId> appointmentsIdsList, int startingIndex);
    }
}
