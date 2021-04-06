package com.scout.patient.ui.AppointmentBooking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDateTime;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;
import com.scout.patient.ui.Hospital.HospitalActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener ,Contract.View, DatePickerDialog.OnDateSetListener, ChipGroup.OnCheckedChangeListener{
    @BindView(R.id.textInputPatientName) TextInputLayout textInputPatientName;
    @BindView(R.id.cardDoctorInfo) CardView cardDoctorInfo;
    @BindView(R.id.text_doctor_name) TextView textInputDoctorName;
    @BindView(R.id.text_name) TextView textInputHospitalName;
    @BindView(R.id.text_specialization) TextView textSpecialisation;
    @BindView(R.id.textPhoneNo) TextView textPhoneNo;
    @BindView(R.id.selectionSpinner) Spinner spinner;
    @BindView(R.id.textInputDisease) TextInputLayout textInputDisease;
    @BindView(R.id.textInputAge) TextInputLayout textInputAge;
    @BindView(R.id.textViewSelectDate) TextView textViewSelectDate;
    @BindView(R.id.textViewInfoSelectDate) TextView textViewInfoSelectDate;
    @BindView(R.id.buttonBookAppointment) Button buttonBookAppointment;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.choice_chip_group) ChipGroup chipGroup;

    Unbinder unbinder;
    int check=0;
    Boolean isOpeningDatePicker = false;
    BookAppointmentPresenter presenter;
    ModelIntent modelIntent;
    String selectedTime = null;
    DatePickerDialog datePickerDialog;
    ArrayList<ModelDateTime> partiallyUnavailableDates = new ArrayList<>();

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
        if (modelIntent==null)
            modelIntent = new ModelIntent();
        else
            spinner.setEnabled(false);

        setUpUi();
        chipGroup.setOnCheckedChangeListener(this);

        buttonBookAppointment.setOnClickListener(this);
        textViewSelectDate.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check>1) {
                    if (position == 1)
                        onDoctorSelected();
                    if (position == 2)
                        onHospitalSelected();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("onNothingSelected","");
            }
        });
    }

    private void onHospitalSelected() {
        String patientName2 = textInputPatientName.getEditText().getText().toString().trim();
                String disease2 = textInputDisease.getEditText().getText().toString().trim();
                String age2 = textInputAge.getEditText().getText().toString().trim();

        if (isValidData(patientName2,disease2,age2)){
            ModelBookAppointment modelBookAppointment1 = new ModelBookAppointment(patientName2,disease2,age2);

            modelIntent.setBookAppointmentData(modelBookAppointment1);
            modelIntent.setIntentFromHospital(true);
            startActivity(new Intent(BookAppointmentActivity.this, HospitalActivity.class).putExtra("modelIntent",modelIntent));
            finish();
        }else
            spinner.setSelection(0);
    }

    private void onDoctorSelected() {
        String patientName1 = textInputPatientName.getEditText().getText().toString().trim();
        String disease1 = textInputDisease.getEditText().getText().toString().trim();
        String age1 = textInputAge.getEditText().getText().toString().trim();

        if (isValidData(patientName1,disease1,age1)){
            ModelBookAppointment modelBookAppointment = new ModelBookAppointment(patientName1,disease1,age1);

            modelIntent.setBookAppointmentData(modelBookAppointment);
            modelIntent.setIntentFromHospital(false);
            startActivity(new Intent(BookAppointmentActivity.this, DoctorsActivity.class).putExtra("modelIntent",modelIntent));
            finish();
        }else
            spinner.setSelection(0);
    }

    public void openDatePicker(){
        isOpeningDatePicker = true;
        Calendar now = Calendar.getInstance();
         datePickerDialog = DatePickerDialog.newInstance(
                BookAppointmentActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
         HelperClass.showProgressbar(progressBar);
         presenter.getAppointmentDates(modelIntent.getDoctorProfileInfo());
    }

    private void setUpUi() {
        if (modelIntent!=null && modelIntent.getBookAppointmentData()!=null) {
            textInputPatientName.getEditText().setText(modelIntent.getBookAppointmentData().getPatientName());
            textInputDisease.getEditText().setText(modelIntent.getBookAppointmentData().getDisease());
            textInputAge.getEditText().setText(modelIntent.getBookAppointmentData().getAge());
            if (modelIntent.getBookAppointmentData().getAppointmentDate()!=null)
                textViewSelectDate.setText(modelIntent.getBookAppointmentData().getAppointmentDate());
        }else {
            textInputPatientName.getEditText().setText(SharedPref.getLoginUserData(this).getName());
        }

        if(modelIntent!=null && modelIntent.getDoctorProfileInfo()!=null){
            cardDoctorInfo.setVisibility(View.VISIBLE);
            textViewSelectDate.setVisibility(View.VISIBLE);
            textViewInfoSelectDate.setVisibility(View.VISIBLE);
            textViewSelectDate.setText(getString(R.string.select_date));
            textInputHospitalName.setText(modelIntent.getDoctorProfileInfo().getHospitalName());
            textInputDoctorName.setText(modelIntent.getDoctorProfileInfo().getName());
            textSpecialisation.setText(modelIntent.getDoctorProfileInfo().getDepartment());
            textPhoneNo.setText(modelIntent.getDoctorProfileInfo().getPhone_no());
            if (modelIntent.isIntentFromHospital()!=null && modelIntent.isIntentFromHospital())
                spinner.setSelection(2);
            else spinner.setSelection(1);
        }else {
            cardDoctorInfo.setVisibility(View.GONE);
            textViewSelectDate.setVisibility(View.GONE);
            textViewInfoSelectDate.setVisibility(View.GONE);
            chipGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBookAppointment :
                String patientName = textInputPatientName.getEditText().getText().toString().trim();
                String doctorName = textInputDoctorName.getText().toString().trim();
                String disease = textInputDisease.getEditText().getText().toString().trim();
                String age = textInputAge.getEditText().getText().toString().trim();
                String date = textViewSelectDate.getText().toString().trim();

                if (isValidData(patientName,disease,age)){
                    if(modelIntent.getDoctorProfileInfo()==null){
                        HelperClass.toast(this, "Select Doctor for Appointment.");
                        return;
                    }
                    if (date.equals(getString(R.string.select_date)) || selectedTime==null) {
                        HelperClass.toast(this, "Select Date and Time.");
                        return;
                    }
                    HelperClass.showProgressbar(progressBar);
                    String patientId = SharedPref.getLoginUserData(BookAppointmentActivity.this).getPatientId().getId();
                    String doctorId = modelIntent.getDoctorProfileInfo().getDoctorId().getId();
                    String hospitalId = modelIntent.getDoctorProfileInfo().getHospitalObjectId().getId();
                    String hospitalName = modelIntent.getDoctorProfileInfo().getHospitalName();
                    String avgCheckupTime = modelIntent.getDoctorProfileInfo().getAvgCheckupTime();
                    long thresholdLimit = presenter.getThresholdLimit(selectedTime,avgCheckupTime);

                    ModelBookAppointment appointment = new ModelBookAppointment(patientName, doctorName, hospitalName, disease, age, date,
                            getString(R.string.pending), "", patientId, doctorId, hospitalId,selectedTime,thresholdLimit);

                    presenter.bookAppointment( appointment);
                }
                break;

            case R.id.textViewSelectDate :
                if (isOpeningDatePicker)
                    HelperClass.toast(BookAppointmentActivity.this,"Please Wait We Checking For Available Dates..");
                else
                    openDatePicker();;
                break;
        }
    }

    private boolean isValidData(String patientName, String disease, String age) {
        if (patientName.isEmpty()) {
            textInputPatientName.setError("Name is Mandatory");
            textInputPatientName.requestFocus();
            return false;
        }else textInputPatientName.setError(null);

        if (disease.isEmpty()) {
            textInputDisease.setError("Please Specify Problem");
            textInputDisease.requestFocus();
            return false;
        }else textInputDisease.setError(null);

        if (age.isEmpty()) {
            textInputAge.setError("Age is Mandatory");
            textInputAge.requestFocus();
            return false;
        }
        else if (Integer.parseInt(age)>120) {
            textInputAge.setHelperText("Please Provide Valid Age.");
            textInputAge.requestFocus();
            return false;
        } else textInputAge.setError(null);
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        month++;
        String dayOfMonth = ""+day;
        String monthOfYear = ""+month;
        if (dayOfMonth.length() == 1)
            dayOfMonth = "0" + dayOfMonth;

        if (monthOfYear.length()==1)
            monthOfYear = "0"+ month;
        String date = dayOfMonth+"-"+monthOfYear+"-"+year;
        textViewSelectDate.setText(date);

        setTime(date);
    }

    private void setTime(String date) {
        ArrayList CompleteTime = modelIntent.getDoctorProfileInfo().getDoctorAvailabilityTime();
        ArrayList AvailableTimes = new ArrayList();
        if (partiallyUnavailableDates.size()==0)
            AvailableTimes.addAll(CompleteTime);
        for(ModelDateTime dateTime : partiallyUnavailableDates){
            if(dateTime.getDate().equals(date)){
                ArrayList unAvailableTimes = dateTime.getUnavailableTimes();
                for(int i=0;i<CompleteTime.size();i++){
                    for(int j=0;j<unAvailableTimes.size();j++){
                        if(CompleteTime.get(i).equals(unAvailableTimes.get(j)))
                            break;
                        if(j==unAvailableTimes.size()-1)
                            AvailableTimes.add(CompleteTime.get(i));
                    }
                }
            }
        }
        if (AvailableTimes.size()==0)
            setChipGroup(CompleteTime);
        else
            setChipGroup(AvailableTimes);
    }

    private void setChipGroup(ArrayList<String> timeList) {
        chipGroup.setVisibility(View.VISIBLE);
        chipGroup.removeAllViews();

        for (String time : timeList){
            Chip chip = new Chip(BookAppointmentActivity.this);
            chip.setText(time);
            chip.setCheckedIconResource(R.drawable.ic_check);
            chip.setChipBackgroundColorResource(R.color.colorPrimary);
            chip.setTextColor(Color.WHITE);
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }
    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
        if (checkedId==-1) {
            selectedTime = null;
            return;
        }
        Chip chip = findViewById(checkedId);
        selectedTime = chip.getText().toString().trim();
    }

    @Override
    public void setUpDatePicker(Calendar[] availabilityDates, Calendar[] unAvailabilityDates, ArrayList<ModelDateTime> partiallyUnavailableDatesList) {
        Calendar now = Calendar.getInstance();
        datePickerDialog.setSelectableDays(availabilityDates);
        datePickerDialog.setDisabledDays(unAvailabilityDates);
        datePickerDialog.setMinDate(now);

        String schedule = modelIntent.getDoctorProfileInfo().getSchedule();
        if (schedule==null)
            schedule = getString(R.string.monthly);
        switch (schedule) {
            case "Weekly":
                now.add(Calendar.WEEK_OF_MONTH, 1);
                break;
            case "Daily":
                now.add(Calendar.DAY_OF_YEAR, 1);
                break;
            default:
                now.add(Calendar.MONTH, 1);
        }
        datePickerDialog.setMaxDate(now);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            datePickerDialog.setAccentColor(getColor(R.color.colorPrimary));
        }

        datePickerDialog.setOkColor(Color.BLUE);
        datePickerDialog.setCancelColor(Color.BLUE);
        datePickerDialog.show(getSupportFragmentManager(),"DATE_PICKER");
        HelperClass.hideProgressbar(progressBar);

        partiallyUnavailableDates.clear();
        partiallyUnavailableDates.addAll(partiallyUnavailableDatesList);
        isOpeningDatePicker = false;
    }

    @Override
    public void OnResponse(String message) {
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,message);
    }
}
