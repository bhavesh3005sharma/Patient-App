package com.scout.patient.ui.DoctorsActivity;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.data.Models.ModelDoctorInfo;

import java.util.ArrayList;

import retrofit2.Call;

public class Contract {
    interface  View{

        void notifyAdapter();
    }
    interface  Presenter{

        void loadDoctorslist(Context context, Call<ArrayList<ModelDoctorInfo>> call, ProgressBar progressBar);
    }
}
