package com.example.appweathergb.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweathergb.R;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {

    private String[] hours;
    private char tmp = 0x00B0;

    public HoursAdapter(String[] hours) {
        this.hours = hours;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hours_element, parent, false);
        return new HoursAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTime.setText(hours[position].substring(0, 5));
        holder.textViewTemp.setText(hours[position].substring(5) + tmp);
    }

    @Override
    public int getItemCount() {
        return hours.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;
        TextView textViewTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.hour_element);
            textViewTemp = itemView.findViewById(R.id.hours_temp_element);
        }
    }
}
