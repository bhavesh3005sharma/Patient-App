package com.scout.patient.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scout.patient.Adapters.SearchAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.interfaceClickListener{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Unbinder unbinder;
    SearchAdapter adapter;
    String key;
    ModelIntent modelIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder = ButterKnife.bind(this);
        initToolbar("Search..");

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        if (modelIntent==null) {
            modelIntent = new ModelIntent();
        }
        key = getIntent().getStringExtra("key");

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.hasFixedSize();
        adapter = new SearchAdapter(this,key);
        recyclerView.setAdapter(adapter);
        adapter.setUpOnClickListener(SearchActivity.this);
    }

    public void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view_menu,menu);

        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.search_bar));

        SearchManager searchManager = (SearchManager) this.getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //changing edittext color

        EditText searchEdit = ((EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        searchEdit.setTextColor(Color.BLACK);
        searchEdit.setHintTextColor(Color.BLACK);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search By Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchActivity.this,SearchResultsActivity.class);
                intent.putExtra("list",adapter.getFilteredList());
                intent.putExtra("modelIntent",modelIntent);
                startActivity(intent);
                if (modelIntent.getBookAppointmentData()!=null)
                    finish();
                return true;
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
    public void holderClick(ModelKeyData modelKeyData) {
        if (modelKeyData.isHospital()){
            Intent intent = new Intent(SearchActivity.this, HospitalProfileActivity.class);
            intent.putExtra("hospitalId",modelKeyData.getId().getId());
            intent.putExtra("hospitalName",modelKeyData.getName());
            modelIntent.setIntentFromHospital(true);
            intent.putExtra("modelIntent",modelIntent);
            startActivity(intent);
            if (modelIntent.getBookAppointmentData()!=null)
                finish();
        }else {
            Intent intent = new Intent(SearchActivity.this, DoctorsProfileActivity.class);
            intent.putExtra("doctorId", modelKeyData.getId().getId());
            intent.putExtra("doctorName", modelKeyData.getName());
            modelIntent.setIntentFromHospital(false);
            intent.putExtra("modelIntent",modelIntent);
            startActivity(intent);
            if (modelIntent.getBookAppointmentData()!=null)
                finish();
        }
    }
}
