package com.scout.patient.ui.Hospital;

import com.scout.patient.Models.ModelHospitalInfo;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void notifyAdapter();

        void setErrorUi(String message);

        void updateSuccessUi(ArrayList<ModelHospitalInfo> data);
    }
    interface  Presenter{

        void getHospitalsList();

        void onError(String message);

        void onSuccess(ArrayList<ModelHospitalInfo> body);
    }

    interface  Model{

        void loadHospitalsList();
    }
}
