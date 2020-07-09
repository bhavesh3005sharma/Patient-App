package com.scout.patient.ui.Notification;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scout.patient.R;

public class NotificationActivity extends AppCompatActivity implements Contract.View{
    NotificationPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notfication);
        presenter = new NotificationPresenter(NotificationActivity.this);
        getSupportActionBar().setTitle("Notifications");
    }
}
