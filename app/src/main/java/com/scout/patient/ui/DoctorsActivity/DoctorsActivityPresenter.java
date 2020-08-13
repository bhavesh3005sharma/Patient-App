package com.scout.patient.ui.DoctorsActivity;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.Models.ModelRequestId;

import java.util.ArrayList;

public class DoctorsActivityPresenter implements Contract.Presenter {
    Contract.View mainView;
    Model model;

    public DoctorsActivityPresenter(Contract.View mainView) {
        this.mainView = mainView;
        this.model = new Model(DoctorsActivityPresenter.this);
    }

    @Override
    public void loadDoctorsList(String startingValue, int noOfItems) {
        model.getDoctorsList(startingValue,noOfItems);
    }

    @Override
    public void onError(String message) {
        mainView.setErrorUi(message);
    }

    @Override
    public void onSuccess(ArrayList<ModelKeyData> data) {
        mainView.updateSuccessUi(data);
    }

    @Override
    public void loadDoctorsList(ArrayList<ModelRequestId> listOfDoctorsIds, int startingIndex) {
        model.getDoctorsList(listOfDoctorsIds,startingIndex);
    }
}
