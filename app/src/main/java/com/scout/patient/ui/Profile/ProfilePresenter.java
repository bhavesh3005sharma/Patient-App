package com.scout.patient.ui.Profile;

import android.util.Log;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Retrofit.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresenter implements Contract.Presenter {
    Contract.View mainView;
    Contract.Model model;

    public ProfilePresenter(Contract.View mainView) {
        this.mainView = mainView;
        this.model = new Model(ProfilePresenter.this);
    }

    @Override
    public ModelPatientInfo getUserData(String id) {
        model.getUserData(id);
        return null;
    }

    @Override
    public void onError(String s) {
        if (mainView!=null)
            mainView.onError(s);
    }

    @Override
    public void onSuccess(ModelPatientInfo body) {
        if (mainView!=null)
            mainView.onSuccess(body);
    }

    @Override
    public void deleteFcmToken(String email, String token) {
        ApiService.getAPIService().DeleteFcmToken(email,token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.code()==200)
                            Log.d("Token","Deleted Successfully\n"+token);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    @Override
    public void sendPasswordUpdateNotification(String email) {
        model.sendPasswordUpdateNotification(email);
    }
}
