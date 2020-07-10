package com.scout.patient.Repository.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scout.patient.R;
import com.scout.patient.Models.ModelPatientInfo;
import java.lang.reflect.Type;

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
}
