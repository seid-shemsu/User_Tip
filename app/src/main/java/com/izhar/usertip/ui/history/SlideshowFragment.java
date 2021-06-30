package com.izhar.usertip.ui.history;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.usertip.Game;
import com.izhar.usertip.GameAdapter;
import com.izhar.usertip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SlideshowFragment extends Fragment {
    private Dialog loading;
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private TextView not_posted;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        showLoading();
        init();
        load();
        return root;
    }
    private void init() {
        recyclerView = root.findViewById(R.id.recycler);
        not_posted = root.findViewById(R.id.not_posted);
    }
    private void load(){
        games = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("histories").child(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    not_posted.setVisibility(View.GONE);
                    games.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        games.add(dataSnapshot.getValue(Game.class));
                    }
                    adapter = new GameAdapter(getContext(), games);
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

    private void showLoading(){
        loading = new Dialog(getContext());
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading);
        loading.show();
    }
}