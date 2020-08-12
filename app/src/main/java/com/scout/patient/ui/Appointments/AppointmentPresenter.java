package com.scout.patient.ui.Appointments;

import android.content.Context;
import android.util.Log;

import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.Models.ModelRequestId;
import java.util.ArrayList;
import java.util.Collections;

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
        Log.d("ListSize",""+IdsList.size());
        Collections.reverse(IdsList);
        appointmentsIdsList = IdsList;
        model.getAppointmentsList(appointmentsIdsList,0);
    }

    @Override
    public void onSuccessAppointmentsList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex) {
        mainView.addDataToList(appointmentArrayList,newStartingIndex);
    }

    @Override
    public void getDoctorProfileData(String id, ModelAppointment appointment) {
        model.getDoctorProfileData(id,appointment);
    }

    @Override
    public void onSuccessDoctorDetails(ModelDoctorInfo doctorInfo, ModelAppointment appointment) {
        mainView.updateDoctorDetails(doctorInfo,appointment);
    }
}
