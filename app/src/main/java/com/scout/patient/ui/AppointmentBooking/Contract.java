package com.scout.patient.ui.AppointmentBooking;

import android.widget.ProgressBar;

import com.scout.patient.Models.ModelDoctorInfo;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class Contract {
    interface  View{

    }
    interface  Presenter{

        void bookAppointment(Call<ResponseBody> call, BookAppointmentActivity bookAppointmentActivity, ProgressBar progressBar);

        Calendar[] getAvailabilityDates(ModelDoctorInfo doctorProfileInfo);
    }
}
