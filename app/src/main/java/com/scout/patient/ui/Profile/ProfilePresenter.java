package com.scout.patient.ui.Profile;

import com.scout.patient.ui.Notification.Contract;

public class ProfilePresenter implements Contract.Presenter {
    Contract.View mainView;

    public ProfilePresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
