package com.example.appweathergb.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.appweathergb.R;
import com.example.appweathergb.settings.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinfs);

        Button button = findViewById(R.id.settings_apply);
        Switch switchDarkTheme = findViewById(R.id.switch_them);

        switchDarkTheme.setChecked(isDarkTheme());
        switchDarkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setDarkTheme(b);
//                if (b) {
//                    switchDarkTheme.setText(R.string.switch_button_dark);
//                } else {
//                    switchDarkTheme.setText(R.string.switch_button_light);
//                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
                finish();
            }
        });
    }
}