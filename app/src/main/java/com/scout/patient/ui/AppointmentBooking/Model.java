package com.scout.patient.ui.AppointmentBooking;

import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDateTime;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ResponseMessage;
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
    public void getUnavailableDates(ModelDoctorInfo doctorProfileInfo) {
        networkApi = ApiService.getAPIService();
        networkApi.getUnavailableDates(doctorProfileInfo.getDoctorId().getId()).enqueue(new Callback<ModelDoctorInfo>() {
            @Override
            public void onResponse(Call<ModelDoctorInfo> call, Response<ModelDoctorInfo> response) {
                if(response.isSuccessful() && response.code()==200){
                    ArrayList<ModelDateTime> unavailableDates = new ArrayList<>(), CompletelyUnavailableDates, PartiallyUnavailableDates;
                    CompletelyUnavailableDates = new ArrayList<>();
                    PartiallyUnavailableDates = new ArrayList<>();
                    if (response.body().getUnAvailableDates()!=null) {
                        unavailableDates = response.body().getUnAvailableDates();
                        for (int i = 0; i < unavailableDates.size(); i++) {
                            if (unavailableDates.get(i).getUnavailableTimes().size() < doctorProfileInfo.getDoctorAvailabilityTime().size())
                                PartiallyUnavailableDates.add(unavailableDates.get(i));
                            else
                                CompletelyUnavailableDates.add(unavailableDates.get(i));
                        }
                        presenter.setUpDatePicker(unavailableDates, CompletelyUnavailableDates, PartiallyUnavailableDates, doctorProfileInfo);
                    }
                    else
                        presenter.setUpDatePicker(unavailableDates, CompletelyUnavailableDates, PartiallyUnavailableDates, doctorProfileInfo);
                }
                else {
                    presenter.OnResponse(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ModelDoctorInfo> call, Throwable t) {
                presenter.OnResponse(t.getMessage());
            }
        });
    }

    @Override
    public void bookAppointment(ModelBookAppointment appointment) {
        networkApi = ApiService.getAPIService();
        networkApi.bookAppointment(appointment).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful() && response.code()==200) {
                    if (response.body()!=null)
                        presenter.OnResponse(response.body().getMessage());
                    else
                        presenter.OnResponse("Appointment Booked");
                }
                else {
                    presenter.OnResponse(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                presenter.OnResponse(t.getMessage());
            }
        });
    }
}
