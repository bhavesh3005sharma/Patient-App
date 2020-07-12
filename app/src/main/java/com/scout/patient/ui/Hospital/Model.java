package com.scout.patient.ui.Hospital;

import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    Contract.Presenter presenter;
    RetrofitNetworkApi networkApi;

    public Model(HospitalsPresenter hospitalsPresenter) {
        this.presenter = hospitalsPresenter;
    }

    @Override
    public void loadHospitalsList() {
        networkApi = ApiService.getAPIService();
        networkApi.getHospitalsList().enqueue(new Callback<ArrayList<ModelHospitalInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelHospitalInfo>> call, Response<ArrayList<ModelHospitalInfo>> response) {
                if (response.isSuccessful() || response.code()==200)
                    presenter.onSuccess(response.body());
                else
                    presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelHospitalInfo>> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }
}
