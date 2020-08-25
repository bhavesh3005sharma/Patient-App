package com.scout.patient.ui.Appointments;

import android.content.Context;
import android.util.Log;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Models.ModelRequestId;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Retrofit.ApiService;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    Contract.Presenter presenter;
    RetrofitNetworkApi networkApi;

    public Model(Contract.Presenter presenter) { this.presenter = presenter; }

    @Override
    public void getAppointmentsIdsList(Context context) {
        networkApi = ApiService.getAPIService();
        String patientId = getPatientId(context);
        networkApi.getAppointmentsIdsList(patientId).enqueue(new Callback<ModelPatientInfo>() {
            @Override
            public void onResponse(Call<ModelPatientInfo> call, Response<ModelPatientInfo> response) {
                if (response.isSuccessful() && response.code()==200){
                    if (response.body().getAppointmentIdsList()!=null)
                        presenter.onSuccessIdsList(response.body().getAppointmentIdsList());
                    else {
                        // user have no appointments yet.
                        presenter.onError("");
                    }
                }
                else presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ModelPatientInfo> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }

    @Override
    public void getAppointmentsList(ArrayList<ModelRequestId> appointmentsIdsList, int startingIndex) {
        networkApi = ApiService.getAPIService();
        ArrayList<ModelAppointment> appointmentArrayList = new ArrayList<>();
        final int[] maxIndex = {10 + startingIndex};
        if(appointmentsIdsList.size()< maxIndex[0])
            maxIndex[0] = appointmentsIdsList.size();

        if(appointmentsIdsList.size()==0)
            presenter.onError("");

        for(int i = startingIndex; i< maxIndex[0]; i++){
            networkApi.getAppointmentsDetails(appointmentsIdsList.get(i).getId()).enqueue(new Callback<ModelAppointment>() {
                @Override
                public void onResponse(Call<ModelAppointment> call, Response<ModelAppointment> response) {
                    if(response.isSuccessful() && response.code()==200){
                        appointmentArrayList.add(response.body());

                        if(appointmentArrayList.size() == maxIndex[0] - startingIndex){
                            int newStartingIndex;
                            if (maxIndex[0]<appointmentsIdsList.size())
                                newStartingIndex = maxIndex[0];
                            else
                                newStartingIndex = -1;
                            presenter.onSuccessAppointmentsList(appointmentArrayList, newStartingIndex);
                        }
                    }
                    else {
                        presenter.onError(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<ModelAppointment> call, Throwable t) {
                    presenter.onError(t.getMessage());
                }
            });
        }
    }

    @Override
    public void getDoctorProfileData(String id, ModelAppointment appointment) {
        networkApi = ApiService.getAPIService();
        networkApi.getDoctorInfo(null,id).enqueue(new Callback<ModelDoctorInfo>() {
            @Override
            public void onResponse(Call<ModelDoctorInfo> call, Response<ModelDoctorInfo> response) {
                if (response.isSuccessful() && response.code()==200)
                    presenter.onSuccessDoctorDetails(response.body(),appointment);
                else presenter.onError(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ModelDoctorInfo> call, Throwable t) {
                presenter.onError(t.getMessage());
            }
        });
    }

    private String getPatientId(Context context) {
        return SharedPref.getLoginUserData(context).getPatientId().getId();
    }
}
