package com.example.carrenting.Service.UserAuthentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmailResign;
    private TextView tvResendPassword;
    private Button btnReturn, btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        overridePendingTransition(R.anim.anim_in_left,R.anim.anim_out_right);

        edtEmailResign = findViewById(R.id.edtEmailResign);
        tvResendPassword = findViewById(R.id.tvResendPassword);
        btnReturn = findViewById(R.id.btnReturn);
        btnVerify = findViewById(R.id.btnVerify);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnLogin();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edtEmailResign.getText().toString().trim();
                SendPassword(strEmail);
                btnVerify.setVisibility(View.GONE);
            }
        });
        tvResendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVerify.setVisibility(View.VISIBLE);
            }
        });
    }

    private void SendPassword(String strEmail) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    private void returnLogin() {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}