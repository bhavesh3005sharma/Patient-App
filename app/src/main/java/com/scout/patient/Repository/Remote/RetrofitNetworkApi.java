package com.scout.patient.Repository.Remote;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDateTime;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Models.ResponseMessage;

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

    @GET("Doctor/incoming_webhook/getDoctorInfo")
    Call<ModelDoctorInfo> getDoctorInfo(@Query("email") String email, @Query("doctor_id") String id);

    @GET("Doctor/incoming_webhook/unavailableDates")
    Call<ModelDoctorInfo> getUnavailableDates(@Query("doctor_id") String id);

    @GET("Hospital/incoming_webhook/getAllHospitals")
    Call<ArrayList<ModelHospitalInfo>> getHospitalsList();

    @GET("Patient/incoming_webhook/getAppointments")
    Call<ArrayList<ModelAppointment>> getAppointments(@Query("patient_id") String patientId);

    @POST("Patient/incoming_webhook/bookAppointment")
    Call<ResponseMessage> bookAppointment(@Body ModelBookAppointment appointment);
}
