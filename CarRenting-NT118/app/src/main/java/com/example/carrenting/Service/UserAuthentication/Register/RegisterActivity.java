package com.example.carrenting.Service.UserAuthentication.Register;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.ActivityPages.ProfileActivity;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.rpc.context.AttributeContext;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class RegisterActivity extends AppCompatActivity{

    @Pattern(regex = "[0-9]{9}", message = "Vui lòng nhập đúng số điện thoại")
    private EditText edtTxtPhone;
    @Email
    private EditText edtTxtEmail;
    @Password(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự")
    private EditText edtTxtPassword, edtTxtPasswordAgain;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private Validator validator;
    private String strPhone, strEmail, strPassword;
    private ProgressDialog progressDialog;
    Boolean isValid = true;
    private FirebaseFirestore mDb;
    User user;


    private void findViewbyIds(){
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPhone = findViewById(R.id.edtTxtCode);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtPasswordAgain = findViewById(R.id.edtTxtPasswordAgain);
        btnSignUp = findViewById(R.id.btnValidate);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();


        findViewbyIds();

        progressDialog = new ProgressDialog(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
                if (isValid)
                {
                    signUp();
                }

            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        strPhone = edtTxtPhone.getText().toString().trim();
        strEmail = edtTxtEmail.getText().toString().trim();
        strPassword = edtTxtPassword.getText().toString().trim();
    }
    private void checkPassword() {
        validator.validate();
        String strPasswordAgain = edtTxtPasswordAgain.getText().toString().trim();
        if (strPassword.equals(strPasswordAgain) == false || strPassword.equals("") == true)
        {
            Toast.makeText(this,"Mật khẩu không khớp, mời nhập lại",Toast.LENGTH_LONG);
            edtTxtPassword.setText("");
            edtTxtPasswordAgain.setText("");
            isValid = false;
        }
    }
    private void signUp() {

        Log.d(strEmail,strPassword);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            send();

                            Intent intent = getIntent();
                            String emailLink = intent.getData().toString();

                            String email = null;
                            if (mAuth.isSignInWithEmailLink(emailLink)) {
                                email = user.getEmail();
                            }
                            Toast.makeText(RegisterActivity.this,email,Toast.LENGTH_LONG).show();
                            mAuth.signInWithEmailLink(email, emailLink)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "Đăng nhập với email link thành công");
                                                AuthResult result = task.getResult();

                                                if (result.getAdditionalUserInfo().isNewUser()) {
                                                    Toast.makeText(RegisterActivity.this, "User này đã tồn tại", Toast.LENGTH_LONG).show();
                                                    firebaseUser.delete();
                                                } else {
                                                    Intent next = new Intent(RegisterActivity.this, ProfileActivity.class);
                                                    startActivity(next);
                                                }
                                            }
                                            else {
                                                Log.e("TAG", "Không thể đăng nhập với email link", task.getException());
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void send(){
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RegisterActivity.this, "Gửi yêu cầu xác thực email", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Không thể gửi yêu cầu xác thực email", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createUser(){
        user = new User();
        user.setEmail(strEmail);
        user.setUsername(strEmail.substring(0, strEmail.indexOf("@")));
        user.setUser_id(FirebaseAuth.getInstance().getUid());
        user.setPassword(strPassword);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        mDb.setFirestoreSettings(settings);

        DocumentReference newUserRef = mDb
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid());
        newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegisterActivity.this, ValidatePhoneActivity.class);
                    intent.putExtra("phone", strPhone);
                    startActivity(intent);

                }else{
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });
    }

}
