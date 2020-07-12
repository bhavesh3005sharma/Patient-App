package com.scout.patient.ui.Hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scout.patient.Adapters.ChipsAdapter;
import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.Adapters.HospitalsAdapter;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalActivity extends AppCompatActivity implements Contract.View,HospitalsAdapter.interfaceClickListener, ChipsAdapter.interfaceClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    public static ArrayList<ModelHospitalInfo> list = new ArrayList<>();
    HospitalsAdapter adapter;
    Unbinder unbinder;
    HospitalsPresenter presenter;

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
        setToolbar();

        presenter = new HospitalsPresenter(HospitalActivity.this);
        initRecyclerView();
        presenter.getHospitalsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu,menu);

        MenuItem search = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search Here!");
        searchView.setBackgroundColor(Color.WHITE);

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

    private void setToolbar() {
        if (toolbar!=null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    onBackPressed();
                }
            });
        }
        collapsingToolbar.setTitleEnabled(false);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(HospitalActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        adapter = new HospitalsAdapter(list,HospitalActivity.this);
        adapter.setUpOnClickListener(HospitalActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void holderClick(int position) {
        Intent intent = new Intent(this,DoctorsActivity.class);
        ModelIntent modelIntent = new ModelIntent(null,null,list.get(position).getHospitalDoctors(),true);
        intent.putExtra("modelIntent",modelIntent);
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
    public void updateSuccessUi(ArrayList<ModelHospitalInfo> data) {
        if (progressBar!=null)
            HelperClass.hideProgressbar(progressBar);
        list.clear();
        list.addAll(data);
        notifyAdapter();
    }
}
