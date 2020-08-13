package com.scout.patient.Repository.Remote;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Models.ModelKeyData;
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

    @POST("Patient/incoming_webhook/updateProfileData")
    Call<ResponseBody> updateProfileData(@Body ModelPatientInfo patientInfo);

    @GET("Hospital/incoming_webhook/getHospitalInfo")
    Call<ModelHospitalInfo> getHospitalInfo(@Query("email") String email,@Query("hospital_id") String id);

    @GET("Patient/incoming_webhook/updateProfilePic")
    Call<ResponseBody> updateProfilePic(@Query("id") String  id, @Query("url") String url);

    @GET("Doctor/incoming_webhook/getAllDoctors")
    Call<ArrayList<ModelKeyData>> getDoctorsList();

    @GET("Doctor/incoming_webhook/getDoctorInfo")
    Call<ModelDoctorInfo> getDoctorInfo(@Query("email") String email, @Query("doctor_id") String id);

    @GET("Doctor/incoming_webhook/getShortDoctorInfo")
    Call<ModelKeyData> getShortDoctorInfo(@Query("email") String email, @Query("doctor_id") String id);

    @GET("Doctor/incoming_webhook/unavailableDates")
    Call<ModelDoctorInfo> getUnavailableDates(@Query("doctor_id") String id);

    @GET("Hospital/incoming_webhook/getAllHospitals")
    Call<ArrayList<ModelKeyData>> getHospitalsList();

    @GET("Patient/incoming_webhook/getAppointmentsIdsList")
    Call<ModelPatientInfo> getAppointmentsIdsList(@Query("patient_id") String patientId);

    @GET("Hospital/incoming_webhook/AppointmentDetails")
    Call<ModelAppointment> getAppointmentsDetails(@Query("appointment_id") String appointmentId);

    @POST("Patient/incoming_webhook/bookAppointment")
    Call<ResponseMessage> bookAppointment(@Body ModelBookAppointment appointment);
}
