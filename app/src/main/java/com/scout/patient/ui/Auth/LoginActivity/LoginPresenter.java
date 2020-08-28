package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.gson.internal.$Gson$Preconditions;
import com.scout.patient.Retrofit.ApiService;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Repository.Prefs.SharedPref;

import okhttp3.ResponseBody;
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
                if (progressBar!=null)
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful()){
                    if (response.body()!=null) {
                        ModelPatientInfo patientInfo = response.body();
                        SharedPref.saveLoginUserData(context,patientInfo);
                        mainView.openHomeActivity();
                        Log.d("ResponseData",response.body().getName());
                    }
                    else {
                        HelperClass.toast(context, "User Data Not Found\n May be user Not Registered Yet");
                        mainView.signOut();
                    }
                }else {
                    HelperClass.toast(context, response.errorBody().toString());
                    mainView.signOut();
                }
            }

            @Override
            public void onFailure(Call<ModelPatientInfo> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
                mainView.signOut();
            }
        });
    }

    @Override
    public void saveFcmToken(String email, String token) {
        ApiService.getAPIService().AddFCMToken(email,token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.code()==200)
                            Log.d("Token","Saved Successfully\n"+token);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
