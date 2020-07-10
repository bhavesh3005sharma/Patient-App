package com.scout.patient.ui.DoctorsActivity;

import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Retrofit.ApiService;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    Contract.Presenter presenter;
    RetrofitNetworkApi networkApi;

    public Model(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getDoctorsList() {
        networkApi = ApiService.getAPIService();
        networkApi.getDoctorsList().enqueue(new Callback<ArrayList<ModelDoctorInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelDoctorInfo>> call, Response<ArrayList<ModelDoctorInfo>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    presenter.onSuccess(response.body());
                }else
                    presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelDoctorInfo>> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }
}
