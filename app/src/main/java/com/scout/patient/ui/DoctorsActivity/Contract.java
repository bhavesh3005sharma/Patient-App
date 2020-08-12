package com.scout.patient.ui.DoctorsActivity;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.Models.ModelRequestId;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void notifyAdapter();

        void setErrorUi(String message);

        void updateSuccessUi(ArrayList<ModelKeyData> data);
    }
    interface  Presenter{

        void loadDoctorsList();

        void onError(String message);

        void onSuccess(ArrayList<ModelKeyData> body);

        void loadDoctorsList(ArrayList<ModelRequestId> listOfDoctors);
    }

    interface  Model{

        void getDoctorsList();

        void getDoctorsList(ArrayList<ModelRequestId> listOfDoctorsIds);
    }
}
