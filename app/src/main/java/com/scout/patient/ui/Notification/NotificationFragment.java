package com.scout.patient.ui.Notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;

public class NotificationFragment extends Fragment implements Contract.View{
    NotificationPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notfication, container, false);
        presenter = new NotificationPresenter(NotificationFragment.this);

        return view;
    }
}
