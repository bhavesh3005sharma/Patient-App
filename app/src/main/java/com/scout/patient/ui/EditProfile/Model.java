package com.scout.patient.ui.EditProfile;

import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Retrofit.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model implements Contract.Model {
    Contract.Presenter presenter;

    public Model(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateProfileData(ModelPatientInfo patientInfo) {
        ApiService.getAPIService().updateProfileData(patientInfo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code()==200)
                    presenter.showToast("Profile Updated Successfully.");
                else
                    presenter.showToast(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                presenter.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void updateProfilePic(EditProfileActivity editProfileActivity, String url) {
        ModelPatientInfo patientInfo = SharedPref.getLoginUserData(editProfileActivity);
        String id = patientInfo.getPatientId().getId();
        ApiService.getAPIService().updateProfilePic(id,url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code()==200) {
                    presenter.showToast("Profile Pic Updated Successfully.");
                    patientInfo.setUrl(url);
                    SharedPref.saveLoginUserData(editProfileActivity,patientInfo);
                }
                else
                    presenter.showToast(response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                presenter.showToast(t.getMessage());
            }
        });
    }
}
