package com.scout.patient.ui.DoctorsActivity;

import android.content.Context;
import android.widget.ProgressBar;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelDoctorInfo;
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
        call.enqueue(new Callback<ArrayList<ModelDoctorInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelDoctorInfo>> call, Response<ArrayList<ModelDoctorInfo>> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful()){
                    DoctorsActivity.list = response.body();
                    mainView.notifyAdapter();
                }
                else
                    HelperClass.toast(context,response.errorBody().toString());

            }

            @Override
            public void onFailure(Call<ArrayList<ModelDoctorInfo>> call, Throwable t) {
                HelperClass.hideProgressbar(progressBar);
                HelperClass.toast(context,t.getMessage());
            }
        });

    }
}
