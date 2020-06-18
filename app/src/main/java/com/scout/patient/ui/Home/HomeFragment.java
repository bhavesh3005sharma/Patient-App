package com.scout.patient.ui.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;

public class HomeFragment extends Fragment implements Contract.View{
    HomePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        presenter = new HomePresenter(HomeFragment.this);

        return view;
    }
}
