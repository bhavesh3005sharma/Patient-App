package com.scout.patient.ui.Auth.LoginActivity;

public class LoginPresenter implements Contract.Presenter {
    Contract.View mainView;

    public LoginPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
