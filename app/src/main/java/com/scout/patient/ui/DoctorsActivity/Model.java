package com.scout.patient.ui.DoctorsActivity;

import com.google.android.gms.common.api.Api;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelRequestId;
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

    @Override
    public void getDoctorsList(ArrayList<ModelRequestId> listOfDoctorsIds) {
        ArrayList<ModelDoctorInfo> doctorInfoArrayList = new ArrayList<>();
        final Boolean[] isError = {false};
        networkApi = ApiService.getAPIService();

        if (listOfDoctorsIds.isEmpty())
            presenter.onSuccess(new ArrayList<>());
        else
            for (ModelRequestId id : listOfDoctorsIds){
            networkApi.getDoctorInfo(null,id.getId()).enqueue(new Callback<ModelDoctorInfo>() {
                @Override
                public void onResponse(Call<ModelDoctorInfo> call, Response<ModelDoctorInfo> response) {
                    if (response.isSuccessful() && response.code() == 200)
                        doctorInfoArrayList.add(response.body());

                    if (doctorInfoArrayList.size()==listOfDoctorsIds.size())
                        presenter.onSuccess(doctorInfoArrayList);
                }

                @Override
                public void onFailure(Call<ModelDoctorInfo> call, Throwable t) {
                    if (!isError[0]) {
                        presenter.onError(t.getMessage());
                        isError[0] = true;
                    }
                }
            });
        }
    }
}
