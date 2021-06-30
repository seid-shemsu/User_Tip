package com.izhar.usertip.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.izhar.usertip.MainActivity;
import com.izhar.usertip.R;

public class Login extends AppCompatActivity {
    private Dialog loading;
    EditText email, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            if (isConnected()){
                if (check())
                    login(login);
            }
            else{
                Snackbar.make(login, "Check your connection", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check() {
        if (email.getText().toString().trim().isEmpty()){
            email.setError("invalid email");
            return false;
        }
        if (password.getText().toString().trim().isEmpty()){
            password.setError("empty password");
            return false;
        }
        return true;
    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
        finish();
    }

    public void login(View view) {
        showLoading();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        login.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(authResult -> {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    loading.dismiss();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    email.setEnabled(true);
                    password.setEnabled(true);
                    login.setEnabled(true);
                });
    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        int connected = 0;
        for (NetworkInfo networkInfo : info) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                connected = 1;
            }
        }
        return connected == 1;
    }
    private void showLoading(){
        loading = new Dialog(this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading);
        loading.show();
    }
}