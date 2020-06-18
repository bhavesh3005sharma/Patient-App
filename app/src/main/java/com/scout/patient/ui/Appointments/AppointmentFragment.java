package com.scout.patient.ui.Appointments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;

public class AppointmentFragment extends Fragment implements Contract.View{
    AppointmentPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        presenter = new AppointmentPresenter(AppointmentFragment.this);

        return view;
    }
}
