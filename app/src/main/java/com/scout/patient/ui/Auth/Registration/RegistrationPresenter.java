package com.scout.patient.ui.Auth.Registration;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.scout.patient.Utilities.HelperClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPresenter implements Contract.Presenter {
    Contract.View mainView;

    public RegistrationPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void registerPatient(Context context, ProgressBar progressBar, Call<ResponseBody> call) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful()) {
                    if (response.code()==200){
                        //success
                        HelperClass.toast(context,"Registered Successfully Verification Email Sent Please Verify First");
                    }
                }
                else {
                    HelperClass.toast(context, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });
    }
}
