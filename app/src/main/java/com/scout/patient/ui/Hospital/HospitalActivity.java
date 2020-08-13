package com.scout.patient.ui.Hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.scout.patient.Adapters.ChipsAdapter;
import com.scout.patient.Adapters.HospitalsAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalActivity extends AppCompatActivity implements Contract.View,HospitalsAdapter.interfaceClickListener, ChipsAdapter.interfaceClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    public static ArrayList<ModelKeyData> list = new ArrayList<ModelKeyData>();
    HospitalsAdapter adapter;
    Unbinder unbinder;
    HospitalsPresenter presenter;
    ModelIntent modelIntent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        unbinder = ButterKnife.bind(this);

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        if (modelIntent==null)
            modelIntent = new ModelIntent();
        presenter = new HospitalsPresenter(HospitalActivity.this);
        initRecyclerView();
        presenter.getHospitalsList();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(HospitalActivity.this,2));
        recyclerView.hasFixedSize();
        adapter = new HospitalsAdapter(list,HospitalActivity.this);
        adapter.setUpOnClickListener(HospitalActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void holderClick(int position) {
        Intent intent = new Intent(this, HospitalProfileActivity.class);
//        modelIntent.setIntentFromHospital(true);
//        modelIntent.setListOfDoctors(list.get(position).getHospitalDoctors());
        intent.putExtra("hospitalId",list.get(position).getId().getId());
        intent.putExtra("hospitalName",list.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setErrorUi(String message) {
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,message);
    }

    @Override
    public void updateSuccessUi(ArrayList<ModelKeyData> data) {
        if (progressBar!=null)
            HelperClass.hideProgressbar(progressBar);
        list.clear();
        list.addAll(data);
        notifyAdapter();
    }
}
