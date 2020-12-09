package com.example.appweathergb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.appweathergb.fragments.DaysFragment;
import com.example.appweathergb.fragments.HoursFragment;
import com.example.appweathergb.fragments.WebViewFragment;
import com.example.appweathergb.model.JsonConnector;
import com.example.appweathergb.model.WeatherRequest;
import com.example.appweathergb.observers.WeatherConnector;
import com.example.appweathergb.storage.Parcel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , WeatherConnector, Constants {

    //TODO: подключить логирование, проверить ресурсы на хардкод, добавить ин.яз., стили, смена темы(настройки в drawer),...

    private static final boolean LOG = true;
    private static final String TAG = "weatherActivityMain";

    private JsonConnector jsonConnector;

    private Toolbar toolbar;
    private HoursFragment hoursFragment;
    private DaysFragment daysFragment;
    private WebViewFragment webViewFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView mainTemp;
    private TextView mainCity;

    private char celsius;
    private String toolbarTitleOpen;
    private String toolbarTitleClose;
    private String toolbarTitleMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LOG) {
            Log.v(TAG, "onCreate");
        }

        jsonConnector = new JsonConnector(getString(R.string.city_start_app), this);

        initBars();
        initDrawer(toolbar);
        initStartFragments();
        initView();
        otherInit();
    }

    private void otherInit() {
        celsius = 0x00B0;
    }

    private void initView() {
        mainTemp = findViewById(R.id.mainPage_temp);
        mainCity = findViewById(R.id.city);
    }

    private void initBars() {
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.toolbarLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: рефакторинг условий, проработать размеры анимации в setCollapsedTitleTextAppearance
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                collapsingToolbarLayout.setTitle(toolbarTitleClose);
//                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.toolbar_close);
            } else if (verticalOffset == 0) {
                collapsingToolbarLayout.setTitle(toolbarTitleOpen);
            } else {
                collapsingToolbarLayout.setTitle(toolbarTitleOpen);
                if (verticalOffset <= -120) {
                    collapsingToolbarLayout.setTitle(toolbarTitleMove);
                }
            }
        });
    }

    private void initStartFragments() {
        hoursFragment = new HoursFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_hours_fragment, hoursFragment);
        fragmentTransaction.commit();

        daysFragment = new DaysFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_days_fragment, daysFragment);
        fragmentTransaction.commit();

        webViewFragment = new WebViewFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.web_view_conteiner, webViewFragment);
        fragmentTransaction.commit();
    }

    private void initDrawer(Toolbar toolbar) {
        if (LOG) {
            Log.v(TAG, "initDrawer");
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.getStatusBarBackgroundDrawable();
        final CoordinatorLayout mainContent = findViewById(R.id.mainContent);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open
                , R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                mainContent.setTranslationX(slideX);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (LOG) {
            Log.v(TAG, "finalDrawer");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.changeCity:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(KEY_SEARCH_ACTIVITY, mainCity.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(KEY_SEARCH_ACTIVITY, mainCity.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == RESULT_COD_SEARCH_VIEW) {
            String city = data.getStringExtra(KEY_MAIN_ACTIVITY);
            mainCity.setText(city);
            jsonConnector = new JsonConnector(city, this);
        }
    }

    @Override
    public void update(WeatherRequest weatherRequest, String exception) {
        if (exception == null) {
            float a = weatherRequest.getMain().getTemp() - 273;
            int result = Math.round(a);
            mainCity.setText(weatherRequest.getName());
            mainTemp.setText(String.valueOf(result) + celsius);
            toolbarTitleOpen = String.format(getString(R.string.wind_speed), weatherRequest.getWind().getSpeed());
            toolbarTitleClose = String.format("%s %d%c", weatherRequest.getName(), result, celsius);
            toolbarTitleMove = String.format("%s", weatherRequest.getName());
        } else {
            mainCity.setText(exception);
            mainTemp.setText(exception);
            toolbarTitleOpen = exception;
            toolbarTitleMove = exception;
            toolbarTitleClose = exception;
        }
    }
}