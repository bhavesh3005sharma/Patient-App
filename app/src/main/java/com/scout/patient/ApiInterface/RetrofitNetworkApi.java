package com.scout.patient.ApiInterface;

import com.scout.patient.Models.ModelPatientInfo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitNetworkApi {

    @GET("rest/getPatientInfo")
    Call<ModelPatientInfo> getPatientInfo();

}
