package com.scout.patient.ui.Hospital;

import com.scout.patient.Models.ModelKeyData;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void notifyAdapter();

        void setErrorUi(String message);

        void updateSuccessUi(ArrayList<ModelKeyData> data);
    }
    interface  Presenter{

        void getHospitalsList(String o, int i);

        void onError(String message);

        void onSuccess(ArrayList<ModelKeyData> body);
    }

    interface  Model{

        void loadHospitalsList(String startValue, int noOfItems);
    }
}
