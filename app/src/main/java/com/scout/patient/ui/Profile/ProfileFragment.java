package com.scout.patient.ui.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;
import com.scout.patient.ui.Notification.Contract;

public class ProfileFragment extends Fragment implements Contract.View{
    ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        presenter = new ProfilePresenter( ProfileFragment.this);

        return view;
    }
}
