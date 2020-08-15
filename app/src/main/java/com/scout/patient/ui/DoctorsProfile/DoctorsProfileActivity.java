package com.scout.patient.ui.DoctorsProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentActivity;
import com.squareup.picasso.Picasso;

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

    Unbinder unbinder;
    ModelDoctorInfo doctorInfo;
    DoctorsProfilePresenter presenter;
    ModelIntent modelIntent;
    String doctorId;

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
        doctorId = getIntent().getStringExtra("doctorId");
        setToolbar(getIntent().getStringExtra("doctorName"));
        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");

        presenter.getDoctorDetails(doctorId);
        buttonBookAppointment.setOnClickListener(this);
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
                modelIntent.setDoctorProfileInfo(doctorInfo);
                intent.putExtra("modelIntent",modelIntent);
                startActivity(intent);
                if (modelIntent.getBookAppointmentData()!=null)
                    finish();
                break;
        }
    }

    private void setToolbar(String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void updateUi(ModelDoctorInfo doctorInfo) {
        if (textName!=null && doctorInfo!=null ) {
            this.doctorInfo = doctorInfo;
            textName.setText(doctorInfo.getName()+" | "+doctorInfo.getHospitalName());
            textSpecialisation.setText(doctorInfo.getDepartment());
            textCareerHistory.setText(doctorInfo.getCareerHistory());
            textLearningHistory.setText(doctorInfo.getLearningHistory());
            textEmail.setText(doctorInfo.getEmail());
            textPhoneNo.setText(doctorInfo.getPhone_no());
            textAddress.setText(doctorInfo.getAddress());

            if (doctorInfo.getUrl() != null && !doctorInfo.getUrl().isEmpty())
                Picasso.get().load(Uri.parse(doctorInfo.getUrl())).placeholder(R.color.placeholder_bg).into(profileImg);
            else
                profileImg.setImageResource(R.drawable.doctor_icon);

            textDoctorAvailability.setText(presenter.getAvailabilityType(doctorInfo.getAvailabilityType(), doctorInfo.getDoctorAvailability(), this));
            textDoctorAvailabilityTime.setText(presenter.getAvailabilityTime(doctorInfo.getAvgCheckupTime(), doctorInfo.getDoctorAvailabilityTime(), this));

            HelperClass.hideProgressbar(progressBar);
            profileLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String s) {
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,s);
    }
}
