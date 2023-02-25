package com.example.carrenting.Service.UserAuthentication;

import android.app.ProgressDialog;
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

import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

public class RegisterActivity extends AppCompatActivity{

    private static final String TAG = "RegisterActivity";

    @Pattern(regex = "[0-9]{10}", message = "Vui lòng nhập đúng số điện thoại")
    private EditText inputPhone;
    @Email
    private EditText inputEmail;
    @Password(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự")

    private EditText inputPass, reinputPass;
    private Button btnSignUp;
    private FirebaseAuth mAuth;

    private FirebaseUser firebaseUser;
    private String Phone, Email, Password, rePassword;
    private ProgressDialog progressDialog;
    private Boolean isValid = true;
    private FirebaseFirestore dtbUser;
    private User user;


    private void init(){
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        inputPass = findViewById(R.id.input_password);
        reinputPass = findViewById(R.id.reinput_password);
        btnSignUp = findViewById(R.id.btn_signup);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        init();

        mAuth = FirebaseAuth.getInstance();
        dtbUser = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
                if (isValid)
                {
                    createAccount();
                }

            }
        });
    }

    private void checkPassword() {
        Password = inputPass.getText().toString().trim();
        rePassword = reinputPass.getText().toString().trim();
        if (!Password.equals(rePassword))
        {
            Toast.makeText(this,"Mật khẩu không khớp, mời nhập lại",Toast.LENGTH_LONG).show();
            inputPass.setText("");
            reinputPass.setText("");
            isValid = false;
        }
        else if(Password.isEmpty())
        {
            Toast.makeText(this,"Vui lòng nhập mật khẩu",Toast.LENGTH_LONG).show();
            inputPass.setText("");
            reinputPass.setText("");
            isValid = false;
        }
    }
    private void createAccount(){

        Phone = inputPhone.getText().toString();
        Email = inputEmail.getText().toString();
        Password = inputPass.getText().toString();
        rePassword = reinputPass.getText().toString();

        if (!validateForm()) {
            return;
        }
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Tạo User thành công");

                            firebaseUser = mAuth.getCurrentUser();

                            if (firebaseUser != null) {
                                firebaseUser.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(RegisterActivity.this, "Verification email sent to " + Email, Toast.LENGTH_LONG).show();

                                                    createUser();
                                                } else {
                                                    Log.e("TAG", "sendEmailVerification failed!", task.getException());
                                                }
                                            }
                                        });
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Không thể tạo User",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
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

        String password = inputPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPass.setError("Required.");
            valid = false;
        } else {
            inputPass.setError(null);
        }

        String phone = inputPhone.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPhone.setError("Required.");
            valid = false;
        } else {
            inputPhone.setError(null);
        }

        return valid;
    }
    private void createUser(){
        user = new User();
        user.setEmail(Email);
        user.setPhoneNumber(Phone);
        user.setUser_id(FirebaseAuth.getInstance().getUid());

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        dtbUser.setFirestoreSettings(settings);

        DocumentReference newUserRef = dtbUser
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid());
        newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                }else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });
    }
}
