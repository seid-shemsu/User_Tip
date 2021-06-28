package com.izhar.usertip.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.izhar.usertip.MainActivity;
import com.izhar.usertip.R;
import com.izhar.usertip.user.Profile;

public class Verify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
    }

    public void verify(View view) {
        startActivity(new Intent(this, Profile.class));
        finish();
    }
}