package com.izhar.usertip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Dialog loading;
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private TextView not_posted, not_verified;

    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Today's tip");
        showLoading();
        init();
        handler.postDelayed(checkInternet, 2000);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("account_status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue().toString().equalsIgnoreCase("verified")){
                                load();
                        }
                        else {
                            not_verified.setVisibility(View.VISIBLE);
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler);
        not_posted = findViewById(R.id.not_posted);
        not_verified = findViewById(R.id.not_verified);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity2.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(this, Settings.class));
            finish();
        }
        return true;
    }

    private void load(){
        games = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("vip").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    not_posted.setVisibility(View.GONE);
                    games.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        games.add(dataSnapshot.getValue(Game.class));
                    }
                    adapter = new GameAdapter(MainActivity.this, games);
                    recyclerView.setAdapter(adapter);
                } else {
                    not_posted.setVisibility(View.VISIBLE);
                }
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private final Runnable checkInternet = new Runnable() {
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            int connected = 0;
            View view = findViewById(R.id.recycler);
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    connected = 1;
                }
            }
            if (connected == 0) {
                Snackbar.make(view, "please connect to internet", Snackbar.LENGTH_INDEFINITE).show();
            }
            handler.postDelayed(this, 3000);
        }
    };
    private void showLoading(){
        loading = new Dialog(this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading);
        loading.show();
    }
}