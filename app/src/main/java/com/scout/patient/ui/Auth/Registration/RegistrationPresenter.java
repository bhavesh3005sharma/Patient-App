package com.scout.patient.ui.Auth.Registration;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.Utilities.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationPresenter implements Contract.Presenter {
    Contract.View mainView;

    public RegistrationPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void registerPatient(Context context, ProgressBar progressBar, Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful())
                    HelperClass.toast(context, response.body());
                else
                    HelperClass.toast(context,response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });
    }
}
