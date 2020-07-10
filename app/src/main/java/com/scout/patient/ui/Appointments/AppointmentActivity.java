package com.scout.patient.ui.Appointments;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.scout.patient.Adapters.AppointmentsAdapter;
import com.scout.patient.R;
import com.scout.patient.Models.ModelAppointment;
import com.scout.patient.Repository.Remote.RetrofitNetworkApi;
import com.scout.patient.Repository.Prefs.*;
import com.scout.patient.Retrofit.*;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class AppointmentActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener {
    RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelAppointment>> call;
    static boolean isCallRunning = false;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static ArrayList<ModelAppointment> list = new ArrayList<>();
    String patientId;
    AppointmentsAdapter adapter;
    Unbinder unbinder;
    AppointmentPresenter presenter;

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

        setPatientId();
        initRetrofitApi();
        initRecyclerView();
        presenter.loadAppointments(this, call, progressBar);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setToolbar() {
        getSupportActionBar().setTitle("Your Appointments");
    }

    private void setPatientId() {
        patientId = SharedPref.getLoginUserData(this).getPatientId().getId();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.hasFixedSize();
        adapter = new AppointmentsAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
        call = networkApi.getAppointments(patientId);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Call<ArrayList<ModelAppointment>> call = null;
        if (!isCallRunning) {
            call = networkApi.getAppointments(patientId);
            presenter.loadAppointments(this, call, progressBar);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_menu,menu);
        
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
}
