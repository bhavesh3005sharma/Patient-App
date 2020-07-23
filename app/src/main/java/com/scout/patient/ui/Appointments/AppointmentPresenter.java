package com.scout.patient.ui.Appointments;

import android.content.Context;
import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelRequestId;
import java.util.ArrayList;

public class AppointmentPresenter implements Contract.Presenter {
    Contract.View mainView;
    Contract.Model model;
    ArrayList<ModelRequestId> appointmentsIdsList = new ArrayList<>();

    public AppointmentPresenter(Contract.View mainView) {
        this.mainView = mainView;
        model = new Model(AppointmentPresenter.this);
    }

    @Override
    public void loadAppointments(int startingIndex) {
        model.getAppointmentsList(appointmentsIdsList,startingIndex);
    }

    @Override
    public void loadAppointmentsIdsList(Context context) {
        model.getAppointmentsIdsList(context);
    }

    @Override
    public void onError(String message) {
        mainView.onError(message);
    }

    @Override
    public void onSuccessIdsList(ArrayList<ModelRequestId> IdsList) {
        appointmentsIdsList = IdsList;
        model.getAppointmentsList(appointmentsIdsList,0);
    }

    @Override
    public void onSuccessAppointmentsList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex) {
        mainView.addDataToList(appointmentArrayList,newStartingIndex);
    }
}
