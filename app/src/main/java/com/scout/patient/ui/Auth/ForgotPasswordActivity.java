package com.scout.patient.ui.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.textInputEmailLogin) TextInputLayout textInputEmail;
    @BindView(R.id.buttonResetPassword) Button buttonResetPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        unbinder = ButterKnife.bind(this);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEmail.getEditText().getText().toString();

                if (email.isEmpty()){
                    textInputEmail.setError("Email is Required");
                    textInputEmail.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputEmail.setError("Please Enter The Valid Email.");
                    textInputEmail.requestFocus();
                    return;
                }else textInputEmail.setError(null);

                ResetPasswordEmailSend(email);
            }
        });
    }

    private void ResetPasswordEmailSend(String email){
        HelperClass.showProgressbar(progressBar);
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HelperClass.hideProgressbar(progressBar);
                        if(task.isSuccessful()){
                            HelperClass.toast(ForgotPasswordActivity.this,"Check Your Email");
                        }
                        else{
                            HelperClass.toast(ForgotPasswordActivity.this,task.getException().getMessage());
                        }
                    }
                });
    }
}
