package com.scout.patient.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelDoctorInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.viewHolder> implements Filterable {
    ArrayList<ModelDoctorInfo> list;
    ArrayList<ModelDoctorInfo> filteredList;
    Context context;

    public DoctorsAdapter(ArrayList<ModelDoctorInfo> list, Context context) {
        this.list = list;
        this.filteredList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctor, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelDoctorInfo doctorInfo = filteredList.get(position);
        holder.name.setText(doctorInfo.getName());
        holder.specialisation.setText(doctorInfo.getSpecialization());
        //Picasso.get().load(Uri.parse(doctorInfo.getUri())).placeholder(R.drawable.ic_person).into(holder.image);
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
                    filteredList.clear();
                    for (ModelDoctorInfo row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getSpecialization().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ModelDoctorInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_doctor_name)
        TextView name;
        @BindView(R.id.text_specialization)
        TextView specialisation;
        @BindView(R.id.profileImage)
        CircularImageView image;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
