package com.scout.patient.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> implements Filterable {
    ArrayList<ModelKeyData> list;
    Context context;

    public SearchAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
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
            Picasso.get().load(Uri.parse(modelKeyData.getImageUrl())).placeholder(R.color.colorPrimary).into(holder.profileImage);
        else
            holder.profileImage.setImageResource(R.drawable.hospital_icon);
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
                Log.d("Data",charSequence+"");
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list.clear();
                } else {
                    ArrayList<ModelKeyData> listFilterByQuery = new ArrayList<>();
                    for (ModelKeyData row : SharedPref.getAllHospitalsList(context)) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            listFilterByQuery.add(row);
                        }
                    }
                    list = listFilterByQuery;
                }

                Log.d("Data","Adapter  "+list.toString());
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

    public class viewHolder extends RecyclerView.ViewHolder{
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
