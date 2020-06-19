package com.scout.patient.ui.DoctorsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.CustomApplication;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.scout.patient.data.Remote.RetrofitNetworkApi;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

public class DoctorsActivity extends AppCompatActivity implements Contract.View{
    @Inject
    Retrofit retrofit;
    RetrofitNetworkApi networkApi;
    Call<ArrayList<ModelDoctorInfo>> call;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.image_back)
    ImageView back;
    @BindView(R.id.image_search)
    SearchView searchView;

    public static ArrayList<ModelDoctorInfo> list = new ArrayList<>();
    DoctorsAdapter adapter;
    Unbinder unbinder;
    DoctorsActivityPresenter presenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        call.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        unbinder = ButterKnife.bind(this);

        initSearchView();
        presenter = new DoctorsActivityPresenter(DoctorsActivity.this);
        initRetrofit();
        initRecyclerView(list);
        call = networkApi.getDoctorsList();
        presenter.loadDoctorslist(this,call,progressBar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initSearchView() {
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
    }

    private void initRecyclerView(ArrayList<ModelDoctorInfo> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(DoctorsActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.hasFixedSize();
        adapter = new DoctorsAdapter(list,DoctorsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void initRetrofit() {
        ((CustomApplication) getApplication()).getNetworkComponent().injectDoctorsActivity(DoctorsActivity.this);
        networkApi = retrofit.create(RetrofitNetworkApi.class);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }
}
