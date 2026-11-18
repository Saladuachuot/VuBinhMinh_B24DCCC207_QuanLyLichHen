package com.example.test4;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.R;
import com.example.test4.model.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<Appointment> list;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public AppointmentAdapter(List<Appointment> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateData(List<Appointment> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText("Địa điểm: " + item.getLocation());
        holder.tvDateTime.setText(item.getFormattedDate() + "  •  " + item.getTime());

        if (item.isReminder()) {
            holder.tvReminder.setText("Có nhắc");
            holder.tvReminder.setBackgroundColor(Color.parseColor("#FEF3C7"));
            holder.tvReminder.setTextColor(Color.parseColor("#B45309"));
        } else {
            holder.tvReminder.setText("Không nhắc");
            holder.tvReminder.setBackgroundColor(Color.parseColor("#E2E8F0"));
            holder.tvReminder.setTextColor(Color.parseColor("#475569"));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvDateTime, tvReminder;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvLocation = itemView.findViewById(R.id.tvItemLocation);
            tvDateTime = itemView.findViewById(R.id.tvItemDateTime);
            tvReminder = itemView.findViewById(R.id.tvItemReminder);
        }
    }
}