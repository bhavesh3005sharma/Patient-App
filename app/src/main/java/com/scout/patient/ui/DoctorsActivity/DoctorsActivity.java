package com.scout.patient.ui.DoctorsActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Models.ModelBookAppointment;
import com.scout.patient.Models.ModelDoctorInfo;
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
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static ArrayList<ModelDoctorInfo> list = new ArrayList<>();
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
        setToolbar();

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        presenter = new DoctorsActivityPresenter(DoctorsActivity.this);
        initRecyclerView();
        if (modelIntent!=null && modelIntent.isIntentFromHospital())
            presenter.loadDoctorsList(modelIntent.getListOfDoctors());
        else
            presenter.loadDoctorsList();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(DoctorsActivity.this,LinearLayoutManager.VERTICAL,false));
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
    public void updateSuccessUi(ArrayList<ModelDoctorInfo> data) {
        if (progressBar!=null)
        HelperClass.hideProgressbar(progressBar);
        list.clear();
        list.addAll(data);
        notifyAdapter();
    }

    @Override
    public void holderClick(int position) {
        Intent intent = new Intent(this, DoctorsProfileActivity.class);
        if (modelIntent==null)
            modelIntent = new ModelIntent();
        modelIntent.setDoctorProfileInfo(list.get(position));
        intent.putExtra("modelIntent",modelIntent);
        startActivity(intent);
        if (modelIntent.getBookAppointmentData()!=null)
            finish();
    }
}
