package com.scout.patient.ui.Appointments;

import android.content.Context;
import android.widget.ProgressBar;

import com.scout.patient.data.Models.ModelAppointment;

import java.util.ArrayList;

import retrofit2.Call;

public class Contract {
    interface  View{

        void notifyAdapter();

    }
    interface  Presenter{

        void loadAppointments(Context context, Call<ArrayList<ModelAppointment>> call, ProgressBar progressBar);
    }
}
