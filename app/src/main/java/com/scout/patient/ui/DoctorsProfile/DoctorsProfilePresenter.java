package com.scout.patient.ui.DoctorsProfile;

import com.scout.patient.ui.DoctorsProfile.Contract;

public class DoctorsProfilePresenter implements Contract.Presenter {
    Contract.View mainView;

    public DoctorsProfilePresenter(Contract.View mainView) {
        this.mainView = mainView;
    }

}
