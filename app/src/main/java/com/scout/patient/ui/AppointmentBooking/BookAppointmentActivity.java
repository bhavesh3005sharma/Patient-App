package com.scout.patient.ui.AppointmentBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Repository.Prefs.*;
import com.scout.patient.Retrofit.*;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener ,Contract.View{
    @BindView(R.id.textInputPatientName)
    TextInputLayout textInputPatientName;
    @BindView(R.id.cardDoctorInfo)
    CardView cardDoctorInfo;
    @BindView(R.id.text_doctor_name)
    TextView textInputDoctorName;
    @BindView(R.id.text_specialization)
    TextView textSpecialisation;
    @BindView(R.id.textPhoneNo)
    TextView textPhoneNo;
    @BindView(R.id.textViewSelectDoctor)
    TextView textViewSelectDoctor;
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
    ModelIntent modelIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        unbinder = ButterKnife.bind(this);

        getSupportActionBar().setTitle("Book Your Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        presenter = new BookAppointmentPresenter(BookAppointmentActivity.this);

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        setUpUi();

        initRetrofitApi();
        buttonBookAppointment.setOnClickListener(this);
        textViewSelectDate.setOnClickListener(this);
        textViewSelectDoctor.setOnClickListener(this);

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

    private void setUpUi() {
        if (modelIntent.getBookAppointmentData()!=null) {
            textInputPatientName.getEditText().setText(modelIntent.getBookAppointmentData().getPatientName());
            textInputDisease.getEditText().setText(modelIntent.getBookAppointmentData().getDisease());
            textInputAge.getEditText().setText(modelIntent.getBookAppointmentData().getAge());
            textViewSelectDate.setText(modelIntent.getBookAppointmentData().getAppointmentDate());
        }else
            textInputPatientName.getEditText().setText(SharedPref.getLoginUserData(this).getName());

        if(modelIntent.getDoctorProfileInfo()!=null){
            cardDoctorInfo.setVisibility(View.VISIBLE);
            textInputDoctorName.setText(modelIntent.getDoctorProfileInfo().getName());
            textSpecialisation.setText(modelIntent.getDoctorProfileInfo().getDepartment());
            textPhoneNo.setText(modelIntent.getDoctorProfileInfo().getPhone_no());
            textViewSelectDoctor.setText("Change Doctor");
        }else
            cardDoctorInfo.setVisibility(View.GONE);
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
                String doctorName = textInputDoctorName.getText().toString().trim();
                //String hospitalName = null;
                String disease = textInputDisease.getEditText().getText().toString().trim();
                String age = textInputAge.getEditText().getText().toString().trim();
                String date = textViewSelectDate.getText().toString().trim();

                if (patientName.isEmpty()) {
                    textInputPatientName.setError("Name is Mandatory");
                    textInputPatientName.requestFocus();
                    return;
                }else textInputPatientName.setError(null);

                if (disease.isEmpty()) {
                    textInputDisease.setError("Please Specify Problem");
                    textInputDisease.requestFocus();
                    return;
                }else textInputDisease.setError(null);

                if (age.isEmpty()) {
                    textInputAge.setError("Age is Mandatory");
                    textInputAge.requestFocus();
                    return;
                } else textInputAge.setError(null);

                if (date.equals(getString(R.string.select_date))) {
                    HelperClass.toast(this,"Please Select Date");
                    return;
                }

                HelperClass.showProgressbar(progressBar);
                String patientId = SharedPref.getLoginUserData(BookAppointmentActivity.this).getPatientId().getId();
                String doctorId = modelIntent.getDoctorProfileInfo().getDoctorId().getId();

                ModelBookAppointment appointment = new ModelBookAppointment(patientName, doctorName, "", disease, age, date,
                        getString(R.string.pending), "", patientId, doctorId, null);

                call = networkApi.bookAppointment(appointment);
                presenter.bookAppointment(call, this, progressBar);
                break;

            case R.id.textViewSelectDate :
                if (!datePicker.isAdded())
                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                break;
            case R.id.textViewSelectDoctor :
                String patientName1 = textInputPatientName.getEditText().getText().toString().trim();
                String disease1 = textInputDisease.getEditText().getText().toString().trim();
                String age1 = textInputAge.getEditText().getText().toString().trim();
                String date1 = textViewSelectDate.getText().toString().trim();

                if (patientName1.isEmpty()) {
                    textInputPatientName.setError("Name is Mandatory");
                    textInputPatientName.requestFocus();
                    return;
                }else textInputPatientName.setError(null);

                if (disease1.isEmpty()) {
                    textInputDisease.setError("Please Specify Problem");
                    textInputDisease.requestFocus();
                    return;
                }else textInputDisease.setError(null);

                if (age1.isEmpty()) {
                    textInputAge.setError("Age is Mandatory");
                    textInputAge.requestFocus();
                    return;
                } else textInputAge.setError(null);

                if (date1.equals(getString(R.string.select_date))) {
                    HelperClass.toast(this,"Please Select Date");
                    return;
                }

                ModelBookAppointment modelBookAppointment = new ModelBookAppointment(patientName1,disease1,age1,date1);
                modelIntent.setBookAppointmentData(modelBookAppointment);
                modelIntent.setIntentFromHospital(false);
                startActivity(new Intent(BookAppointmentActivity.this, DoctorsActivity.class).putExtra("modelIntent",modelIntent));
                finish();
                break;
        }
    }
}
