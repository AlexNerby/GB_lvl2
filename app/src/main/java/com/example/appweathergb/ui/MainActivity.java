package com.example.appweathergb.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.appweathergb.BuildConfig;
import com.example.appweathergb.R;
import com.example.appweathergb.parser.JsonParser;
import com.example.appweathergb.singleton.MyApp;
import com.example.appweathergb.ui.dialogs.CitySelectionExceptionDialog;
import com.example.appweathergb.ui.dialogs.OnDialogListenerExceptionConnection;
import com.example.appweathergb.entities.WeatherView;
import com.example.appweathergb.ui.fragments.DaysFragment;
import com.example.appweathergb.ui.fragments.HoursFragment;
import com.example.appweathergb.ui.fragments.WebViewFragment;
import com.example.appweathergb.network.model.WeatherRequest;
import com.example.appweathergb.observers.WeatherConnector;
import com.example.appweathergb.parser.ParserConnector;
import com.example.appweathergb.service.JsonService;
import com.example.appweathergb.ui.settings.BaseActivity;
import com.example.appweathergb.ui.settings.SettingsActivity;
import com.example.appweathergb.storage.Constants;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        , ParserConnector.BackParser {
    //TODO: добавить ин.яз. в ресурсы. В новом классе переопределить коллбэк для requestRetrofit();

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

    private boolean isBound = false;
    private JsonService.ServiceBinder boundService;

    private final ParserConnector parserConnector = new JsonParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LOG) {
            Log.d(TAG, "start onCreate");
        }
        //TODO: можно определить задачу в свободный сервис
//        Intent intent = new Intent(MainActivity.this, JsonService.class);
//        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);

        //Проверка на конект сервиса
//        if (boundService == null) {
//            Log.d(TAG, "boundService == null");
//            Intent intent = new Intent(MainActivity.this, JsonService.class);
//            bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
//        } else {
//            boundService.freeMethod();
//        }

        initRetrofit();
        initBars();
        initDrawer(toolbar);
        initStartFragments();
        initView();
    }

    private void initRetrofit() {
        if (LOG) {
            Log.v(TAG, "start initRetrofit");
        }

        requestRetrofit(getString(R.string.city_start_app), BuildConfig.WEATHER_API_KEY);
    }

    private void requestRetrofit(String city, String keyApi) {
        if (LOG) {
            Log.v(TAG, "requestRetrofit");
        }
        MyApp.getOpenWeatherApi().loadWeather(city, keyApi).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response.body() != null) {
                    if (LOG) {
                        Log.v(TAG, "onResponse if");
                    }
                    parserConnector.parse(response.body(), MainActivity.this, null);
//                    response.body().setName(null);
                } else {
                    if (LOG) {
                        Log.v(TAG, "onResponse else");
                    }
                    parserConnector.parse(new WeatherRequest(), MainActivity.this, Constants.failConnection);
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                if (LOG) {
                    Log.v(TAG, "onFailure", t);
                }
                parserConnector.parse(new WeatherRequest(), MainActivity.this, Constants.failConnection);
            }
        });
    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            boundService = (JsonService.ServiceBinder) iBinder;
            isBound = boundService != null;
            boundService.freeMethod(MainActivity.this, getString(R.string.city_start_app));
            if (isBound) {
                Log.d(TAG, "boundService connection | startHttps");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            boundService = null;
            if (LOG) {
                Log.d(TAG, "boundService = null | isBound = false");
            }
        }
    };

    @Override
    protected void onStop() {
        //TODO: fixed: при каждом повороте экрана убивает HandlerThread и пересоздает
        if (LOG) {
            Log.d(TAG, "onStop | unbindService");
        }
        if (isBound) {
            unbindService(boundServiceConnection);
        }
        super.onStop();
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
        if (LOG) {
            Log.v(TAG, "initFragments");
        }

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
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(Constants.KEY_SEARCH_ACTIVITY, mainCity.getText().toString());
                startActivityForResult(intent, Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (LOG) {
            Log.v(TAG, "onNavigationItemSelected");
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.nav:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(Constants.KEY_SEARCH_ACTIVITY, mainCity.getText().toString());
                startActivityForResult(intent, Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY);
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
            requestRetrofit(city, BuildConfig.WEATHER_API_KEY);
        }
    }

    @Override
    public void dataWeather(WeatherView weatherView, String exception) {
        if (LOG) {
            Log.v(TAG, "dataWeather");
        }
        if (exception != null) {
            if (LOG) {
                Log.v(TAG, "exception != null");
            }
            CitySelectionExceptionDialog exceptionDialog = CitySelectionExceptionDialog.newInstance();
            exceptionDialog.setDialogListenerExceptionConnection(new OnDialogListenerExceptionConnection() {
                @Override
                public void change() {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra(Constants.KEY_SEARCH_ACTIVITY, mainCity.getText().toString());
                    startActivityForResult(intent, Constants.REQUEST_CODE_MAIN_AND_SEARCH_ACTIVITY);
                }
            });
            exceptionDialog.show(getSupportFragmentManager(), "dialog_fragment");
        } else {
            if (LOG) {
                Log.v(TAG, "exception == null");
            }
            mainCity.setText(weatherView.getCity());
            mainTemp.setText(weatherView.getTemperature());
            toolbarTitleOpen = String.format(getString(R.string.wind_speed), weatherView.getWindSpeed());
            toolbarTitleClose = String.format("%s %s", weatherView.getCity(), weatherView.getTemperature());
            toolbarTitleMove = String.format("%s", weatherView.getCity());
        }
    }
}