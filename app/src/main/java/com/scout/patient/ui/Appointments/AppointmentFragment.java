package com.scout.patient.ui.Appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scout.patient.Adapters.AppointmentsAdapter;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.data.Models.ModelRequestId;
import com.scout.patient.data.Prefs.SharedPref;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitNetworkApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class AppointmentFragment extends Fragment implements Contract.View{
    RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelAppointment>> call;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static ArrayList<ModelAppointment> list;
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
        presenter = new AppointmentPresenter(AppointmentFragment.this);
        unbinder = ButterKnife.bind(this,view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Appointments");

        setPatientId();
        initRetrofitApi();
        initRecyclerView(list);
        presenter.loadAppointments(getContext(),call,progressBar);
        return view;
    }

    private void setPatientId() {
       // patientId = SharedPref.getLoginUserData(getContext()).getPatientId().getId();
        patientId = "5eeef82a186559d3e9e20300";
    }

    private void initRecyclerView(ArrayList<ModelAppointment> list) {
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
}
