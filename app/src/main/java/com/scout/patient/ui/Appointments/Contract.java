package com.scout.patient.ui.Appointments;

import android.content.Context;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.Models.ModelRequestId;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void onError(String message);

        void addDataToList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex);

        void updateDoctorDetails(ModelDoctorInfo doctorInfo, ModelAppointment position);
    }
    interface  Presenter{

        void loadAppointments(int startingIndex);

        void loadAppointmentsIdsList(Context context);

        void onError(String message);

        void onSuccessIdsList(ArrayList<ModelRequestId> appointmentIdsList);

        void onSuccessAppointmentsList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex);

        void getDoctorProfileData(String id, ModelAppointment position);

        void onSuccessDoctorDetails(ModelDoctorInfo body, ModelAppointment position);
    }

    interface Model{

        void getAppointmentsIdsList(Context context);

        void getAppointmentsList(ArrayList<ModelRequestId> appointmentsIdsList, int startingIndex);

        void getDoctorProfileData(String id, ModelAppointment position);
    }
}
