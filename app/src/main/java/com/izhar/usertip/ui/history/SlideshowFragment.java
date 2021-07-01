package com.izhar.usertip.ui.history;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
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

    private String today = "";
    private Dialog loading;
    private List<Game> games;
    private GameAdapter adapter;
    private RecyclerView recyclerView;
    private TextView not_posted;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        showLoading();
        setHasOptionsMenu(true);
        today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        init();
        load(today);
        return root;
    }
    private void init() {
        recyclerView = root.findViewById(R.id.recycler);
        not_posted = root.findViewById(R.id.not_posted);
    }
    private void load(String today){
        games = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("history").child(today);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.date, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.date){
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.date_picker);
            dialog.setCanceledOnTouchOutside(false);
            final DatePicker datePicker = dialog.findViewById(R.id.date_picker);
            Button ok = dialog.findViewById(R.id.ok);
            ok.setOnClickListener(v -> {
                String day = datePicker.getDayOfMonth() + "";
                String month = datePicker.getMonth()+1 + "";
                if (day.length() == 1 )
                    day = "0" + day;
                if (month.length() == 1 )
                    month = "0" + month;
                today = day + "-" + month + "-" + datePicker.getYear();
                load(today);
                dialog.dismiss();
            });
            dialog.show();
        }
        return true;
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