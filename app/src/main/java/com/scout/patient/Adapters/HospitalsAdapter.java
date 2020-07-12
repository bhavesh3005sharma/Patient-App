package com.scout.patient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.Models.ModelDepartment;
import com.scout.patient.Models.ModelDoctorInfo;
import com.scout.patient.Models.ModelHospitalInfo;
import com.scout.patient.R;
import com.scout.patient.ui.Hospital.HospitalActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.viewHolder> implements Filterable {
    ArrayList<ModelHospitalInfo> list;
    ArrayList<ModelHospitalInfo> filteredList;
    Context context;
    interfaceClickListener mListener;

    public void  setUpOnClickListener(interfaceClickListener mListener) {
        this.mListener = mListener;
    }

    public HospitalsAdapter(ArrayList<ModelHospitalInfo> list, Context context) {
        this.list = list;
        this.filteredList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hospital, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelHospitalInfo hospitalInfo = filteredList.get(position);
        holder.name.setText(hospitalInfo.getName());
        holder.phoneNo.setText(hospitalInfo.getPhone_no());
        holder.location.setText(hospitalInfo.getAddress());
        holder.email.setText(hospitalInfo.getEmail());

        if (list.get(position).getDepartments()!=null) {
            ArrayList<String> departmentsNameList = new ArrayList<>();
            for (ModelDepartment department : list.get(position).getDepartments())
                departmentsNameList.add(department.getDepartmentName());

            holder.recyclerViewDepartments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerViewDepartments.hasFixedSize();
            ChipsAdapter adapter = new ChipsAdapter(departmentsNameList, context);
//        adapter.setUpOnClickListener(HospitalActivity.this);
            holder.recyclerViewDepartments.setAdapter(adapter);
        }
 //       Picasso.get().load(Uri.parse(doctorInfo.getUri())).placeholder(R.drawable.ic_person).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return(filteredList!=null? filteredList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    ArrayList<ModelHospitalInfo> listFilterByQuery = new ArrayList<>();
                    for (ModelHospitalInfo row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone_no().toLowerCase().contains(charString.toLowerCase())
                        ||  row.getAddress().toLowerCase().contains(charString.toLowerCase())) {
                            listFilterByQuery.add(row);
                        }
                    }
                    filteredList = listFilterByQuery;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ModelHospitalInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_hospital_name) TextView name;
        @BindView(R.id.text_phoneNo) TextView phoneNo;
        @BindView(R.id.text_location) TextView location;
        @BindView(R.id.text_email) TextView email;
        @BindView(R.id.profileImage) ImageView image;
        @BindView(R.id.departmentsRecyclerView) RecyclerView recyclerViewDepartments;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.holderClick(getAdapterPosition());
                }
            });
        }
    }

    public interface interfaceClickListener{
        void holderClick(int position);
    }
}
