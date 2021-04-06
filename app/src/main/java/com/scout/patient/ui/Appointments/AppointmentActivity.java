package com.scout.patient.ui.Appointments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.scout.patient.Adapters.AppointmentsAdapter;
import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentActivity;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AppointmentActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener ,AppointmentsAdapter.interfaceClickListener{
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.shimmerLayout) ShimmerFrameLayout shimmerLayout;

    public static ArrayList<ModelAppointment> list = new ArrayList<ModelAppointment>();
    AppointmentsAdapter adapter;
    Unbinder unbinder;
    AppointmentPresenter presenter;
    Boolean isScrolling = false, isLoading = false;
    int currentItems, totalItems, scrollOutItems, startingIndex=-1;
    int checkedMenuItemId = R.id.no_filter;
    LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        presenter = new AppointmentPresenter(AppointmentActivity.this);
        unbinder = ButterKnife.bind(this);
        setToolbar();

        list.clear();
        initRecyclerView();
        shimmerLayout.startShimmer();
        isLoading = true;
        Log.d("getApp","loadAppointmentsIdsList");
        presenter.loadAppointmentsIdsList(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setToolbar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_appointments));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(manager);
        recyclerView.hasFixedSize();
        adapter = new AppointmentsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setUpOnClickListener(AppointmentActivity.this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                        check_loadMoreData();
                }
            }
        });
    }

    public void check_loadMoreData() {
        currentItems = manager.getChildCount();
        totalItems = manager.getItemCount();
        scrollOutItems = manager.findFirstVisibleItemPosition();

        if (isScrolling && ((currentItems + scrollOutItems == totalItems) || scrollOutItems == -1) )
            loadMoreData();
    }

    @Override
    public void onError(String message) {
        if (progressBar!=null)
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,message);
        isLoading = false;
    }

    @Override
    public void addDataToList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex) {
        if (list!=null && appointmentArrayList!=null)
            list.addAll(appointmentArrayList);

        startingIndex = newStartingIndex;
        if (shimmerLayout!=null) {
            shimmerLayout.setVisibility(View.GONE);
            shimmerLayout.stopShimmer();
        }
        if (progressBar!=null)
         HelperClass.hideProgressbar(progressBar);
        isLoading = false;
        Log.d("getApp",list.size()+"");
        switch (checkedMenuItemId){
            case R.id.pending :
                adapter.getFilter().filter(getString(R.string.pending));
                break;
            case R.id.completed :
                adapter.getFilter().filter(getString(R.string.completed));
                break;
            case R.id.accepted :
                adapter.getFilter().filter(getString(R.string.accepted));
                break;
            case R.id.rejected :
                adapter.getFilter().filter(getString(R.string.rejected));
                break;
            case R.id.no_filter :
                adapter.getFilter().filter("");
                break;
            case R.id.not_attempted :
                adapter.getFilter().filter(getString(R.string.not_attempted));
                break;
        }
    }

    @Override
    public void updateDoctorDetails(ModelDoctorInfo doctorInfo, ModelAppointment appointment) {
        ModelIntent modelIntent = new ModelIntent();
        modelIntent.setDoctorProfileInfo(doctorInfo);
        modelIntent.setBookAppointmentData(new ModelBookAppointment(appointment.getPatientName(),appointment.getDisease(),appointment.getAge()));
        Intent intent = new Intent(AppointmentActivity.this, BookAppointmentActivity.class);
        intent.putExtra("modelIntent",modelIntent);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            list.clear();
            isLoading= true;
            adapter.notifyDataSetChanged();
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();
            presenter.loadAppointmentsIdsList(this);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        checkedMenuItemId = item.getItemId();
        switch (item.getItemId()){
            case R.id.pending :
                 adapter.getFilter().filter(getString(R.string.pending));
                 break;
            case R.id.completed :
                adapter.getFilter().filter(getString(R.string.completed));
                break;
            case R.id.accepted :
                adapter.getFilter().filter(getString(R.string.accepted));
                break;
            case R.id.rejected :
                adapter.getFilter().filter(getString(R.string.rejected));
                break;
            case R.id.no_filter :
                adapter.getFilter().filter("");
                break;
            case R.id.not_attempted :
                adapter.getFilter().filter(getString(R.string.not_attempted));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_activity_menu,menu);
        
        MenuItem search = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void holderClick(ModelAppointment appointment) {
        openAppointmentDetails(appointment);
    }

    @Override
    public void loadMoreData() {
        if ( startingIndex != -1 && !isLoading) {
            Log.d("getAppointmentsList","loading Data..");
            isLoading = true;
            isScrolling = false;
            HelperClass.showProgressbar(progressBar);
            presenter.loadAppointments(startingIndex);
        }
    }

    private void openAppointmentDetails(ModelAppointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        View view = LayoutInflater.from(this).inflate(R.layout.dialogue_appointment_details, null, false);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(null);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView textViewAppointmentId = view.findViewById(R.id.textViewAppointmentId);
        TextView doctorHospitalName = view.findViewById(R.id.textViewDoctorHospitalName);
        TextView patientName = view.findViewById(R.id.textViewName);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView textViewAge = view.findViewById(R.id.textViewAge);
        TextView textViewDisease = view.findViewById(R.id.textViewDisease);
        TextView textViewSerialNo = view.findViewById(R.id.textViewSerialNo);
        TextView textViewStatus = view.findViewById(R.id.textViewStatus);
        TextView buttonBookAppointment = view.findViewById(R.id.buttonBookAppointment);

        textViewAppointmentId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked","copied");
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Appointment Id", appointment.getAppointmentId().getId());
                clipboard.setPrimaryClip(clip);
                HelperClass.toast(AppointmentActivity.this,"Appointment Id Copied to Clipboard");
            }
        });

        //textViewAppointmentId.setText("AppointmentId : "+appointment.getAppointmentId().getId());
        doctorHospitalName.setText(appointment.getDoctorName()+"\n("+appointment.getHospitalName()+")");
        patientName.setText(appointment.getPatientName());
        textViewDate.setText(appointment.getAppointmentDate());
        textViewTime.setText(appointment.getAppointmentTime());
        textViewAge.setText(appointment.getAge()+" years");
        textViewDisease.setText(appointment.getDisease());
        if(appointment.getSerialNumber()!=null && !appointment.getSerialNumber().equals("")){
            textViewSerialNo.setText(getString(R.string.your_serial_number)+" "+appointment.getSerialNumber());
            textViewSerialNo.setVisibility(View.VISIBLE);
        }

        if (appointment.getStatus().equals(getString(R.string.accepted)) || appointment.getStatus().equals(getString(R.string.completed))){
            textViewStatus.setText(appointment.getStatus());
            textViewStatus.setTextColor(Color.WHITE);
            textViewStatus.setBackgroundResource(R.drawable.accepted_backgrounded);
        }
        if (appointment.getStatus().equals(getString(R.string.rejected))){
            textViewStatus.setText(appointment.getStatus());
            textViewStatus.setTextColor(Color.WHITE);
            textViewStatus.setBackgroundResource(R.drawable.rejected_backgrounded);
        }
        if (appointment.getStatus().equals(getString(R.string.pending)) || appointment.getStatus().equals(getString(R.string.not_attempted))){
            textViewStatus.setText(appointment.getStatus());
            textViewStatus.setTextColor(Color.BLACK);
            textViewStatus.setBackgroundResource(R.drawable.pending_backgrounded);
        }

        if (!appointment.getStatus().equals(getString(R.string.accepted)) && !appointment.getStatus().equals(getString(R.string.pending))){
            buttonBookAppointment.setVisibility(View.VISIBLE);
            buttonBookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.getDoctorProfileData(appointment.getDoctorId().getId(),appointment);
                }
            });
        }
    }
}
