package com.scout.patient.Utilities;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scout.patient.R;

public class HelperClass {
    public static String BASE_URL = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/patient_app-qjhgx/service/";

    public static void toast(Context context , String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    public static void showProgressbar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressbar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }
}
