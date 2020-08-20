package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.Models.ModelPatientInfo;

import retrofit2.Call;

public class Contract {
    interface View {

        void openHomeActivity();

        void signOut();
    }

    interface Presenter {

        void getPatientInfo(Context context, ProgressBar progressBar, Call<ModelPatientInfo> call, String password);

        void saveFcmToken(String trim, String token);
    }
}
