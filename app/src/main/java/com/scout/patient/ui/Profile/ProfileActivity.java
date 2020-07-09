package com.scout.patient.ui.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelPatientInfo;
import com.scout.patient.data.Prefs.SharedPref;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_frag_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            SharedPref.deleteLoginUserData(this);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
