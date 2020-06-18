package com.scout.patient.ui.Prescription;

import com.scout.patient.ui.Prescription.Contract;

public class PrescriptionPresenter implements Contract.Presenter {
    Contract.View mainView;

    public PrescriptionPresenter(Contract.View mainView) {
        this.mainView = mainView;
    }
}
