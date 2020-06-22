package com.scout.patient.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scout.patient.R;
import com.scout.patient.data.Models.ModelAppointment;
import com.scout.patient.data.Models.ModelBookAppointment;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.viewHolder> implements Filterable {
    Context context;
    ArrayList<ModelAppointment> list;
    ArrayList<ModelAppointment> filteredList;

    public AppointmentsAdapter(Context context, ArrayList<ModelAppointment> list) {
        this.context = context;
        this.list = list;
        this.filteredList = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_appointment, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ModelAppointment appointment = filteredList.get(position);
        holder.date.setText(appointment.getAppointmentDate());
        holder.disease.setText(appointment.getDisease());
        Log.d("AdapterAppoi",appointment.getDisease()+"*"+appointment.getStatus());

        if (appointment.getStatus().equals(context.getString(R.string.accepted))){
            holder.status.setText(R.string.accepted);
            holder.status.setTextColor(Color.WHITE);
            holder.status.setBackgroundResource(R.drawable.accepted_backgrounded);
        }
        if (appointment.getStatus().equals(context.getString(R.string.rejected))){
            holder.status.setText(R.string.rejected);
            holder.status.setTextColor(Color.WHITE);
            holder.status.setBackgroundResource(R.drawable.rejected_backgrounded);
        }
        if (appointment.getStatus().equals(context.getString(R.string.pending))){
            holder.status.setText(R.string.pending);
            holder.status.setTextColor(Color.BLACK);
            holder.status.setBackgroundResource(R.drawable.pending_backgrounded);
        }
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
                    for (ModelAppointment row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAppointmentDate().toLowerCase().contains(charString.toLowerCase()) || row.getDisease().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<ModelAppointment>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_disease)
        TextView disease;
        @BindView(R.id.text_status)
        TextView status;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
