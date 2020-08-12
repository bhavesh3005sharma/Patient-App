package com.scout.patient.ui.HospitalProfile;

import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.ui.HospitalProfile.Contract;

public class HospitalProfilePresenter implements Contract.Presenter {
    Contract.View mainView;
    Contract.Model model;

    public HospitalProfilePresenter(Contract.View mainView) {
        this.mainView = mainView;
        this.model = new HospitalProfileModel(HospitalProfilePresenter.this);
    }

    @Override
    public void getHospitalDetails(String hospitalId) {
        model.getHospitalDetails(hospitalId);
    }

    @Override
    public void onSuccess(ModelHospitalInfo body) {
        mainView.onSetUi(body);
    }

    @Override
    public void onError(String s) {
        mainView.onError(s);
    }
}
