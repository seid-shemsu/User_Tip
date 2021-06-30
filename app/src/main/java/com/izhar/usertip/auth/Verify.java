package com.izhar.usertip.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.izhar.usertip.R;
import com.izhar.usertip.user.Profile;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth mAuth;

    ProgressBar progressBar;
    EditText editText;
    Button buttonSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        setTitle("Verification");
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress);
        editText = findViewById(R.id.code);
        buttonSignIn = findViewById(R.id.verify);

        String phoneNumber = getIntent().getStringExtra("phone");

        sendVerificationCode(phoneNumber);

        // save phone number
        SharedPreferences prefs = getSharedPreferences("profile", MODE_PRIVATE);
        prefs.edit().putString("phone", phoneNumber).apply();

        buttonSignIn.setOnClickListener(v -> {

            String code = editText.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                editText.setError("Enter code...");
                editText.requestFocus();
                return;
            }
            verifyCode(code);
        });
    }
    private void verifyCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
            //FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential);
        } catch (Exception e) {

        }
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Verify.this, Profile.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Verify.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );

        progressBar.setVisibility(View.GONE);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify.this, e.getMessage(), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };
}