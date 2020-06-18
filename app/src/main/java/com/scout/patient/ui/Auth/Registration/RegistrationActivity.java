package com.scout.patient.ui.Auth.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.data.Remote.RetrofitNetworkApi;
import com.scout.patient.CustomApplication;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity implements Contract.View,View.OnClickListener{
    @Inject
    Retrofit retrofit;
    RetrofitNetworkApi networkApi;
    Call<String> call;

    @BindView(R.id.textInputEmail) TextInputLayout textInputLayout;
    @BindView(R.id.textInputPassword) TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnRegistration) Button btnRegistration;
    @BindView(R.id.textInputName) TextInputLayout textInputName;
    @BindView(R.id.textInputEmail) TextInputLayout textInputEmail;
    @BindView(R.id.textInputPhoneNo) TextInputLayout textInputPhoneNo;
    @BindView(R.id.textInputAddress) TextInputLayout textInputAddress;
    @BindView(R.id.textInputDOB) TextInputLayout textInputDOB;
    @BindView(R.id.textInputMedicalHistory) TextInputLayout textInputMedicalHistory;
    @BindView(R.id.textInputWeight) TextInputLayout textInputWeight;
    @BindView(R.id.bloodGrpSpinner) Spinner bloodGrpSpinner;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    Unbinder unbinder;
    RegistrationPresenter presenter;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        unbinder = ButterKnife.bind(this);

        presenter = new RegistrationPresenter(RegistrationActivity.this);
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
        initRetrofit();

        TextInputLayout dobInput = findViewById(R.id.textInputDOB);
        TextView text = findViewById(R.id.text);
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Your DOB");
        MaterialDatePicker datePicker = builder.build();

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dobInput.getEditText().setText(datePicker.getHeaderText());
            }
        });
    }

    private void initRetrofit() {
        ((CustomApplication) getApplication()).getNetworkComponent().injectRegistrationActivity(RegistrationActivity.this);
        networkApi = retrofit.create(RetrofitNetworkApi.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistration :
                String name = textInputName.getEditText().getText().toString();
                String email = textInputEmail.getEditText().getText().toString();
                String phoneNo = textInputPhoneNo.getEditText().getText().toString();
                String password  = textInputPassword.getEditText().getText().toString();
                String address = textInputAddress.getEditText().getText().toString();
                String dob = textInputDOB.getEditText().getText().toString();
                String previousDisease = textInputMedicalHistory.getEditText().getText().toString();
                String weight = textInputWeight.getEditText().getText().toString();
                String bloodGrp = bloodGrpSpinner.getSelectedItem().toString();

                if (email.isEmpty()){
                    textInputEmail.setError("Email is Required");
                    return;
                }
                if (name.isEmpty()){
                    textInputName.setError("Name is Required");
                    return;
                }
                if (password.isEmpty()){
                    textInputPassword.setError("Password is Required");
                    return;
                }
                if (phoneNo.isEmpty()){
                    textInputPhoneNo.setError("Phone No. is Required");
                    return;
                }
                HelperClass.showProgressbar(progressBar);
                call = networkApi.registerPatient(name,email,phoneNo,password,address,dob,previousDisease,weight,bloodGrp);
                presenter.registerPatient(this,progressBar,call);

                break;
            case R.id.btnLogin :
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                break;
        }
    }
}
