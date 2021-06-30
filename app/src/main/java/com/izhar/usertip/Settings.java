package com.izhar.usertip;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Settings extends AppCompatActivity {
    private Dialog loading;
    private Button request;
    private TextView name, phone, account_status, expire_date;
    private ImageView profile, txn;
    private Uri imgUri;
    private SharedPreferences timeout;
    private DatabaseReference database;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        timeout = getSharedPreferences("timeout", MODE_PRIVATE);


        init();
        setData();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String activation_time = timeout.getString("activation", "2020-10-10");
        if (now.equalsIgnoreCase(activation_time))
            request.setEnabled(false);
        txn.setOnClickListener(v -> openFileChooser());
        request.setOnClickListener(v -> {
            if (imgUri == null)
                Snackbar.make(v, "please attach the required photo", BaseTransientBottomBar.LENGTH_SHORT).show();
            else {
                showLoading();
                String id = new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "__" + FirebaseAuth.getInstance().getUid();
                storage = FirebaseStorage.getInstance().getReference("payment_requests").child(id);
                storage.putFile(imgUri)
                        .addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                    String time = new SimpleDateFormat("MM-dd - hh:mm").format(new Date());
                                    database = FirebaseDatabase.getInstance().getReference("payment_requests").child(id);
                                    database.child("id").setValue(FirebaseAuth.getInstance().getUid());
                                    database.child("photo").setValue(uri.toString());
                                    database.child("time").setValue(time);
                                    database.child("phone").setValue(phone);
                                    loading.dismiss();
                                    request.setEnabled(false);
                                    timeout.edit().putString("activation", new SimpleDateFormat("yyyy-MM-dd").format(new Date())).apply();
                                    Snackbar.make(v, "Your request sent successfully", BaseTransientBottomBar.LENGTH_SHORT).show();
                                }))
                        .addOnFailureListener(e -> Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            }
        });
    }

    private void setData() {
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue().toString());
                phone.setText(snapshot.child("phone").getValue().toString());
                expire_date.setText(snapshot.child("expire_date").getValue().toString());
                account_status.setText(snapshot.child("account_status").getValue().toString());
                if (snapshot.child("account_status").getValue().toString().equals("Verified"))
                    account_status.setTextColor(getResources().getColor(R.color.purple_700));
                //Picasso...for profile image
                Picasso.with(Settings.this).load(snapshot.child("image").getValue().toString()).placeholder(R.drawable.person).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        request = findViewById(R.id.btn_send_request);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        account_status = findViewById(R.id.accout_status);
        expire_date = findViewById(R.id.expire_date);

        profile = findViewById(R.id.user_profile);
        txn = findViewById(R.id.txn_image);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener((picasso, uri, exception) -> Toast.makeText(Settings.this, "" + exception.toString(), Toast.LENGTH_SHORT).show());
            builder.build().load(imgUri).into(txn);
        } else {
            Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        }
        return true;
    }

    private void showLoading() {
        loading = new Dialog(this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.loading);
        loading.show();
    }
}