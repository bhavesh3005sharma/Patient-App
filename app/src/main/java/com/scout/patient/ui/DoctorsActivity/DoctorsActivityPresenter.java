package com.scout.patient.ui.DoctorsActivity;

import android.content.Context;
import android.widget.ProgressBar;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.ui.Appointments.AppointmentFragment;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsActivityPresenter implements Contract.Presenter {
    Contract.View mainView;

    public DoctorsActivityPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void loadDoctorslist(Context context, Call<ArrayList<ModelDoctorInfo>> call, ProgressBar progressBar) {
        DoctorsActivity.isCallRunning = true;
        call.enqueue(new Callback<ArrayList<ModelDoctorInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelDoctorInfo>> call, Response<ArrayList<ModelDoctorInfo>> response) {
                HelperClass.hideProgressbar(progressBar);
                DoctorsActivity.list.clear();
                DoctorsActivity.isCallRunning = false;
                if (response.isSuccessful() && response.body()!=null){
                    for(ModelDoctorInfo doctorInfo : response.body())
                        DoctorsActivity.list.add(doctorInfo);
                    mainView.notifyAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelDoctorInfo>> call, Throwable t) {
                DoctorsActivity.isCallRunning = false;
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });

    }
}
