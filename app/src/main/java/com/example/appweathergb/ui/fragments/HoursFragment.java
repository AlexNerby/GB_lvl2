package com.example.appweathergb.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweathergb.R;
import com.example.appweathergb.adapters.HoursAdapter;

public class HoursFragment extends Fragment {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherFragmentHours";

    private String[] hours;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (LOG) {
            Log.d(TAG, "onCreateView");
        }

        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        hours = getResources().getStringArray(R.array.hours);
        recyclerView = view.findViewById(R.id.hour_recycler);

        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.getRecycledViewPool().clear();
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            HoursAdapter adapter = new HoursAdapter(hours);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}