package com.motatwera.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.motatwera.myproject.ui.SplashActivity;
import com.motatwera.myproject.ui.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            getSharedPreferences("userShared", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();
          startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        });
    }
}