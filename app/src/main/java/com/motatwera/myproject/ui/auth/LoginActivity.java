package com.motatwera.myproject.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.motatwera.myproject.MainActivity;
import com.motatwera.myproject.R;

public class LoginActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.loginEmail);
        etPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.loginButton);
        progressBar=findViewById(R.id.loginProgress);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //onClick
        btnLogin.setOnClickListener(view -> {

            validationData();
        });
        findViewById(R.id.createAccount).setOnClickListener(v -> {
            Intent intent= new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
            finish();

        });
    }

    private void validationData() {
        String email = etEmail.getText().toString().trim();

        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please add ur email", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "Please add valid email", Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please add ur password", Toast.LENGTH_LONG).show();
            etPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(LoginActivity.this, "password should be 6 char", Toast.LENGTH_LONG).show();
            etPassword.requestFocus();
            return;
        }
        login(email, pass);

    }

    private void login(String email, String pass) {

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        getSharedPreferences("userShared", MODE_PRIVATE)
                                .edit()

                                .putBoolean("isLogin", true)
                                .apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();


                    } else {
                        // handle error

                        Toast.makeText(LoginActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
        {


        }


    }
}