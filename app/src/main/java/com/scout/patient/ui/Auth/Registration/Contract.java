package com.scout.patient.ui.Auth.Registration;

import android.content.Context;
import android.widget.ProgressBar;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class Contract {
    interface View {

    }

    interface Presenter {

        void registerPatient(Context context, ProgressBar progressBar, Call<ResponseBody> call);
    }
}
