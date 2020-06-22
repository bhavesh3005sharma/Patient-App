package com.scout.patient.ui.AppointmentBooking;

import android.util.Log;
import android.widget.ProgressBar;

import com.scout.patient.Utilities.HelperClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;

    public BookAppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void bookAppointment(Call<ResponseBody> call, BookAppointmentActivity bookAppointmentActivity, ProgressBar progressBar) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HelperClass.hideProgressbar(progressBar);
                if (response.isSuccessful() && response.code()==200)
                    HelperClass.toast(bookAppointmentActivity,"Appointment Saved Successfully\n You will be notified soon.");
                else
                    HelperClass.toast(bookAppointmentActivity,response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                HelperClass.toast( bookAppointmentActivity,t.getMessage());
                HelperClass.hideProgressbar(progressBar);
            }
        });
    }
}
