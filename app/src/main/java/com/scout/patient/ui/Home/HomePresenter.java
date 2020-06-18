package com.scout.patient.ui.Home;

public class HomePresenter implements Contract.Presenter {
    Contract.View mainView;

    public HomePresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
