package com.example.carrenting.Service.UserAuthentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private static final String TAG = "LoginActivity";
    private Button btnSignIn, btnSignUp, btnForget;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String email, password, username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        overridePendingTransition(R.anim.anim_in_left,R.anim.anim_out_right);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                signIn(email, password);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String iemail = inputEmail.getText().toString();

                try {
                    if(!iemail.isEmpty()){
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    intent.putExtra("email", iemail);
                    startActivity(intent);
                }
                else
                {

                        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        startActivity(intent);
                }
                }catch (Exception ex)
                {
                    Log.e(TAG, ex.toString());
                }

            }
        });

    }

    private void signIn(String email, String password){
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
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            FirebaseFirestore.getInstance().collection("Users").document(uid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // do something with the retrieved data
                                                    String username = document.getString("username");
                                                    String phonenumber = document.getString("phoneNumber");
                                                    if (username != null && username.isEmpty()) {
                                                        Intent intent = new Intent(LoginActivity.this, ValidatePhoneActivity.class);
                                                        intent.putExtra("phone", phonenumber);
                                                        startActivity(intent);
                                                    }
                                                    else
                                                    {
                                                        Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                                                        startActivity(intent);
                                                    }

                                                } else {
                                                    // the document does not exist
                                                }
                                            } else {
                                                // handle the error
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });

    }
    private boolean validateForm() {
        boolean valid = true;
        String email = inputEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Required.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }
        String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }
        return valid;
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btn_signUp);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.btn_signIn);
        btnForget = findViewById(R.id.btn_forget);
        progressDialog = new ProgressDialog(this);
    }

}
