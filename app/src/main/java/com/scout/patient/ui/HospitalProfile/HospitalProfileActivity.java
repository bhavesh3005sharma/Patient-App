package com.scout.patient.ui.HospitalProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scout.patient.Adapters.DepartmentsAdapter;
import com.scout.patient.Adapters.DoctorsAdapter;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.R;
import com.scout.patient.Utilities.HelperClass;
import com.scout.patient.ui.DoctorsActivity.DoctorsActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HospitalProfileActivity extends AppCompatActivity implements Contract.View,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.textViewHospitalName)
    TextView textViewHospitalName;
    @BindView(R.id.year_establishment)
    TextView yearEstablishment;
    @BindView(R.id.address)
    TextView textViewAddress;
    @BindView(R.id.email)
    TextView textViewEmail;
    @BindView(R.id.contactNo)
    TextView textViewContactNo;
    @BindView(R.id.textViewDepartments)
    TextView textViewDepartments;
    @BindView(R.id.recyclerViewDepartments)
    RecyclerView recyclerView;
    @BindView(R.id.HospitalImage)
    ImageView HospitalImage;
    @BindView(R.id.buttonDoctors)
    Button buttonDoctors;
    @BindView(R.id.details)
    LinearLayout contactDetails;
    @BindView(R.id.cardViewImage)
    CardView cardViewImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    Unbinder unbinder;
    HospitalProfilePresenter presenter;
    ModelHospitalInfo hospitalInfo;
    String hospitalId;
    Boolean isLoading;
    DepartmentsAdapter adapter;
    ModelIntent modelIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);
        unbinder = ButterKnife.bind(this);
        presenter = new HospitalProfilePresenter(HospitalProfileActivity.this);

        hospitalId = getIntent().getStringExtra("hospitalId");
        setUpToolbar(getIntent().getStringExtra("hospitalName"));
        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        isLoading = true;
        presenter.getHospitalDetails(hospitalId);
        swipeRefreshLayout.setOnRefreshListener(this);

        buttonDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelIntent.setListOfDoctors(hospitalInfo.getHospitalDoctors());
                Intent intent = new Intent(HospitalProfileActivity.this, DoctorsActivity.class);
                intent.putExtra("modelIntent",modelIntent);
                intent.putExtra("hospitalName",getIntent().getStringExtra("hospitalName"));
                startActivity(intent);
                if (modelIntent.getBookAppointmentData()!=null)
                    finish();
            }
        });
    }

    private void setUpToolbar(String hospitalName) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(hospitalName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onSetUi(ModelHospitalInfo data) {
        if (data!=null) {
            hospitalInfo = data;
            textViewHospitalName.setText(hospitalInfo.getName());
            textViewEmail.setText(hospitalInfo.getEmail());
            textViewContactNo.setText(hospitalInfo.getPhone_no());
            textViewAddress.setText(hospitalInfo.getAddress());
            yearEstablishment.setText(getString(R.string.year_establishment)+hospitalInfo.getYear_of_establishment());
            if (hospitalInfo.getUrl()!=null)
                Picasso.get().load(Uri.parse(hospitalInfo.getUrl())).placeholder(R.color.placeholder_bg).into(HospitalImage);

            recyclerView.setLayoutManager(new LinearLayoutManager(HospitalProfileActivity.this,RecyclerView.VERTICAL,false));
            recyclerView.hasFixedSize();
            adapter = new DepartmentsAdapter(data.getDepartments(),HospitalProfileActivity.this);
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(View.VISIBLE);
            textViewDepartments.setVisibility(View.VISIBLE);
            cardViewImage.setVisibility(View.VISIBLE);
            contactDetails.setVisibility(View.VISIBLE);
            buttonDoctors.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String message) {
        HelperClass.hideProgressbar(progressBar);
        HelperClass.toast(this,message);
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (!isLoading){
            presenter.getHospitalDetails(hospitalId);
            isLoading = true;
        }
    }
}
