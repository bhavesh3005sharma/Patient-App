package com.scout.patient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scout.patient.Models.ModelDepartment;
import com.scout.patient.R;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.viewHolder> {
    private ArrayList<ModelDepartment> list;
    private Context context;

    public DepartmentsAdapter(ArrayList<ModelDepartment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_department, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.title.setText("\u2022 "+list.get(position).getDepartmentName());
        holder.description.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount()  {
        return(list!=null? list.size() : 0);
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) TextView title;
        @BindView(R.id.body) TextView description;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
