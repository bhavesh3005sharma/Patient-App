package com.scout.patient.ui.Auth.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegistrationActivity extends AppCompatActivity implements Contract.View{
    @BindView(R.id.textInputEmail)
    TextInputLayout textInputLayout;
    @BindView(R.id.textInputPassword)
    TextInputLayout textInputPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegistration)
    Button btnRegistration;
    @BindView(R.id.textInputName)
    TextInputLayout textInputName;
    @BindView(R.id.textInputPhoneNo)
    TextInputLayout textInputPhoneNo;
    @BindView(R.id.textInputAddress)
    TextInputLayout textInputAddress;
    @BindView(R.id.textInputDOB)
    TextInputLayout textInputDOB;
    @BindView(R.id.textInputMedicalHistory)
    TextInputLayout textInputMedicalHistory;
    @BindView(R.id.textInputWeight)
    TextInputLayout textInputWeight;
    @BindView(R.id.bloodGrpSpinner)
    Spinner bloodGrpSpinner;

    Unbinder unbinder;
    RegistrationPresenter presenter;

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
}
