package com.izhar.usertip.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.izhar.usertip.MainActivity2;
import com.izhar.usertip.R;
import com.izhar.usertip.user.Profile;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
        finish();
    }

    public void login(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }
}