package com.scout.patient.ui.Appointments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.scout.patient.Adapters.AppointmentsAdapter;
import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentActivity;
import com.scout.patient.ui.AppointmentBooking.BookAppointmentPresenter;

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
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems, startingIndex=-1;

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

        initRecyclerView();
        list.clear();
        shimmerLayout.startShimmer();
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
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.hasFixedSize();
        adapter = new AppointmentsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setUpOnClickListener(AppointmentActivity.this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems) && startingIndex!=-1)
                {
                    isScrolling = false;
                    HelperClass.showProgressbar(progressBar);
                    presenter.loadAppointments(startingIndex);
                }
            }
        });
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        if (progressBar!=null)
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,message);
    }

    @Override
    public void addDataToList(ArrayList<ModelAppointment> appointmentArrayList, int newStartingIndex) {
        if (list!=null)
            list.addAll(appointmentArrayList);
        adapter.notifyDataSetChanged();
        startingIndex = newStartingIndex;
        if (shimmerLayout!=null) {
            shimmerLayout.setVisibility(View.GONE);
            shimmerLayout.stopShimmer();
        }
        if (progressBar!=null)
         HelperClass.hideProgressbar(progressBar);
    }

    @Override
    public void onRefresh() {
        list.clear();
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        presenter.loadAppointmentsIdsList(this);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appointment_activity_menu,menu);
        
        MenuItem search = menu.findItem(R.id.search_bar);
        MenuItem pending = menu.findItem(R.id.pending);
        MenuItem accepted = menu.findItem(R.id.accepted);
        MenuItem rejected = menu.findItem(R.id.rejected);
        MenuItem completed = menu.findItem(R.id.completed);
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
    public void holderClick(int position) {
        openAppointmentDetails(position);
    }

    private void openAppointmentDetails(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        View view = LayoutInflater.from(this).inflate(R.layout.dialogue_appointment_details, null, false);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView doctorHospitalName = view.findViewById(R.id.textViewDoctorHospitalName);
        TextView patientName = view.findViewById(R.id.textViewName);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        TextView textViewAge = view.findViewById(R.id.textViewAge);
        TextView textViewDisease = view.findViewById(R.id.textViewDisease);
        TextView textViewSerialNo = view.findViewById(R.id.textViewSerialNo);
        TextView textViewStatus = view.findViewById(R.id.textViewStatus);
        TextView buttonBookAppointment = view.findViewById(R.id.buttonBookAppointment);

        ModelAppointment appointment = list.get(position);
        doctorHospitalName.setText(appointment.getDoctorName()+"\n("+appointment.getHospitalName()+")");
        patientName.setText(appointment.getPatientName());
        textViewDate.setText(appointment.getAppointmentDate());
        textViewTime.setText(appointment.getAppointmentTime());
        textViewAge.setText(appointment.getAge());
        textViewDisease.setText(appointment.getDisease());
        if(appointment.getSerialNumber()!=null && !appointment.getSerialNumber().equals("")){
            textViewSerialNo.setText(appointment.getSerialNumber());
            textViewSerialNo.setVisibility(View.VISIBLE);
        }

        if (appointment.getStatus().equals(getString(R.string.accepted))){
            textViewStatus.setText(R.string.accepted);
            textViewStatus.setTextColor(Color.WHITE);
            textViewStatus.setBackgroundResource(R.drawable.accepted_backgrounded);
        }
        if (appointment.getStatus().equals(getString(R.string.rejected))){
            textViewStatus.setText(R.string.rejected);
            textViewStatus.setTextColor(Color.WHITE);
            textViewStatus.setBackgroundResource(R.drawable.rejected_backgrounded);
        }
        if (appointment.getStatus().equals(getString(R.string.pending))){
            textViewStatus.setText(R.string.pending);
            textViewStatus.setTextColor(Color.BLACK);
            textViewStatus.setBackgroundResource(R.drawable.pending_backgrounded);
        }

        if (appointment.getStatus().equals(getString(R.string.completed))){
            buttonBookAppointment.setVisibility(View.VISIBLE);
            buttonBookAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AppointmentActivity.this, BookAppointmentActivity.class));
                }
            });
        }
    }
}
