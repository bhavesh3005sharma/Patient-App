package com.scout.patient.ui.Auth.Registration;

import com.scout.patient.ui.Auth.Registration.Contract;

public class RegistrationPresenter implements Contract.Presenter {
    Contract.View mainView;

    public RegistrationPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
