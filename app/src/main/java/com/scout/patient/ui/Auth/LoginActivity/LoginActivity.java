package com.scout.patient.ui.Auth.LoginActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.PasswordClass;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements Contract.View , View.OnClickListener{
    @BindView(R.id.textInputEmail)
    TextInputLayout textInputLayout;
    @BindView(R.id.textInputPassword)
    TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegistration)
    Button btnRegistration;

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
        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin :
               char[] password =  textInputPassword.getEditText().getText().toString().toCharArray();
               byte[] salt = PasswordClass.getNextSalt();
               byte[] hash = PasswordClass.hash(password,salt);
               Log.d(TAG,"\nhash = "+hash.toString()+"\nSalt = "+salt.toString()+"\nPassword = "+password.toString()+PasswordClass.isExpectedPassword(password,salt,hash));

                break;
            case R.id.btnRegistration :
                //startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));

                break;
        }
    }

    public void saveToSharedPref(){

    }
}
