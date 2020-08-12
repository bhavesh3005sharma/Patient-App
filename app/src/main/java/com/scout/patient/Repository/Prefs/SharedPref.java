package com.scout.patient.Repository.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Models.ModelPatientInfo;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPref {

    public static void saveLoginUserData(Context context, ModelPatientInfo patientInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_user_data),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(patientInfo);
        editor.putString("patientInfo", json);
        editor.apply();
    }

    public static ModelPatientInfo getLoginUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_user_data),context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("patientInfo", null);
        Type type = new TypeToken<ModelPatientInfo>() {}.getType();
        ModelPatientInfo patientInfo = gson.fromJson(json, type);
        return  patientInfo;
    }

    public static void deleteLoginUserData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_user_data),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveAllHospitalsList(Context context, ArrayList<ModelKeyData> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_hospitals_list),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("hospitalsList", json);
        editor.apply();
    }

    public static ArrayList<ModelKeyData> getAllHospitalsList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_hospitals_list),context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("hospitalsList", null);
        Type type = new TypeToken<ArrayList<ModelKeyData>>() {}.getType();
        ArrayList<ModelKeyData> list = gson.fromJson(json, type);
        return  list;
    }

    public static void saveAllDoctorsList(Context context, ArrayList<ModelKeyData> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_doctors_list),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("doctorsList", json);
        editor.apply();
    }

    public static ArrayList<ModelKeyData> getAllDoctorsList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.pref_for_doctors_list),context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("doctorsList", null);
        Type type = new TypeToken<ArrayList<ModelKeyData>>() {}.getType();
        ArrayList<ModelKeyData> list = gson.fromJson(json, type);
        return  list;
    }
}
