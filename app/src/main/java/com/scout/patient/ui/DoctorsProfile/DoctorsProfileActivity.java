package com.scout.patient.ui.DoctorsProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelBookAppointment;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DoctorsProfileActivity extends AppCompatActivity implements Contract.View, View.OnClickListener {
    @BindView(R.id.profileImage) CircularImageView profileImg;
    @BindView(R.id.textName) TextView textName;
    @BindView(R.id.textSpecialisation) TextView textSpecialisation;
    @BindView(R.id.textWorkingPlaces) TextView textWorkingPlaces;
    @BindView(R.id.textCareerHistory) TextView textCareerHistory;
    @BindView(R.id.textLearningHistory) TextView textLearningHistory;
    @BindView(R.id.textEmail) TextView textEmail;
    @BindView(R.id.textPhoneNo) TextView textPhoneNo;
    @BindView(R.id.textAddress) TextView textAddress;
    @BindView(R.id.profileLayout) ConstraintLayout profileLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.buttonBookAppointment) Button buttonBookAppointment;

    Unbinder unbinder;
    DoctorsProfilePresenter presenter;
    ModelDoctorInfo doctorInfo;
    ModelBookAppointment modelBookAppointment;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_profile);
        unbinder = ButterKnife.bind(this);

        presenter = new DoctorsProfilePresenter(DoctorsProfileActivity.this);
        doctorInfo = (ModelDoctorInfo) getIntent().getSerializableExtra("ProfileModel");
        modelBookAppointment = (ModelBookAppointment) getIntent().getSerializableExtra("modelBookAppointment");
        setToolbar();

        buttonBookAppointment.setOnClickListener(this);
        setProfileData();
    }

    private void setToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(doctorInfo.getName());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setProfileData() {
        textName.setText(doctorInfo.getName());
        textSpecialisation.setText(doctorInfo.getSpecialization());
        textCareerHistory.setText(doctorInfo.getCareerHistory());
        textLearningHistory.setText(doctorInfo.getLearningHistory());
        textEmail.setText(doctorInfo.getUserName());
        textPhoneNo.setText(doctorInfo.getPhoneNo());
        textAddress.setText(doctorInfo.getAddress());

        String workingPlaces=null;
        for(int i=0; i<doctorInfo.getWorkingPlaces().size();i++)
            workingPlaces=doctorInfo.getWorkingPlaces().get(i)+"\n";
        textWorkingPlaces.setText(workingPlaces);

        HelperClass.hideProgressbar(progressBar);
        profileLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonBookAppointment :
                Intent intent = new Intent(DoctorsProfileActivity.this, BookAppointmentActivity.class);
                intent.putExtra("doctorInfo",doctorInfo);
                intent.putExtra("modelBookAppointment",modelBookAppointment);
                startActivity(intent);
                finish();
                break;
        }
    }
}
