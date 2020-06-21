package com.scout.patient.ui.Auth.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelPatientInfo;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitNetworkApi;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class RegistrationActivity extends AppCompatActivity implements Contract.View,View.OnClickListener{
    RetrofitNetworkApi networkApi;
    Call<ResponseBody> call;

    @BindView(R.id.textInputEmailRegister) TextInputLayout textInputEmailRegister;
    @BindView(R.id.textInputPassword) TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnRegistration) Button btnRegistration;
    @BindView(R.id.textInputName) TextInputLayout textInputName;
    @BindView(R.id.textInputPhoneNo) TextInputLayout textInputPhoneNo;
    @BindView(R.id.textInputAddress) TextInputLayout textInputAddress;
    @BindView(R.id.textInputDOB) TextView textInputDOB;
    @BindView(R.id.textInputMedicalHistory) TextInputLayout textInputMedicalHistory;
    @BindView(R.id.textInputWeight) TextInputLayout textInputWeight;
    @BindView(R.id.bloodGrpSpinner) Spinner bloodGrpSpinner;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    MaterialDatePicker datePicker;
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
        textInputDOB.setOnClickListener(this);
        initRetrofitApi();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Your DOB");
        datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                textInputDOB.setText(datePicker.getHeaderText());
            }
        });
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistration :
                String name = textInputName.getEditText().getText().toString();
                String email = textInputEmailRegister.getEditText().getText().toString();
                String phoneNo = textInputPhoneNo.getEditText().getText().toString();
                String password  = textInputPassword.getEditText().getText().toString();
                String address = textInputAddress.getEditText().getText().toString();
                String dob = textInputDOB.getText().toString();
                String previousDisease = textInputMedicalHistory.getEditText().getText().toString();
                String weight = textInputWeight.getEditText().getText().toString();
                String bloodGrp = bloodGrpSpinner.getSelectedItem().toString();

                if (email.isEmpty()){
                    textInputEmailRegister.setError("Email is Required");
                    textInputEmailRegister.requestFocus();
                    return;
                }
                if (name.isEmpty()){
                    textInputName.setError("Name is Required");
                    textInputName.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    textInputPassword.setError("Password is Required");
                    textInputPassword.requestFocus();
                    return;
                }
                if (phoneNo.isEmpty()){
                    textInputPhoneNo.setError("Phone No. is Required");
                    textInputPhoneNo.requestFocus();
                    return;
                }

                ModelPatientInfo patientInfo = new ModelPatientInfo(email,weight,name,dob,phoneNo,address,bloodGrp,previousDisease);
                HelperClass.showProgressbar(progressBar);
                call = networkApi.registerPatient(patientInfo);
                presenter.registerPatient(this,progressBar,call);

                break;
            case R.id.btnLogin :
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                break;
            case R.id.textInputDOB :
                datePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                break;
        }
    }
}
