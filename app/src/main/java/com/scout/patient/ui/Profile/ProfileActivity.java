package com.scout.patient.ui.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.Models.ModelPatientInfo;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;
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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int GOOGLE_SIGN_IN = 9001;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_change_password :
                openAuthDialogue();
                break;
            case R.id.menu_signout :
                SharedPref.deleteLoginUserData(this);

                // Firebase sign out
                FirebaseAuth.getInstance().signOut();

                // Google sign out
                initGoogleSignIn();
                mGoogleSignInClient.signOut();


                generateFcmToken(patientInfo.getEmail());

                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        presenter.deleteFcmToken(email,token);
                    }
                });
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

    private void openAuthDialogue() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder( ProfileActivity.this );
        View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.layout_password_authenticate, null, false);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final Boolean[] isAuthenticatedUser = {false};
        FirebaseUser currentUser = mAuth.getCurrentUser();


        TextInputLayout textInputPassword = view.findViewById(R.id.textInputPassword);
        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        Button buttonAuthenticate = view.findViewById(R.id.buttonAuthenticate);
        TrailingCircularDotsLoader trailingCircularDotsLoader = view.findViewById(R.id.trailingCircularDotsLoader);

        if (isAuthenticatedUser[0]){
            textViewTitle.setText(getString(R.string.text_enter_password_again));
            buttonAuthenticate.setText(getString(R.string.authenticate));
            textInputPassword.setHint(getString(R.string.loginPassword));
        }else {
            textViewTitle.setText(getString(R.string.text_enter_password_again));
            buttonAuthenticate.setText(getString(R.string.authenticate));
            textInputPassword.setHint(getString(R.string.loginPassword));
        }

        buttonAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = textInputPassword.getEditText().getText().toString().trim();
                if (password.isEmpty()){
                    textInputPassword.setError("Enter Your Password");
                    textInputPassword.requestFocus();
                    return;
                }else if(password.length()<6){
                    textInputPassword.setError("Minimum 6 Char Password Required.");
                    textInputPassword.requestFocus();
                    return;
                }else
                    textInputPassword.setError(null);

                trailingCircularDotsLoader.setVisibility(View.VISIBLE);
                view.setAlpha(0.5f);
                buttonAuthenticate.setEnabled(false);

                if (isAuthenticatedUser[0]){
                    currentUser.updatePassword(password).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            view.setAlpha(1.0f);
                            buttonAuthenticate.setEnabled(true);
                            trailingCircularDotsLoader.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                HelperClass.toast(ProfileActivity.this, "Password Updated Successfully");
                                presenter.sendPasswordUpdateNotification(patientInfo.getEmail());
                                alertDialog.dismiss();
                            }else {
                                HelperClass.toast(ProfileActivity.this, task.getException().getMessage());
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
                else {
                    AuthCredential credentials = EmailAuthProvider.getCredential(currentUser.getEmail(),password);
                    currentUser.reauthenticate(credentials).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            view.setAlpha(1.0f);
                            buttonAuthenticate.setEnabled(true);
                            trailingCircularDotsLoader.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                HelperClass.toast(ProfileActivity.this, "Authentication Successful\nChange Password");
                                isAuthenticatedUser[0] = true;
                                textViewTitle.setText(getString(R.string.enter_your_new_password));
                                buttonAuthenticate.setText(getString(R.string.confirm));
                                textInputPassword.setHint(getString(R.string.new_password));
                            }else {
                                HelperClass.toast(ProfileActivity.this, task.getException().getMessage());
                                textInputPassword.setError("Invalid Password");
                                textInputPassword.getEditText().setText(null);
                            }
                        }
                    });
                }
            }
        });
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
