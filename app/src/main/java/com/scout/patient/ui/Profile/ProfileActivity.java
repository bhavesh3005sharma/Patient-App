package com.scout.patient.ui.Profile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileActivity extends AppCompatActivity implements Contract.View{
    @BindView(R.id.textId)
    TextView textId;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textEmail)
    TextView textEmail;
    @BindView(R.id.textPhoneNo)
    TextView textPhoneNo;
    @BindView(R.id.textAddress)
    TextView textAddress;
    @BindView(R.id.textMedicalHistory)
    TextView textMedicalHistory;
    @BindView(R.id.textBloodGroup)
    TextView textBloodGroup;
    @BindView(R.id.textWeight)
    TextView textWeight;
    @BindView(R.id.textDob)
    TextView textDob;

    Unbinder unbinder;
    ProfilePresenter presenter;
    ModelPatientInfo patientInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        unbinder = ButterKnife.bind(this);
        
        getSupportActionBar().setTitle("Your Profile");
        presenter = new ProfilePresenter( ProfileActivity.this);
        patientInfo = SharedPref.getLoginUserData(this);
        setUpView();
    }

    private void setUpView() {
        textId.setText(patientInfo.getPatientId().getId());
        textName.setText(patientInfo.getName());
        textAddress.setText(patientInfo.getAddress());
        textBloodGroup.setText(patientInfo.getBloodGroup());
        textMedicalHistory.setText(patientInfo.getMedicalHistory());
        textDob.setText(patientInfo.getDOB());
        textEmail.setText(patientInfo.getEmail());
        textWeight.setText(patientInfo.getWeight());
        textPhoneNo.setText(patientInfo.getPhoneNo());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
