package com.scout.patient.ui.Appointments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.scout.patient.Adapters.AppointmentsAdapter;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelBookAppointment;
import com.scout.patient.data.Prefs.SharedPref;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitNetworkApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class AppointmentFragment extends Fragment implements Contract.View,SwipeRefreshLayout.OnRefreshListener{
    RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelAppointment>> call;
    public static boolean isCallRunning = false;

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        setHasOptionsMenu(true);
        presenter = new AppointmentPresenter(AppointmentFragment.this);
        unbinder = ButterKnife.bind(this,view);
        setToolbar();

        setPatientId();
        initRetrofitApi();
        initRecyclerView();
        presenter.loadAppointments(getContext(),call,progressBar);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Appointments");
    }

    private void setPatientId() {
        patientId = SharedPref.getLoginUserData(getContext()).getPatientId().getId();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        adapter = new AppointmentsAdapter(getContext(),list);
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
            presenter.loadAppointments(getContext(), call, progressBar);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view_menu,menu);

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

        super.onCreateOptionsMenu(menu, inflater);
    }
}
