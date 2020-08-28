package com.scout.patient.ui.Profile;

import com.google.android.gms.common.api.Api;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Retrofit.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    Contract.Presenter presenter;

    public Model(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getUserData(String id) {
        ApiService.getAPIService().getPatientInfo(null,id).enqueue(new Callback<ModelPatientInfo>() {
            @Override
            public void onResponse(Call<ModelPatientInfo> call, Response<ModelPatientInfo> response) {
                if (response.isSuccessful() && response.code()==200)
                    presenter.onSuccess(response.body());
                else
                    presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ModelPatientInfo> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }

    @Override
    public void sendPasswordUpdateNotification(String email) {
        ApiService.getAPIService().sendPasswordUpdateNotification(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code()==200){
                    // Password Update Notification Sent.
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
