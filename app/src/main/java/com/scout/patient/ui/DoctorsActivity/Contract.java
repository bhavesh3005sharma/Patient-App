package com.scout.patient.ui.DoctorsActivity;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.Models.ModelDoctorInfo;

import java.util.ArrayList;

public class Contract {
    interface  View{

        void notifyAdapter();

        void setErrorUi(String message);

        void updateSuccessUi(ArrayList<ModelDoctorInfo> data);
    }
    interface  Presenter{

        void loadDoctorsList();

        void onError(String message);

        void onSuccess(ArrayList<ModelDoctorInfo> body);
    }

    interface  Model{

        void getDoctorsList();
    }
}
