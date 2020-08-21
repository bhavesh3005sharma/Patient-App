package com.scout.patient.ui.Auth.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.TashieLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
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
    private GoogleSignInClient mGoogleSignInClient;
    private static final int GOOGLE_SIGN_IN = 9001;

    RetrofitNetworkApi networkApi;
    Call<ModelPatientInfo> call;

    @BindView(R.id.textInputEmailLogin) TextInputLayout textInputEmail;
    @BindView(R.id.textForgotPassword) TextView textForgotPassword;
    @BindView(R.id.textInputPassword) TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnRegistration) Button btnRegistration;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.google_signIn) SignInButton googleSignIn;
    @BindView(R.id.tashieLoader) TashieLoader tashieLoader;

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
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified() && SharedPref.getLoginUserData(LoginActivity.this)!=null) {
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
        googleSignIn.setOnClickListener(this);
    }

    private void initGoogleSignIn() {
        // [START config_signIn]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signIn]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tashieLoader.setVisibility(View.GONE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                HelperClass.toast(this,"Google sign in Failed");
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        HelperClass.showProgressbar(progressBar);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                // Person is Signing with google hence its email is obviously verified no need to check.
                                String email = mAuth.getCurrentUser().getEmail();
                                call = networkApi.getPatientInfo(email,null);
                                generateFcmToken(email);
                                presenter.getPatientInfo(LoginActivity.this,progressBar,call,"");
                            }
                        }else{
                            HelperClass.hideProgressbar(progressBar);
                            HelperClass.toast(LoginActivity.this,task.getException().getMessage());
                            mAuth.signOut();
                        }
                    }
                });
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
            case R.id.google_signIn :
                tashieLoader.setVisibility(View.VISIBLE);
                initGoogleSignIn();
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
                        mAuth.signOut();
                    }
                }else{
                    HelperClass.hideProgressbar(progressBar);
                    HelperClass.toast(LoginActivity.this,task.getException().getMessage());
                    mAuth.signOut();
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

    @Override
    public void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
    }


}
