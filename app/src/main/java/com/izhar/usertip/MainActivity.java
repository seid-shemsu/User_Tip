package com.izhar.usertip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference data;
    private ProgressBar progress;
    private TextView not_posted, not_verified;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Today's tip");
        init();
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
                            progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(this, Settings.class));
        }
        return false;
    }

    private void load(){
        games = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        data = FirebaseDatabase.getInstance().getReference(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
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
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}