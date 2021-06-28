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
    List<Game> games;
    GameAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference data;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Today's tip");
        games = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        data = FirebaseDatabase.getInstance().getReference(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                games.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    games.add(dataSnapshot.getValue(Game.class));
                }
                adapter = new GameAdapter(MainActivity.this, games);
                recyclerView.setAdapter(adapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.vip){
            startActivity(new Intent(this, Vip.class));
        }
        return true;
    }
}