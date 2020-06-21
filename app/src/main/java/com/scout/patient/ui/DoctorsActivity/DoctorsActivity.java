package com.scout.patient.ui.DoctorsActivity;

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
import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.data.Remote.ApiService;
import com.scout.patient.data.Remote.RetrofitNetworkApi;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

public class DoctorsActivity extends AppCompatActivity implements Contract.View{
    RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelDoctorInfo>> call;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static ArrayList<ModelDoctorInfo> list = new ArrayList<>();
    DoctorsAdapter adapter;
    Unbinder unbinder;
    DoctorsActivityPresenter presenter;

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

        presenter = new DoctorsActivityPresenter(DoctorsActivity.this);
        initRetrofitApi();
        initRecyclerView(list);
        presenter.loadDoctorslist(this,call,progressBar);
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
//        if (toolbar!=null) {
//            setSupportActionBar(toolbar);
//            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_left));
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //What to do on back clicked
//                    onBackPressed();
//                }
//            });
//        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Check Our Doctors");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initRecyclerView(ArrayList<ModelDoctorInfo> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(DoctorsActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        adapter = new DoctorsAdapter(list,DoctorsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void initRetrofitApi() {
        networkApi = ApiService.getAPIService();
        call = networkApi.getDoctorsList();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }
}
