package com.scout.patient.data.Remote;

import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.data.Models.ModelPatientInfo;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitNetworkApi {

    @GET("Patient/incoming_webhook/getPatientInfo")
    Call<ModelPatientInfo> getPatientInfo(@Query("email") String  email, @Query("patient_id") String id);

    @POST("Patient/incoming_webhook/registerPatient")
    Call<ResponseBody> registerPatient(@Body ModelPatientInfo patientInfo);

    @GET("Doctor/incoming_webhook/getAllDoctors")
    Call<ArrayList<ModelDoctorInfo>> getDoctorsList();

    @GET("Patient/incoming_webhook/getAppointments")
    Call<ArrayList<ModelAppointment>> getAppointments(@Query("patient_id") String patientId);

    @POST("Patient/incoming_webhook/bookAppointment")
    Call<ResponseBody> bookAppointment(@Body ModelAppointment appointment);
}
