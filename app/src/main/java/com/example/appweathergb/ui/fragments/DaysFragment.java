package com.example.appweathergb.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweathergb.R;
import com.example.appweathergb.adapters.DaysAdapter;

public class DaysFragment extends Fragment {

    private static final boolean LOG = true;
    private static final String TAG = "myWeatherFragmentDays";

    private String[] days;
    private RecyclerView recyclerView;
    private DaysAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (LOG) {
            Log.d(TAG, "onCreateView");
        }


        View view = inflater.inflate(R.layout.fragment_days, container, false);
        days = getResources().getStringArray(R.array.days);
        recyclerView = view.findViewById(R.id.day_recycler);

        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(null);
            recyclerView.getRecycledViewPool().clear();
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new DaysAdapter(days);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new DaysAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    //:TODO открыть подробную погоду на 1 день
                }
            });
        }




        return view;
    }
}