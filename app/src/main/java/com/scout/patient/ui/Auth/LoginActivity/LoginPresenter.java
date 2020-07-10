package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Repository.Prefs.SharedPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements Contract.Presenter {
    Contract.View mainView;

    public LoginPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void getPatientInfo(Context context, ProgressBar progressBar, Call<ModelPatientInfo> call, String password) {
        call.enqueue(new Callback<ModelPatientInfo>() {
            @Override
            public void onResponse(Call<ModelPatientInfo> call, Response<ModelPatientInfo> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful()){
                    if (response.body()!=null) {
                        ModelPatientInfo patientInfo = response.body();
                        SharedPref.saveLoginUserData(context,patientInfo);
                        mainView.openHomeActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelPatientInfo> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });
    }
}
