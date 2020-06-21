package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelPatientInfo;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitNetworkApi;
import com.scout.patient.ui.Auth.Registration.RegistrationActivity;
import com.scout.patient.ui.WelcomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements Contract.View , View.OnClickListener{
    RetrofitNetworkApi networkApi;
    Call<ModelPatientInfo> call;

    @BindView(R.id.textInputEmailLogin) TextInputLayout textInputEmail;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        presenter = new LoginPresenter(LoginActivity.this);
        initRetrofitApi();
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin :
               String email = textInputEmail.getEditText().getText().toString();
               String password = textInputPassword.getEditText().getText().toString();

               call = networkApi.getPatientInfo(email,null);
               HelperClass.showProgressbar(progressBar);
               presenter.getPatientInfo(this,progressBar,call,password);
                break;
            case R.id.btnRegistration :
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
        }
    }

    @Override
    public void openHomeActivity() {
        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
    }
}
