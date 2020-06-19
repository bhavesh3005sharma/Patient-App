package com.scout.patient.ui.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;
import com.scout.patient.ui.BookAmbulanceActivity;
import com.scout.patient.ui.BookAppointmentActivity;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements Contract.View, View.OnClickListener{
    @BindView(R.id.card_hospital)
    CardView card_hospital;
    @BindView(R.id.card_doctor)
    CardView card_doctor;
    @BindView(R.id.card_appointment)
    CardView card_appointment;
    @BindView(R.id.card_ambulance)
    CardView card_ambulance;

    Unbinder unbinder;
    HomePresenter presenter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        presenter = new HomePresenter(HomeFragment.this);
        card_hospital.setOnClickListener(this);
        card_doctor.setOnClickListener(this);
        card_appointment.setOnClickListener(this);
        card_ambulance.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_hospital:
                break;
            case R.id.card_doctor:
                startActivity(new Intent(getContext(), DoctorsActivity.class));
                break;
            case R.id.card_appointment:
                startActivity(new Intent(getContext(), BookAppointmentActivity.class));
                break;
            case R.id.card_ambulance:
                startActivity(new Intent(getContext(), BookAmbulanceActivity.class));
                break;
        }
    }
}
