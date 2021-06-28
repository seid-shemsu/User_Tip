package com.izhar.usertip.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.izhar.usertip.MainActivity;
import com.izhar.usertip.MainActivity2;
import com.izhar.usertip.R;
import com.izhar.usertip.user.Profile;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check())
                    login(login);
            }
        });
    }

    private boolean check() {
        if (email.getText().toString().trim().isEmpty()){
            email.setError("invalid email");
            return false;
        }
        if (password.getText().toString().trim().isEmpty()){
            password.setError("empty password");
            return false;
        }
        return true;
    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
        finish();
    }

    public void login(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        login.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        email.setEnabled(true);
                        password.setEnabled(true);
                        login.setEnabled(true);
                    }
                });
    }
}