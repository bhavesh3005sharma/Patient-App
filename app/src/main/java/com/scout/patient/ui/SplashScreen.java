package com.scout.patient.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Repository.Room.DataBaseManager;
import com.scout.patient.Retrofit.ApiService;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
import com.scout.patient.ui.Welcome.WelcomeActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    DataBaseManager dataBaseManager = new DataBaseManager(SplashScreen.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
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
                if (response.isSuccessful() && response.code()==200 && response.body()!=null){
                    dataBaseManager.clearHospitalsList();
                    for (ModelKeyData modelKeyData : response.body())
                        dataBaseManager.addData(modelKeyData.getId().getId(), modelKeyData.getName(), modelKeyData.getImageUrl(), "1");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelKeyData>> call, Throwable t) {

            }
        });

        ApiService.getAPIService().getDoctorsList(null, -1).enqueue(new Callback<ArrayList<ModelKeyData>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelKeyData>> call, Response<ArrayList<ModelKeyData>> response) {
                if (response.isSuccessful() && response.code()==200 && response.body()!=null && !response.body().isEmpty()){
                    dataBaseManager.clearDoctorsList();
                    for (ModelKeyData modelKeyData : response.body())
                        dataBaseManager.addData(modelKeyData.getId().getId(),modelKeyData.getName(),modelKeyData.getImageUrl(),"2");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelKeyData>> call, Throwable t) {

            }
        });
    }
}
