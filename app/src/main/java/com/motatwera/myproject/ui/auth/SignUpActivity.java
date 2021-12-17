package com.motatwera.myproject.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.motatwera.myproject.MainActivity;
import com.motatwera.myproject.R;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private ProgressBar progressBar;
    private EditText registerName, registerEmail, registerPhone, registerPassword;
    private Button signUp;


    String vr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // findView
        registerName = findViewById(R.id.signUpName);
        registerEmail = findViewById(R.id.signUpEmail);
        registerPhone = findViewById(R.id.signUpPhone);
        registerPassword = findViewById(R.id.signUpPassword);
        progressBar = findViewById(R.id.registerProgress);

        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // OnClick
        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(view -> {

            validationData();
        });

    }



    private void validationData() {

        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String phone = registerPhone.getText().toString().trim();
        String pass = registerPassword.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(SignUpActivity.this, R.string.error_name_register, Toast.LENGTH_LONG).show();
            registerName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please add ur email", Toast.LENGTH_LONG).show();
            registerEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "Please add valid email", Toast.LENGTH_LONG).show();
            registerEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please add ur phone", Toast.LENGTH_LONG).show();
            registerPhone.requestFocus();
            return;
        }

        if (phone.length() < 11) {
            Toast.makeText(SignUpActivity.this, "phone should be 11 char", Toast.LENGTH_LONG).show();
            registerPhone.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please add ur password", Toast.LENGTH_LONG).show();
            registerPassword.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(SignUpActivity.this, "password should be 11 char", Toast.LENGTH_LONG).show();
            registerPassword.requestFocus();
            return;
        }

        register(email, pass, name, phone);

    }

    //on fireBase documentation
    private void register(String email, String password, String name, String phone) {

       progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        // send data to firestore
                        sendData(email, password, name, phone);
                    } else {
                        // handle error
                     progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                });

    }

    // sendData to firebase using hashmap
    private void sendData(String email, String password, String name, String phone) {

        long tsLong = System.currentTimeMillis() / 1000;
        String timestamp = Long.toString(tsLong);

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("name", name);
        map.put("phone", phone);
        map.put("timestamp", timestamp);
        map.put("isDelete", false);

        firestore.collection("users")
                .document()
                .set(map)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(SignUpActivity.this, "done", Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.GONE);


                    // set to shared P
                    getSharedPreferences("userShared",MODE_PRIVATE)
                            .edit()
                            .putString("userName",name)
                            .putString("phone",phone)
                            .putBoolean("isLogin",true)
                            .apply();

                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();


                })
                .addOnFailureListener(e -> {
                   progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();

                });


    }

}