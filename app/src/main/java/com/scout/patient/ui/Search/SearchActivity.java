package com.scout.patient.ui.Search;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.scout.patient.Adapters.SearchAdapter;
import com.scout.patient.Models.ModelIntent;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Repository.Room.DataBaseManager;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.interfaceClickListener{
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ImgClrSuggestionHistory)
    ImageView ImgClrSuggestionHistory;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.searchEditText)
    EditText searchEdit;

    Unbinder unbinder;
    SearchAdapter adapter;
    String key;
    ModelIntent modelIntent;
    DataBaseManager dataBaseManager;
    ArrayList<ModelKeyData> previousDataList = new ArrayList<>();
    ArrayList<ModelKeyData> searchableList = new ArrayList<>();
    ArrayList<ModelKeyData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder = ButterKnife.bind(this);

        modelIntent = (ModelIntent) getIntent().getSerializableExtra("modelIntent");
        if (modelIntent==null) {
            modelIntent = new ModelIntent();
        }
        key = getIntent().getStringExtra("key");

        initRecyclerView();
        loadData();

        ImgClrSuggestionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SearchActivity.this);
                alertDialog.setTitle("Are you sure?");
                alertDialog.setMessage("You want to Clear Suggestion History.");
                alertDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBaseManager.cleanSuggestionHistory();
                        loadDataWithoutHistory();
                        dialog.dismiss();
                    }
                });

                alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.create().show();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                ArrayList<ModelKeyData> filteredList = new ArrayList<>();

                if (!newText.isEmpty())
                    for (ModelKeyData result : searchableList)
                        if (result.getName().toLowerCase().contains(newText.toLowerCase()))
                            filteredList.add(result);

                for (ModelKeyData result : previousDataList)
                    if (result.getName().toLowerCase().contains(newText.toLowerCase()))
                        filteredList.add(result);

                updateAdapter(filteredList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String query = v.getText().toString().trim();
                if(actionId == EditorInfo.IME_ACTION_SEARCH && !query.isEmpty()){
                    ArrayList<ModelKeyData> sendingList = new ArrayList<>();

                    for (ModelKeyData result : searchableList)
                        if (result.getName().toLowerCase().contains(query.toLowerCase()))
                            sendingList.add(result);

                    Intent intent = new Intent(SearchActivity.this,SearchResultsActivity.class);
                    intent.putExtra("list",sendingList);
                    intent.putExtra("modelIntent",modelIntent);
                    startActivity(intent);
                    dataBaseManager.addData(null,query,null,"3");
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void loadDataWithoutHistory() {
        for (int  i=list.size()-1;i>=0;i--){
            if (list.get(i).getStrId()==null)
                list.remove(i);
            else
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        dataBaseManager = new DataBaseManager(SearchActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.hasFixedSize();
        adapter = new SearchAdapter(this,list,key);
        recyclerView.setAdapter(adapter);
        adapter.setUpOnClickListener(SearchActivity.this);
    }

    @Override
    public void holderClick(ModelKeyData modelKeyData) {
        if (modelKeyData.getStrId()!=null) {
            if (modelKeyData.isHospital()) {
                Intent intent = new Intent(SearchActivity.this, HospitalProfileActivity.class);
                intent.putExtra("hospitalId", modelKeyData.getStrId());
                intent.putExtra("hospitalName", modelKeyData.getName());
                modelIntent.setIntentFromHospital(true);
                intent.putExtra("modelIntent", modelIntent);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SearchActivity.this, DoctorsProfileActivity.class);
                intent.putExtra("doctorId", modelKeyData.getStrId());
                intent.putExtra("doctorName", modelKeyData.getName());
                modelIntent.setIntentFromHospital(false);
                intent.putExtra("modelIntent", modelIntent);
                startActivity(intent);
                finish();
            }
        }else {
            searchEdit.setText(modelKeyData.getName());
            searchEdit.requestFocus();
        }
    }

    @Override
    public void removeSuggestion(String title, int position) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(SearchActivity.this);
        alertDialog.setTitle("Remove From Suggestion History?");
        alertDialog.setMessage(title);
        alertDialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBaseManager.deleteData(title);
                list.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create().show();
    }

    private void loadData() {
        if ((key.equals(getString(R.string.only_hospitals)) || key.equals(getString(R.string.both)))) {
            Cursor cursor = dataBaseManager.getAllHospitalsData();
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                searchableList.add(new ModelKeyData( cursor.getString(0), cursor.getString(1),cursor.getString(2),true));
                cursor.moveToNext();
            }
        }

        if ((key.equals(getString(R.string.only_doctors)) || key.equals(getString(R.string.both)))) {
            Cursor cursor = dataBaseManager.getAllDoctorsData();
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                searchableList.add(new ModelKeyData(cursor.getString(0), cursor.getString(1), cursor.getString(2),false));
                cursor.moveToNext();
            }
        }

        Cursor cursor = dataBaseManager.getAllPreviousData();
        cursor.moveToFirst();

        for(int i=0;i<cursor.getCount();i++){
            previousDataList.add(new ModelKeyData(cursor.getString(0),null,null));
            cursor.moveToNext();
        }

        updateAdapter(previousDataList);
    }

    private void updateAdapter(ArrayList<ModelKeyData> filteredList) {
        list.clear();
        list.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}
