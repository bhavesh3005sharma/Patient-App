package com.scout.patient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.scout.patient.ui.DoctorsProfile.DoctorsProfileActivity;
import com.scout.patient.ui.HospitalProfile.HospitalProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.viewHolder> {
    ArrayList<ModelKeyData> list;
    Context context;
    interfaceClickListener mListener;

    public void  setUpOnClickListener(interfaceClickListener mListener) {
        this.mListener = mListener;
    }

    public SearchResultsAdapter(ArrayList<ModelKeyData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelKeyData modelKeyData = list.get(position);
        holder.name.setText(modelKeyData.getName());

        if (modelKeyData.getImageUrl()!=null && !modelKeyData.getImageUrl().isEmpty())
            Picasso.get().load(Uri.parse(modelKeyData.getImageUrl())).placeholder(R.color.placeholder_bg).into(holder.image);
        else {
            if (modelKeyData.isHospital())
                holder.image.setImageResource(R.drawable.hospital_icon);
            else
                holder.image.setImageResource(R.drawable.doctor_icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.holderClick(modelKeyData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return(list!=null? list.size() : 0);
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_name) TextView name;
        @BindView(R.id.profileImage) ImageView image;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface interfaceClickListener{
        void holderClick(ModelKeyData position);
    }
}
