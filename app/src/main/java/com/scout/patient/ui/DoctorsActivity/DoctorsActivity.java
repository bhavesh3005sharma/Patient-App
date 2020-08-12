package com.scout.patient.ui.DoctorsActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DoctorsActivity extends AppCompatActivity implements Contract.View,DoctorsAdapter.interfaceClickListener{//,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static ArrayList<ModelKeyData> list = new ArrayList<ModelKeyData>();
    DoctorsAdapter adapter;
    Unbinder unbinder;
    DoctorsActivityPresenter presenter;
    ModelIntent modelIntent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        unbinder = ButterKnife.bind(this);

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        presenter = new DoctorsActivityPresenter(DoctorsActivity.this);
        initRecyclerView();
        if (modelIntent!=null && modelIntent.isIntentFromHospital())
            presenter.loadDoctorsList(modelIntent.getListOfDoctors());
        else
            presenter.loadDoctorsList();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(DoctorsActivity.this,2));
        recyclerView.hasFixedSize();
        adapter = new DoctorsAdapter(list,DoctorsActivity.this);
        adapter.setUpOnClickListener(DoctorsActivity.this);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void holderClick(int position) {
        Intent intent = new Intent(this, DoctorsProfileActivity.class);
//        if (modelIntent==null)
//            modelIntent = new ModelIntent();
        //modelIntent.setDoctorProfileInfo(list.get(position));
        intent.putExtra("id",list.get(position).getId().getId());
        startActivity(intent);
//        if (modelIntent.getBookAppointmentData()!=null)
//            finish();
    }
}
