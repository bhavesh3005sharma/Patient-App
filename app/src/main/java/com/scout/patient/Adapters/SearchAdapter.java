package com.scout.patient.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.scout.patient.Models.ModelKeyData;
import com.scout.patient.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {
    private ArrayList<ModelKeyData> list;
    private Context context;
    private String key;
    private interfaceClickListener mListener;

    public void  setUpOnClickListener(interfaceClickListener mListener) {
        this.mListener = mListener;
    }

    public SearchAdapter(Context context, ArrayList<ModelKeyData> list, String key) {
        this.list = list;
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
        if (modelKeyData.getStrId()!=null)
            Log.d("onBindViewHolder","Data - "+modelKeyData.getName()+ " /// "+modelKeyData.getStrId());
        else
            Log.d("onBindViewHolder","Data - "+modelKeyData.getName());

        if (modelKeyData.getImageUrl()!=null && !modelKeyData.getImageUrl().isEmpty()) {
            holder.profileImage.setBorderColor(Color.WHITE);
            ViewGroup.LayoutParams params = holder.profileImage.getLayoutParams();
            params.width = 100;
            params.height = 100;
            holder.profileImage.setLayoutParams(params);
            Picasso.get().load(Uri.parse(modelKeyData.getImageUrl())).placeholder(R.color.placeholder_bg).into(holder.profileImage);
        }
        else {
            if (modelKeyData.getStrId()==null) {
                holder.profileImage.setImageResource(R.drawable.ic_history);
                holder.profileImage.setBorderColor(Color.WHITE);
                ViewGroup.LayoutParams params = holder.profileImage.getLayoutParams();
                params.width = 50;
                params.height = 50;
                holder.profileImage.setLayoutParams(params);
            }
            else {
                holder.profileImage.setBorderColor(Color.WHITE);
                ViewGroup.LayoutParams params = holder.profileImage.getLayoutParams();
                params.width = 100;
                params.height = 100;
                holder.profileImage.setLayoutParams(params);

                if (modelKeyData.isHospital())
                    holder.profileImage.setImageResource(R.drawable.hospital_icon);
                else
                    holder.profileImage.setImageResource(R.drawable.doctor_icon);
            }
        }

        if (modelKeyData.getStrId()==null) {
            holder.imageViewClr.setVisibility(View.VISIBLE);
            holder.imageViewClr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.removeSuggestion(modelKeyData.getName(),position);
                }
            });
        }
        else holder.imageViewClr.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.holderClick(modelKeyData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.profileImage)
        CircularImageView profileImage;
        @BindView(R.id.titleName)
        TextView titleName;
        @BindView(R.id.imageViewClr)
        ImageView imageViewClr;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface interfaceClickListener{
        void holderClick(ModelKeyData position);

        void removeSuggestion(String name, int position);
    }
}
