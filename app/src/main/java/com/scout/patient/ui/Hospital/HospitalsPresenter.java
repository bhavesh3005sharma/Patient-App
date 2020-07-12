package com.scout.patient.ui.Hospital;

import com.scout.patient.Models.ModelHospitalInfo;
import java.util.ArrayList;

public class HospitalsPresenter implements Contract.Presenter {
    Contract.View mainView;
    Model model;

    public HospitalsPresenter(Contract.View mainView) {
        this.mainView = mainView;
        model = new Model(HospitalsPresenter.this);
    }

    @Override
    public void getHospitalsList() {
        model.loadHospitalsList();
    }

    @Override
    public void onError(String message) {
        mainView.setErrorUi(message);
    }

    @Override
    public void onSuccess(ArrayList<ModelHospitalInfo> data) {
        mainView.updateSuccessUi(data);
    }
}