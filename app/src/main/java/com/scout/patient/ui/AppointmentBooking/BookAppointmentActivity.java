package com.scout.patient.ui.AppointmentBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelRequestId;
import com.scout.patient.data.Prefs.SharedPref;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitClient;
import com.scout.patient.data.Remote.RetrofitNetworkApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener ,Contract.View{
    @BindView(R.id.textInputPatientName)
    TextInputLayout textInputPatientName;
    @BindView(R.id.textInputDoctorName)
    TextInputLayout textInputDoctorName;
    @BindView(R.id.textInputHospitalName)
    TextInputLayout textInputHospitalName;
    @BindView(R.id.textInputDisease)
    TextInputLayout textInputDisease;
    @BindView(R.id.textInputAge)
    TextInputLayout textInputAge;
    @BindView(R.id.textViewSelectDate)
    TextView textViewSelectDate;
    @BindView(R.id.buttonBookAppointment)
    Button buttonBookAppointment;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    MaterialDatePicker datePicker;
    Call<ResponseBody> call;
    RetrofitNetworkApi networkApi;
    Unbinder unbinder;
    BookAppointmentPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setTitle("Book Your Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        presenter = new BookAppointmentPresenter(BookAppointmentActivity.this);
        textInputPatientName.getEditText().setText(SharedPref.getLoginUserData(this).getName());
        initRetrofitApi();
        buttonBookAppointment.setOnClickListener(this);
        textViewSelectDate.setOnClickListener(this);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Appointment Date");
        datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                textViewSelectDate.setText(datePicker.getHeaderText());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBookAppointment :
                String patientName = textInputPatientName.getEditText().getText().toString().trim();
                String doctorName = textInputDoctorName.getEditText().getText().toString().trim();
                String hospitalName = textInputHospitalName.getEditText().getText().toString().trim();
                String disease = textInputDisease.getEditText().getText().toString().trim();
                String age = textInputAge.getEditText().getText().toString().trim();
                String date = textViewSelectDate.getText().toString().trim();

                if (patientName.isEmpty()) {
                    textInputPatientName.setError("Name is Mandatory");
                    textInputPatientName.requestFocus();
                    return;
                }
                if (disease.isEmpty()) {
                    textInputDisease.setError("Please Specify Problem");
                    textInputDisease.requestFocus();
                    return;
                }
                if (age.isEmpty()) {
                    textInputAge.setError("Age is Mandatory");
                    textInputAge.requestFocus();
                    return;
                }
                if (date.isEmpty()) {
                    textViewSelectDate.setError("Please Select Date");
                    textViewSelectDate.requestFocus();
                    return;
                }
                if (hospitalName.isEmpty()) {
                    textInputHospitalName.setError("Hospital Name is Mandatory");
                    textInputHospitalName.requestFocus();
                    return;
                }
                if (doctorName.isEmpty()) {
                    textInputDoctorName.setError("Doctor Name is Mandatory");
                    textInputDoctorName.requestFocus();
                    return;
                }

                HelperClass.showProgressbar(progressBar);
                ModelRequestId patientId = SharedPref.getLoginUserData(BookAppointmentActivity.this).getPatientId();
                ModelRequestId doctorId = new ModelRequestId();
                doctorId.setId("5eeefa85186559d3e9e20301");

                ModelAppointment appointment = new ModelAppointment(patientName, doctorName, hospitalName, disease, age, date,
                        getString(R.string.pending), null, patientId, doctorId, null);

                call = networkApi.bookAppointment(appointment);
                presenter.bookAppointment(call, this, progressBar);
                break;

            case R.id.textViewSelectDate :
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                break;
        }
    }
}
