package com.scout.patient.Adapters;

import android.content.Context;
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
import com.scout.patient.Models.ModelDoctorInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.viewHolder> implements Filterable {
    ArrayList<ModelDoctorInfo> list;
    ArrayList<ModelDoctorInfo> filteredList;
    Context context;
    interfaceClickListener mListener;

    public void  setUpOnClickListener(interfaceClickListener mListener) {
        this.mListener = mListener;
    }

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
        holder.specialisation.setText(doctorInfo.getDepartment());
        holder.location.setText(doctorInfo.getAddress());
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
                    ArrayList<ModelDoctorInfo> listFilterByQuery = new ArrayList<>();
                    for (ModelDoctorInfo row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getDepartment().toLowerCase().contains(charString.toLowerCase())) {
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
        @BindView(R.id.text_location)
        TextView location;
        @BindView(R.id.profileImage)
        CircularImageView image;

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
