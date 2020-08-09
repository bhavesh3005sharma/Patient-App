package com.scout.patient.ui.Profile;

import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Retrofit.ApiService;

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
}
