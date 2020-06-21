package com.scout.patient.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scout.patient.R;
import com.scout.patient.ui.Appointments.AppointmentFragment;
import com.scout.patient.ui.Home.HomeFragment;
import com.scout.patient.ui.Notification.NotificationFragment;
import com.scout.patient.ui.Prescription.PrescriptionFragment;
import com.scout.patient.ui.Profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WelcomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    Unbinder unbinder;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        unbinder = ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState==null)
            loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                loadFragment(new HomeFragment());
                return true;
            case R.id.item_appointment:
                loadFragment(new AppointmentFragment());
                return true;
            case R.id.item_prescriptions:
                loadFragment(new PrescriptionFragment());
                return true;
            case R.id.item_notifications:
                loadFragment(new NotificationFragment());
                return true;
            case R.id.item_profile:
                loadFragment(new ProfileFragment());
                return true;
        }
        return true;
    }
}
