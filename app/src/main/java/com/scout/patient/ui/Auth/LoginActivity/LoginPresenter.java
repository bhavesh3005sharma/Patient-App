package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.scout.patient.PasswordClass;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelPatientInfo;
import com.scout.patient.data.Prefs.SharedPref;

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
                if (response.isSuccessful()){
                    ModelPatientInfo patientInfo = response.body();
                    Log.d("PatientInfo",patientInfo.getName()+"*"+patientInfo.getUserName()+patientInfo.getAddress());
                    checkForCorrectUser(context,progressBar,patientInfo,password);
                }
                else {
                    HelperClass.hideProgressbar(progressBar);
                    HelperClass.toast(context,response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ModelPatientInfo> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });
    }

    private void checkForCorrectUser(Context context, ProgressBar progressBar, ModelPatientInfo patientInfo, String password) {
        String hash = patientInfo.getHash();
        String salt = patientInfo.getSalt();
        if (PasswordClass.isExpectedPassword(password.toCharArray(),salt.getBytes(),hash.getBytes())){
            HelperClass.hideProgressbar(progressBar);
            SharedPref.saveLoginUserData(context,patientInfo);
            mainView.openHomeActivity();
        }
        else
            HelperClass.toast(context,"Invalid Password");
    }
}
