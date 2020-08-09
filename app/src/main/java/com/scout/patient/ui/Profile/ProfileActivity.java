package com.scout.patient.ui.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.EditProfile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.textViewName)
    TextView textName;
    @BindView(R.id.email)
    TextView textEmail;
    @BindView(R.id.contactNo)
    TextView textPhoneNo;
    @BindView(R.id.address)
    TextView textAddress;
    @BindView(R.id.medical_history)
    TextView textMedicalHistory;
    @BindView(R.id.blood_group)
    TextView textBloodGroup;
    @BindView(R.id.weight)
    TextView textWeight;
    @BindView(R.id.dob)
    TextView textDob;
    @BindView(R.id.profileImage)
    CircularImageView profileImage;
    @BindView(R.id.editProfile)
    ImageView editProfile;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.account_details)
    LinearLayout accountDetails;
    @BindView(R.id.medical_details)
    LinearLayout medicalDetails;
    @BindView(R.id.textInfoMedical)
    TextView textInfoMedical;
    @BindView(R.id.textInfoAccount)
    TextView textInfoAccount;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    Unbinder unbinder;
    ProfilePresenter presenter;
    ModelPatientInfo patientInfo;
    Boolean isLoading = false;
    String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        unbinder = ButterKnife.bind(this);

        isLoading = true;
        swipeRefreshLayout.setOnRefreshListener(this);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new ProfilePresenter( ProfileActivity.this);
        userId = SharedPref.getLoginUserData(this).getPatientId().getId();
        patientInfo = presenter.getUserData(userId);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("patientInfo",patientInfo);
                startActivity(intent);
            }
        });
    }

    private void setUpView() {
        if (textName!=null) {
            textName.setText(patientInfo.getName());
            textAddress.setText(patientInfo.getAddress());
            textBloodGroup.setText(patientInfo.getBloodGroup());
            textMedicalHistory.setText(patientInfo.getMedicalHistory());
            textDob.setText(patientInfo.getDOB());
            textEmail.setText(patientInfo.getEmail());
            textWeight.setText(patientInfo.getWeight());
            textPhoneNo.setText(patientInfo.getPhoneNo());
            if (patientInfo.getUrl()!=null)
                Picasso.get().load(Uri.parse(patientInfo.getUrl())).placeholder(R.color.placeholder_bg).into(profileImage);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(ModelPatientInfo body) {
        if (body!=null) {
            patientInfo = body;
            setUpView();
        }
        isLoading = false;
        HelperClass.hideProgressbar(progressBar);
        textInfoAccount.setVisibility(View.VISIBLE);
        textInfoMedical.setVisibility(View.VISIBLE);
        accountDetails.setVisibility(View.VISIBLE);
        medicalDetails.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String s) {
        isLoading = false;
        HelperClass.toast(this,s);
        if (progressBar!=null)
            HelperClass.hideProgressbar(progressBar);
    }

    @Override
    public void onRefresh() {
        if (!isLoading){
            presenter.getUserData(userId);
        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
