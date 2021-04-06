package com.scout.patient.ui.DoctorsActivity;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.Models.ModelRequestId;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Retrofit.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    private Contract.Presenter presenter;
    private RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelKeyData>> callGetDoctorsList;

    public Model(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getDoctorsList(String startingValue, int noOfItems) {
        networkApi = ApiService.getAPIService();
        if (callGetDoctorsList == null || callGetDoctorsList.isExecuted()) {
            callGetDoctorsList = networkApi.getDoctorsList(startingValue,noOfItems);
            callGetDoctorsList.enqueue(new Callback<ArrayList<ModelKeyData>>() {
                @Override
                public void onResponse(Call<ArrayList<ModelKeyData>> call, Response<ArrayList<ModelKeyData>> response) {
                    if (response.isSuccessful() && response.body()!=null){
                        presenter.onSuccess(response.body());
                    }else
                        presenter.onError(response.errorBody().toString());
                }

                @Override
                public void onFailure(Call<ArrayList<ModelKeyData>> call, Throwable t) {
                    presenter.onError(t.getMessage());
                }
            });
        }
    }

    @Override
    public void getDoctorsList(ArrayList<ModelRequestId> listOfDoctorsIds, int startingIndex) {
        networkApi = ApiService.getAPIService();
        ArrayList<ModelKeyData> doctorInfoArrayList = new ArrayList<>();

        if (listOfDoctorsIds==null || listOfDoctorsIds.isEmpty() ||  startingIndex>=listOfDoctorsIds.size())
            presenter.onSuccess(doctorInfoArrayList);
        else {
            final int[] maxIndex = {8 + startingIndex};
            if (listOfDoctorsIds.size() < maxIndex[0])
                maxIndex[0] = listOfDoctorsIds.size();

            for (int i = startingIndex; i < maxIndex[0]; i++) {
                    networkApi.getShortDoctorInfo(null,listOfDoctorsIds.get(i).getId()).enqueue(new Callback<ModelKeyData>() {
                        @Override
                        public void onResponse(Call<ModelKeyData> call, Response<ModelKeyData> response) {
                            if (response.isSuccessful() && response.code() == 200)
                                doctorInfoArrayList.add(response.body());

                            if (doctorInfoArrayList.size()==maxIndex[0]-startingIndex)
                                presenter.onSuccess(doctorInfoArrayList);
                        }

                        @Override
                        public void onFailure(Call<ModelKeyData> call, Throwable t) {
                            presenter.onError(t.getMessage());
                        }
                    });
            }
        }
    }
}
