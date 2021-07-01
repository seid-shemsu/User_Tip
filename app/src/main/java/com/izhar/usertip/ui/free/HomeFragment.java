package com.izhar.usertip.ui.free;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.izhar.usertip.Game;
import com.izhar.usertip.GameAdapter;
import com.izhar.usertip.MainActivity;
import com.izhar.usertip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class HomeFragment extends Fragment {

    private String today = "";
    private Dialog loading;
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference data;
    private TextView not_posted;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        showLoading();
        today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        init();
        load(today);
        return root;
    }
    private void init() {
        recyclerView = root.findViewById(R.id.recycler);
        not_posted = root.findViewById(R.id.not_posted);
    }
    private void load(String today) {
        games = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);
        data = FirebaseDatabase.getInstance().getReference("tips").child(today);
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
                loading.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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