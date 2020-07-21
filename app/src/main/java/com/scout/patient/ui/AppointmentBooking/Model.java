package com.scout.patient.ui.AppointmentBooking;

import android.util.Log;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDateTime;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Retrofit.ApiService;
import java.util.ArrayList;
import okhttp3.ResponseBody;
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
        networkApi.getUnavailableDates(doctorProfileInfo.getDoctorId().getId()).enqueue(new Callback<ArrayList<ModelDateTime>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelDateTime>> call, Response<ArrayList<ModelDateTime>> response) {
                if(response.isSuccessful() && response.code()==200){
                    ArrayList<ModelDateTime> unavailableDates,CompletelyUnavailableDates,PartiallyUnavailableDates;
                    unavailableDates = response.body();
                    CompletelyUnavailableDates = new ArrayList<>();
                    PartiallyUnavailableDates = new ArrayList<>();
                    for (int i=0;i<unavailableDates.size();i++){
                        if (unavailableDates.get(i).getUnavailableTimes().size()<doctorProfileInfo.getDoctorAvailabilityTime().size())
                            PartiallyUnavailableDates.add(unavailableDates.get(i));
                        else
                            CompletelyUnavailableDates.add(unavailableDates.get(i));
                    }
                    Log.d("Respons:",response.body().toString());
                    presenter.setUpDatePicker(unavailableDates,CompletelyUnavailableDates,PartiallyUnavailableDates,doctorProfileInfo);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelDateTime>> call, Throwable t) {

            }
        });
    }

    @Override
    public void bookAppointment(ModelBookAppointment appointment) {
        networkApi = ApiService.getAPIService();
        networkApi.bookAppointment(appointment).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code()==200)
                    presenter.OnResponse("Appointment Saved Successfully\n  You will be notified soon.");
                else
                    presenter.OnResponse(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                presenter.OnResponse(t.getMessage());
            }
        });
    }
}
