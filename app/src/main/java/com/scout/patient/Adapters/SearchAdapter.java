package com.scout.patient.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.Repository.Prefs.SharedPref;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> implements Filterable {
    ArrayList<ModelKeyData> list;
    Context context;
    String key;

    public SearchAdapter(Context context,String key) {
        list = new ArrayList<>();
        this.context = context;
        this.key = key;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelKeyData modelKeyData = list.get(position);
        holder.titleName.setText(modelKeyData.getName());
        if (modelKeyData.getImageUrl()!=null && !modelKeyData.getImageUrl().isEmpty())
            Picasso.get().load(Uri.parse(modelKeyData.getImageUrl())).placeholder(R.color.placeholder_bg).into(holder.profileImage);
        else {
            if (modelKeyData.isHospital())
                holder.profileImage.setImageResource(R.drawable.hospital_icon);
            else
                holder.profileImage.setImageResource(R.drawable.doctor_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelKeyData.isHospital()){
                    Intent intent = new Intent(context, HospitalProfileActivity.class);
                    intent.putExtra("hospitalId",list.get(position).getId().getId());
                    intent.putExtra("hospitalName",list.get(position).getName());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, DoctorsProfileActivity.class);
                    intent.putExtra("doctorId", list.get(position).getId().getId());
                    intent.putExtra("doctorName", list.get(position).getName());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list.clear();
                } else {
                    ArrayList<ModelKeyData> listFilterByQuery = new ArrayList<>();
                    if (SharedPref.getAllHospitalsList(context)!=null && (key.equals(context.getString(R.string.only_hospitals)) || key.equals(context.getString(R.string.both))))
                        for (ModelKeyData row : SharedPref.getAllHospitalsList(context)) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()))
                            listFilterByQuery.add(row);
                    }
                    if (SharedPref.getAllDoctorsList(context)!=null && (key.equals(context.getString(R.string.only_doctors)) || key.equals(context.getString(R.string.both))))
                        for (ModelKeyData row : SharedPref.getAllDoctorsList(context)) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase()))
                                listFilterByQuery.add(row);
                        }
                    list = listFilterByQuery;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values!=null) {
                    list = (ArrayList<ModelKeyData>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public ArrayList<ModelKeyData> getFilteredList() {
        return list;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.profileImage)
        CircularImageView profileImage;
        @BindView(R.id.titleName)
        TextView titleName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
