package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.data.Models.ModelPatientInfo;

import retrofit2.Call;

public class Contract {
    interface View {

        void openHomeActivity();
    }

    interface Presenter {

        void getPatientInfo(Context context, ProgressBar progressBar, Call<ModelPatientInfo> call, String password);
    }
}
