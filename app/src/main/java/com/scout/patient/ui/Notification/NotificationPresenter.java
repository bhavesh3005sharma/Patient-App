package com.scout.patient.ui.Notification;

public class NotificationPresenter implements Contract.Presenter {
    Contract.View mainView;

    public NotificationPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
