package com.scout.patient.ui.Appointments;

import com.scout.patient.ui.Appointments.Contract;

public class AppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;

    public AppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
