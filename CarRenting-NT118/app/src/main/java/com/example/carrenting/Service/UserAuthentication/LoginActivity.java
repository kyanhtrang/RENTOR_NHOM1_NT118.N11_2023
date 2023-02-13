package com.example.carrenting.Service.UserAuthentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.ActivityPages.ProfileActivity;
import com.example.carrenting.R;
import com.example.carrenting.Service.UserAuthentication.Register.RegisterActivity;
import com.example.carrenting.Service.UserAuthentication.Register.ValidatePhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

public class LoginActivity extends AppCompatActivity {
    private EditText txtemail, txtpassword;
    private static final String TAG = "LoginActivity";
    private TextView txtSignUp;
    private Button btn_signIn;
    private TextView tvForgotPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        overridePendingTransition(R.anim.anim_in_left,R.anim.anim_out_right);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtemail.getText().toString();
                String password = txtpassword.getText().toString();
                signIn(email, password);
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtemail.getText().toString();
                String password = txtpassword.getText().toString();
                createAccount(email, password);
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

    }
    private void forgotPassword() {
        progressDialog.show();
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
    }
    private void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Tạo User thành công");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUIRegister(user);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Không thể tạo User", task.getException());
                            Toast.makeText(LoginActivity.this, "Không thể tạo User",Toast.LENGTH_SHORT).show();
                            updateUIRegister(null);
                        }
                        progressDialog.cancel();
                    }
                });
    }
    private void signIn(String email, String password){
        Log.d(TAG,"Đăng nhập với:" + email);
        if (!validateForm()){
            return;
        }

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Đăng nhập thành công");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUILogin(user);

                        } else {
                            Log.w(TAG, "Đăng nhập thất bại", task.getException());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                            updateUILogin(null);
                        }

                        progressDialog.cancel();
                    }
                });


    }
    private void updateUIRegister(FirebaseUser user) {
        if (user != null) {
            if (user.isEmailVerified()){
                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        }
        else {
            txtemail.setVisibility(View.GONE);
            txtpassword.setVisibility(View.GONE);
        }
    }
    private void updateUILogin(FirebaseUser user) {
//        if (user != null) {
//            if (user.isEmailVerified()){
                Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                startActivity(intent);
//            }
//        }
//        else {
//            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//            startActivity(intent);
//        }
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = txtemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtemail.setError("Required.");
            valid = false;
        } else {
            txtemail.setError(null);
        }
        String password = txtpassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtpassword.setError("Required.");
            valid = false;
        } else {
            txtpassword.setError(null);
        }
        return valid;
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        txtSignUp = findViewById(R.id.btn_signUp);
        txtemail = findViewById(R.id.email);
        txtpassword = findViewById(R.id.password);
        btn_signIn = findViewById(R.id.btn_signIn);
        tvForgotPassword = findViewById(R.id.btn_forget);
        progressDialog = new ProgressDialog(this);
    }

}
