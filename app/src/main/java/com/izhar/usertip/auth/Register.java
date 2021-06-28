package com.izhar.usertip.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.izhar.usertip.R;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void login(View view) {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    public void register(View view) {
        startActivity(new Intent(this, Verify.class));
        finish();
    }
}