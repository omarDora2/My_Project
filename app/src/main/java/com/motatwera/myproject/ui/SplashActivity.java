package com.motatwera.myproject.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.motatwera.myproject.MainActivity;
import com.motatwera.myproject.R;
import com.motatwera.myproject.ui.auth.LoginActivity;
import com.motatwera.myproject.ui.auth.SignUpActivity;

public class SplashActivity extends AppCompatActivity {
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        isLogin = getSharedPreferences("userShared", MODE_PRIVATE)
                .getBoolean("isLogin", false);


    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {

            Intent intent;
            if (isLogin){
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);


            }
            startActivity(intent);
            finish();

        }, 4000);


    }
}