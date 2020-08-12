package com.scout.patient.ui.HospitalProfile;

import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalProfileModel implements Contract.Model {
    Contract.Presenter presenter;

    public HospitalProfileModel(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getHospitalDetails(String hospitalId) {
        ApiService.getAPIService().getHospitalInfo(null,hospitalId).enqueue(new Callback<ModelHospitalInfo>() {
            @Override
            public void onResponse(Call<ModelHospitalInfo> call, Response<ModelHospitalInfo> response) {
                if (response.isSuccessful() && response.code()==200)
                    presenter.onSuccess(response.body());
                else
                    presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ModelHospitalInfo> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }
}
