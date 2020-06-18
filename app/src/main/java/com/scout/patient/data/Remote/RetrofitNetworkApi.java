package com.scout.patient.data.Remote;

import com.scout.patient.data.Models.ModelPatientInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitNetworkApi {

    @GET("getPatientInfo/incoming_webhook/patientInfo")
    Call<ModelPatientInfo> getPatientInfo(String  email);

    @POST("rest/getPatientInfo")
    Call<String> registerPatient(String name,String email,String phoneNo,String password,String address,String dob,String previousDisease,String weight,String bloodGrp);

}
