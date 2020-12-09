package com.example.appweathergb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appweathergb.singleton.SimpleSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO: добавить Диалоги подтверждения выбора города

public class SearchActivity extends AppCompatActivity implements Constants {

    private static final boolean LOG = true;
    private static final String TAG = "weatherActivitySearch";

    private List<String> list;
    private ImageButton mic;
    private ImageButton send;
    private ListView listView;
    private SearchView searchView;
    private ArrayAdapter<String> adapter;
    private SimpleSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate");

        singleton = SimpleSingleton.getInstance();

        initView();
        otherInit();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchView.setQuery(adapterView.getItemAtPosition(i).toString(), false);
                String str = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent();
                intent.putExtra(KEY_MAIN_ACTIVITY, str);
                setResult(RESULT_COD_SEARCH_VIEW, intent);
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                adapter.getFilter().filter(query);
                if (!checkContains(query)) {
                    adapter.clear();
                    list.add(query);
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    singleton.setMsg(list);
                }
                Intent intent = new Intent();
                intent.putExtra(KEY_MAIN_ACTIVITY, query);
                setResult(RESULT_COD_SEARCH_VIEW, intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void otherInit() {
        if (singleton.getMsg() == null) {
            Log.d(TAG, "singleton check true");
            list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.citys)));
        } else {
            Log.d(TAG, "singleton check false (null)");
            list = new ArrayList<>(singleton.getMsg());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.activity_search_element_list_view
                , R.id.listView_searchView_text, list);
        listView.setAdapter(adapter);
    }

    private boolean checkContains(String query) {
        for (int i = 0; i < list.size() ; i++) {
            if (list.get(i).equalsIgnoreCase(query)) {
                return true;
            }
        }
        return false;
    }

    private void initView() {
        mic = findViewById(R.id.mic);
        send = findViewById(R.id.send);
        listView = findViewById(R.id.list_view_search);
        searchView = findViewById(R.id.searchView_listView);
    }

    @Override
    public void onBackPressed() {
        singleton.setMsg(list);
        super.onBackPressed();
    }
}