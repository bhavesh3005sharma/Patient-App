package com.scout.patient.ui.Profile;

import com.scout.patient.Models.ModelPatientInfo;

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
}
