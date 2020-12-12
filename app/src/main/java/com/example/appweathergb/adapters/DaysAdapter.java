package com.example.appweathergb.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweathergb.R;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherDaysAdapter";

    private String[] days;
    private DaysAdapter.OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне

    public DaysAdapter(String[] days) {
        this.days = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_days_element, parent, false);
        return new DaysAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(days[position]);

    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    // Сеттер слушателя нажатий
    public void setOnItemClickListener(DaysAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.day_element);
            textView.setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view);
                }
            });
        }
    }
}
