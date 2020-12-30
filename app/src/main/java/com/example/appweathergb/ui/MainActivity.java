package com.example.appweathergb.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appweathergb.R;
import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.storage.Constants;
import com.example.appweathergb.ui.dialogs.CitySelectionExceptionDialog;
import com.example.appweathergb.ui.fragments.DaysFragment;
import com.example.appweathergb.ui.fragments.HoursFragment;
import com.example.appweathergb.ui.fragments.WebViewFragment;
import com.example.appweathergb.ui.presenter.WeatherPresenter;
import com.example.appweathergb.ui.settings.BaseActivity;
import com.example.appweathergb.ui.settings.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        , OutputWeatherView {

    //TODO: добавить ин.яз. в ресурсы.

    private static final int PERMISSION_REQUEST_CODE = 10;


    private static final boolean LOG = true;
    private static final String TAG = "myWeatherActivityMain";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Toolbar toolbar;
    private HoursFragment hoursFragment;
    private DaysFragment daysFragment;
    private WebViewFragment webViewFragment;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView mainTemp;
    private TextView mainCity;

    private String toolbarTitleOpen;
    private String toolbarTitleClose;
    private String toolbarTitleMove;

    private InputPresenter inputPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LOG) {
            Log.d(TAG, "onCreate");
        }

        initBars();
        initDrawer(toolbar);
        initStartFragments();
        initView();
        architectureInit();
        initGetToken();
        requestPemissions();

    }

    private void requestPemissions() {
        if (LOG) {
            Log.v(TAG, "requestPemissions");
        }
        // Проверим на пермиссии, и если их нет, запросим у пользователя
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // запросим координаты
            if (LOG) {
                Log.v(TAG, "requestPemissions if");
            }

        } else {
            // пермиссии нет, будем запрашивать у пользователя
            if (LOG) {
                Log.v(TAG, "requestPemissions else");
            }
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        if (LOG) {
            Log.v(TAG, "requestLocationPermissions");
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void architectureInit() {
        inputPresenter = new WeatherPresenter(this);
        inputPresenter.search(getString(R.string.city_start_app));
    }

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("PushMessage", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.i(TAG, token);
                    }
                });
    }


    @Override
    protected void onStop() {

        if (LOG) {
            Log.d(TAG, "onStop");
            super.onStop();
        }
    }

    private void initView() {
        if (LOG) {
            Log.v(TAG, "initView");
        }
        mainTemp = findViewById(R.id.mainPage_temp);
        mainCity = findViewById(R.id.city);
    }

    private void initBars() {
        if (LOG) {
            Log.v(TAG, "initBars");
        }
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.toolbarLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                collapsingToolbarLayout.setTitle(toolbarTitleClose);
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
        if (LOG) {
            Log.v(TAG, "initFragments");
        }

        hoursFragment = new HoursFragment();
        daysFragment = new DaysFragment();
        webViewFragment = new WebViewFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container_hours_fragment, hoursFragment);
        fragmentTransaction.add(R.id.container_days_fragment, daysFragment);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LOG) {
            Log.v(TAG, "onCreateOptionsMenu");
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (LOG) {
            Log.v(TAG, "onOptionsItemSelected");
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.changeCity:
                inputPresenter.searchByCoordinates();
        }
        return super.onOptionsItemSelected(item);
    }

    private void activityForResult(int requestCode, String key, String value) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(key, value);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (LOG) {
            Log.v(TAG, "onNavigationItemSelected");
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.nav:
                activityForResult(Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY
                        , Constants.KEY_SEARCH_ACTIVITY
                        , mainCity.getText().toString());
                break;

            case R.id.set:
                Intent intentSet = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intentSet, Constants.SETTING_CODE);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (LOG) {
            Log.v(TAG, "onActivityResult");
        }
        if (requestCode == Constants.SETTING_CODE) {
            recreate();
        }
        if (requestCode != Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == Constants.RESULT_COD_SEARCH_VIEW) {
            String city = data.getStringExtra(Constants.KEY_MAIN_ACTIVITY);

            mainCity.setText(city);
            inputPresenter.search(city);
        }
    }

    @Override
    public void WeatherViewRequest(String city, String temp, String speed) {

        if (city.equalsIgnoreCase(Constants.failConnection)) {
            CitySelectionExceptionDialog exceptionDialog = CitySelectionExceptionDialog.newInstance();
            exceptionDialog.setDialogListenerExceptionConnection(() ->
                    activityForResult(Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY
                            , Constants.KEY_SEARCH_ACTIVITY
                            , mainCity.getText().toString()));
            exceptionDialog.show(getSupportFragmentManager(), "dialog_fragment");
        }
        mainCity.setText(city);
        mainTemp.setText(temp);
        toolbarTitleOpen = speed;
        toolbarTitleMove = city;
        toolbarTitleClose = String.format("%s %s", city, temp);
    }

    @Override
    public void allWeather(List<WeatherView> weatherViewList) {
    }
}
