package com.scout.patient.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Retrofit.ApiService;
import com.scout.patient.ui.Welcome.WelcomeActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                finish();
            }
        },2000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadSearchData();
            }
        }).start();
    }

    private void loadSearchData() {
        ApiService.getAPIService().getHospitalsList(null,-1).enqueue(new Callback<ArrayList<ModelKeyData>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelKeyData>> call, Response<ArrayList<ModelKeyData>> response) {
                Log.d("Response","getHospitalsList - "+response.body());
                if (response.isSuccessful() && response.code()==200 && response.body()!=null){
                    ArrayList<ModelKeyData> list = new ArrayList<>();
                    for (ModelKeyData modelKeyData : response.body()) {
                        modelKeyData.setHospital(true);
                        list.add(modelKeyData);
                    }
                    SharedPref.saveAllHospitalsList(SplashScreen.this,list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelKeyData>> call, Throwable t) {

            }
        });

        ApiService.getAPIService().getDoctorsList(null, -1).enqueue(new Callback<ArrayList<ModelKeyData>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelKeyData>> call, Response<ArrayList<ModelKeyData>> response) {
                Log.d("Response","getDoctorsList - "+response.body());
                if (response.isSuccessful() && response.code()==200 && response.body()!=null && !response.body().isEmpty()){
                    ArrayList<ModelKeyData> list = new ArrayList<>();
                    for (ModelKeyData modelKeyData : response.body()) {
                        modelKeyData.setHospital(false);
                        list.add(modelKeyData);
                    }
                    SharedPref.saveAllDoctorsList(SplashScreen.this,list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelKeyData>> call, Throwable t) {

            }
        });
    }
}
