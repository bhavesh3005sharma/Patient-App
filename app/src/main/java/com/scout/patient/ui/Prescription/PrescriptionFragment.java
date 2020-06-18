package com.scout.patient.ui.Prescription;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;

public class PrescriptionFragment extends Fragment implements Contract.View{
   PrescriptionPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescription, container, false);
        presenter = new PrescriptionPresenter(PrescriptionFragment.this);

        return view;
    }
}
