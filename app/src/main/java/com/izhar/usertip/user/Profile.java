package com.izhar.usertip.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.izhar.usertip.MainActivity;
import com.izhar.usertip.MainActivity2;
import com.izhar.usertip.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void update(View view) {
        startActivity(new Intent(this, MainActivity2.class));
        finish();
    }
}