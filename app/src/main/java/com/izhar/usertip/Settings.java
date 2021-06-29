package com.izhar.usertip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Settings extends AppCompatActivity {

    private Button request, complain;
    private TextView name, phone, account_status, expire_date;
    private ImageView profile, txn;
    private Uri imgUri;
    private SharedPreferences complain_timeout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        complain_timeout = getSharedPreferences("complain_timeout", MODE_PRIVATE);
        init();
        setData();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String time = complain_timeout.getString("time", "2020-10-10");
        if (now.equalsIgnoreCase(time))
            complain.setEnabled(false);
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain.setEnabled(false);
                DatabaseReference complain = FirebaseDatabase.getInstance().getReference("complains");
                complain.child(FirebaseAuth.getInstance().getUid()).child(new SimpleDateFormat("MM-dd-yyy").format(new Date()))
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                complain_timeout.edit().putString("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date())).apply();
                                Toast.makeText(Settings.this, "complain requested successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        txn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri == null)
                    Toast.makeText(Settings.this, "please attach the required photo", Toast.LENGTH_SHORT).show();
                else {

                }
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
        complain = findViewById(R.id.btb_send_complain);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        account_status = findViewById(R.id.accout_status);
        expire_date = findViewById(R.id.expire_date);

        profile = findViewById(R.id.user_profile);
        txn = findViewById(R.id.txn_image);
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1001);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Toast.makeText(Settings.this, ""+exception.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.build().load(imgUri).into(profile);
        }
        else {
            Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

}