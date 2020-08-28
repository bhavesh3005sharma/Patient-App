package com.scout.patient.ui.Auth.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.scout.patient.R;
import com.scout.patient.Retrofit.*;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelPatientInfo;
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

    private FirebaseAuth mAuth;
    MaterialDatePicker datePicker;
    Unbinder unbinder;
    RegistrationPresenter presenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mAuth=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        unbinder = ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
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
                String bloodGrp;
                if (bloodGrpSpinner.getSelectedItemPosition()==0)
                    bloodGrp = "Not Submitted";
                else
                    bloodGrp = bloodGrpSpinner.getSelectedItem().toString();

                if (email.isEmpty()){
                    textInputEmailRegister.setError("Email is Required");
                    textInputEmailRegister.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputEmailRegister.setError("Please Enter The Valid Email.");
                    textInputEmailRegister.requestFocus();
                    return;
                }else textInputEmailRegister.setError(null);

                if (password.isEmpty() || password.length() < 6) {
                    textInputPassword.setError("At least 6 Character Password is Required.");
                    textInputPassword.requestFocus();
                    return;
                }else textInputPassword.setError(null);

                if (name.isEmpty()){
                    textInputName.setError("Name is Required");
                    textInputName.requestFocus();
                    return;
                }else textInputName.setError(null);

                if (phoneNo.isEmpty()){
                    textInputPhoneNo.setError("Phone No. is Required");
                    textInputPhoneNo.requestFocus();
                    return;
                }else textInputPhoneNo.setError(null);

                ModelPatientInfo patientInfo = new ModelPatientInfo(email,weight,name,dob,phoneNo,address,bloodGrp,previousDisease);
                generateFcmToken(email,password,patientInfo);

                break;
            case R.id.btnLogin :
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                break;
            case R.id.textInputDOB :
                if (!datePicker.isAdded())
                datePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                break;
        }
    }

    private void registerUser(String email, String password, ModelPatientInfo patientInfo){
        HelperClass.showProgressbar(progressBar);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> it) {
                                    if (it.isSuccessful()) {
                                        call = networkApi.registerPatient(patientInfo);
                                        presenter.registerPatient(RegistrationActivity.this,progressBar,call);
                                        mAuth.signOut();
                                    } else {
                                        HelperClass.hideProgressbar(progressBar);
                                        HelperClass.toast(RegistrationActivity.this,it.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            HelperClass.hideProgressbar(progressBar);
                            HelperClass.toast(RegistrationActivity.this,task.getException().getMessage());
                        }
                    }
                });
    }

    private void generateFcmToken(String email, String password, ModelPatientInfo patientInfo) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Task : ", "getInstanceId failed", task.getException());
                            HelperClass.toast(RegistrationActivity.this,"Token Cannot be Generated");
                            registerUser(email,password,patientInfo);
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("InstanceId(Token) : ",token);
                        patientInfo.setFcmToken(token);
                        registerUser(email,password,patientInfo);
                    }
                });
    }
}
