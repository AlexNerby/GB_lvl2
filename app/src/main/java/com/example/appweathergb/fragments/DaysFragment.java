package com.example.appweathergb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweathergb.R;
import com.example.appweathergb.adapters.DaysAdapter;

public class DaysFragment extends Fragment {

    private static final boolean LOG = true;
    private static final String TAG = "weatherFragmentDays";

    private String[] days;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        days = getResources().getStringArray(R.array.days);
        recyclerView = view.findViewById(R.id.day_recycler);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DaysAdapter adapter = new DaysAdapter(days);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DaysAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                //:TODO открыть подробную погоду на 1 день
            }
        });
        return view;
    }
}