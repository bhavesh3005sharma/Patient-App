package com.scout.patient.ui.DoctorsProfile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DoctorsProfileActivity extends AppCompatActivity implements Contract.View, View.OnClickListener {
    @BindView(R.id.profileImage) ImageView profileImg;
    @BindView(R.id.textName) TextView textName;
    @BindView(R.id.textSpecialisation) TextView textSpecialisation;
    @BindView(R.id.textDoctorAvailability) TextView textDoctorAvailability;
    @BindView(R.id.textDoctorAvailabilityTime) TextView textDoctorAvailabilityTime;
    @BindView(R.id.textCareerHistory) TextView textCareerHistory;
    @BindView(R.id.textLearningHistory) TextView textLearningHistory;
    @BindView(R.id.textEmail) TextView textEmail;
    @BindView(R.id.textPhoneNo) TextView textPhoneNo;
    @BindView(R.id.textAddress) TextView textAddress;
    @BindView(R.id.profileLayout) ConstraintLayout profileLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.buttonBookAppointment) Button buttonBookAppointment;
    ProgressBar progressBarDialogue;

    Unbinder unbinder;
    ModelDoctorInfo doctorInfo;
    DoctorsProfilePresenter presenter;
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
        setToolbar(doctorInfo.getName());

        buttonBookAppointment.setOnClickListener(this);
        setProfileData(doctorInfo);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void setToolbar(String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    private void setProfileData(ModelDoctorInfo doctorInfo) {
        textName.setText(doctorInfo.getName());
        textSpecialisation.setText(doctorInfo.getDepartment());
        textCareerHistory.setText(doctorInfo.getCareerHistory());
        textLearningHistory.setText(doctorInfo.getLearningHistory());
        textEmail.setText(doctorInfo.getEmail());
        textPhoneNo.setText(doctorInfo.getPhone_no());
        textAddress.setText(doctorInfo.getAddress());

        textDoctorAvailability.setText(presenter.getAvailabilityType(doctorInfo.getAvailabilityType(),doctorInfo.getDoctorAvailability(),this));
        textDoctorAvailabilityTime.setText(presenter.getAvailabilityTime(doctorInfo.getAvgCheckupTime(),doctorInfo.getDoctorAvailabilityTime(),this));

        HelperClass.hideProgressbar(progressBar);
        profileLayout.setVisibility(View.VISIBLE);
    }
}
