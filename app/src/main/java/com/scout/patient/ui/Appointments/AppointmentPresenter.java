package com.scout.patient.ui.Appointments;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelBookAppointment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;

    public AppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void loadAppointments(Context context, Call<ArrayList<ModelAppointment>> call, ProgressBar progressBar) {
        call.enqueue(new Callback<ArrayList<ModelAppointment>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelAppointment>> call, Response<ArrayList<ModelAppointment>> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful() && response.body()!=null){
                    AppointmentFragment.list = response.body();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainView.notifyAdapter();
                        }
                    },1000);

                }
                Log.d("appointments",response.body()+"" +
                        response.isSuccessful());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelAppointment>> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });
    }
}
