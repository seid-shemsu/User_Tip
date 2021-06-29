package com.izhar.usertip.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.izhar.usertip.MainActivity;
import com.izhar.usertip.MainActivity2;
import com.izhar.usertip.R;
import com.izhar.usertip.Settings;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    private ImageView image;
    private Button update;
    private EditText name;
    private DatabaseReference database;
    private StorageReference storage;

    private Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        image = findViewById(R.id.image);
        update = findViewById(R.id.update);
        name = findViewById(R.id.name);
        database = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        storage = FirebaseStorage.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    name.setEnabled(false);
                    update.setEnabled(false);
                    image.setEnabled(false);
                    storage.putFile(imgUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.child("name").setValue(name.getText().toString());
                                            database.child("image").setValue(uri.toString());
                                            startActivity(new Intent(Profile.this, MainActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private boolean check() {
        if (name.getText().toString().length() < 5){
            name.setError("full name required");
            return false;
        }
        if (imgUri == null){
            Toast.makeText(this, "please insert profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                    Toast.makeText(Profile.this, ""+exception.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            builder.build().load(imgUri).into(image);
        }
        else {
            Toast.makeText(this, "" + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

}