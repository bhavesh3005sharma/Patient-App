package com.scout.patient.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.Adapters.SearchAdapter;
import com.scout.patient.Adapters.SearchResultsAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchResultsActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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
        list.addAll((ArrayList<ModelKeyData>)getIntent().getSerializableExtra("list"));

        setToolbar();
        initRecyclerView();
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchResultsActivity.this, SearchActivity.class).putExtra("key",getString(R.string.only_doctors)));
            }
        });
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
    }
}
