package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.scout.patient.R;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.Retrofit.*;
import com.scout.patient.ui.Auth.ForgotPasswordActivity;
import com.scout.patient.ui.Auth.Registration.RegistrationActivity;
import com.scout.patient.ui.Welcome.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements Contract.View , View.OnClickListener{
    private FirebaseAuth mAuth;
    RetrofitNetworkApi networkApi;
    Call<ModelPatientInfo> call;

    @BindView(R.id.textInputEmailLogin) TextInputLayout textInputEmail;
    @BindView(R.id.textForgotPassword) TextView textForgotPassword;
    @BindView(R.id.textInputPassword) TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnRegistration) Button btnRegistration;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private static final String TAG = "LoginActivity";
    Unbinder unbinder;
    LoginPresenter presenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()) {
            openHomeActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        presenter = new LoginPresenter(LoginActivity.this);
        initRetrofitApi();
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
        textForgotPassword.setOnClickListener(this);
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin :
               String email = textInputEmail.getEditText().getText().toString().trim();
               String password = textInputPassword.getEditText().getText().toString().trim();

                if (email.isEmpty()){
                    textInputEmail.setError("Email is Required");
                    textInputEmail.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputEmail.setError("Please Enter The Valid Email.");
                    textInputEmail.requestFocus();
                    return;
                }else textInputEmail.setError(null);

                if (password.isEmpty() || password.length() < 6) {
                    textInputPassword.setError("At least 6 Character Password is Required.");
                    textInputPassword.requestFocus();
                    return;
                }else textInputPassword.setError(null);

                loginUser(email,password);
                break;
            case R.id.btnRegistration :
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
            case R.id.textForgotPassword :
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void loginUser(String email, String password) {
        HelperClass.showProgressbar(progressBar);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        call = networkApi.getPatientInfo(email,null);
                        generateFcmToken(email);
                        presenter.getPatientInfo(LoginActivity.this,progressBar,call,password);
                    }
                    else{
                        HelperClass.hideProgressbar(progressBar);
                        HelperClass.toast(LoginActivity.this,"User not Verified\n  check Email");
                    }
                }else{
                    HelperClass.hideProgressbar(progressBar);
                    HelperClass.toast(LoginActivity.this,task.getException().getMessage());
                }
            }
        });
    }

    private void generateFcmToken(String email) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Task : ", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("InstanceId(Token) : ",token);
                        presenter.saveFcmToken(email,token);
                    }
                });
    }

    @Override
    public void openHomeActivity() {
        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        finish();
    }
}
