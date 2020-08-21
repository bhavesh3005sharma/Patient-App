package com.scout.patient.ui.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scout.patient.Adapters.SearchResultsAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.interfaceClickListener{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.image_no_data)
    ImageView imageNoData;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.textViewSearch)
    TextView textViewSearch;

    public static ArrayList<ModelKeyData> list = new ArrayList<ModelKeyData>();
    SearchResultsAdapter adapter;
    Unbinder unbinder;
    ModelIntent modelIntent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        unbinder = ButterKnife.bind(this);

        list.clear();
        if (getIntent().getSerializableExtra("list")!=null)
        list.addAll((ArrayList<ModelKeyData>)getIntent().getSerializableExtra("list"));
        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");

        setToolbar();
        checkForDataAndDecide();

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultsActivity.this, SearchActivity.class).putExtra("key",getString(R.string.only_doctors)));
                finish();
            }
        });
    }

    private void checkForDataAndDecide() {
        if (list.isEmpty()){
            imageNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            imageNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initRecyclerView();
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.our_doctors));
        collapsingToolbarLayout.setTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(SearchResultsActivity.this,2));
        recyclerView.hasFixedSize();
        adapter = new SearchResultsAdapter(list,SearchResultsActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.setUpOnClickListener(SearchResultsActivity.this);
    }

    @Override
    public void holderClick(ModelKeyData modelKeyData) {
        if (modelKeyData.getStrId()!=null) {
            if (modelKeyData.isHospital()) {
                Intent intent = new Intent(SearchResultsActivity.this, HospitalProfileActivity.class);
                intent.putExtra("hospitalId", modelKeyData.getStrId());
                intent.putExtra("hospitalName", modelKeyData.getName());
                modelIntent.setIntentFromHospital(true);
                intent.putExtra("modelIntent", modelIntent);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SearchResultsActivity.this, DoctorsProfileActivity.class);
                intent.putExtra("doctorId", modelKeyData.getStrId());
                intent.putExtra("doctorName", modelKeyData.getName());
                modelIntent.setIntentFromHospital(false);
                intent.putExtra("modelIntent", modelIntent);
                startActivity(intent);
                finish();
            }
        }
    }
}
