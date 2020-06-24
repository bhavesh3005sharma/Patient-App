package com.scout.patient.ui.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelPatientInfo;
import com.scout.patient.data.Prefs.SharedPref;
import com.scout.patient.ui.Auth.LoginActivity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment implements Contract.View{
    @BindView(R.id.textId)
    TextView textId;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textEmail)
    TextView textEmail;
    @BindView(R.id.textPhoneNo)
    TextView textPhoneNo;
    @BindView(R.id.textAddress)
    TextView textAddress;
    @BindView(R.id.textMedicalHistory)
    TextView textMedicalHistory;
    @BindView(R.id.textBloodGroup)
    TextView textBloodGroup;
    @BindView(R.id.textWeight)
    TextView textWeight;
    @BindView(R.id.textDob)
    TextView textDob;

    Unbinder unbinder;
    ProfilePresenter presenter;
    ModelPatientInfo patientInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this,view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Profile");
        presenter = new ProfilePresenter( ProfileFragment.this);
        patientInfo = SharedPref.getLoginUserData(getContext());
        setUpView();

        return view;
    }

    private void setUpView() {
        textId.setText(patientInfo.getPatientId().getId());
        textName.setText(patientInfo.getName());
        textAddress.setText(patientInfo.getAddress());
        textBloodGroup.setText(patientInfo.getBloodGroup());
        textMedicalHistory.setText(patientInfo.getMedicalHistory());
        textDob.setText(patientInfo.getDOB());
        textEmail.setText(patientInfo.getEmail());
        textWeight.setText(patientInfo.getWeight());
        textPhoneNo.setText(patientInfo.getPhoneNo());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_frag_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            SharedPref.deleteLoginUserData(getContext());
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
