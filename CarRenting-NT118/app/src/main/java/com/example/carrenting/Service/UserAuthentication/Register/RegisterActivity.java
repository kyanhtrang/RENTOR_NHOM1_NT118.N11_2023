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

import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Pattern(regex = "[0-9]{9}", message = "Please enter valid Phone number")
    private EditText edtTxtPhone;
    @Email
    private EditText edtTxtEmail;
    @Password(min = 6, message = "Password must have at-least 6 characters")
    private EditText edtTxtPassword, edtTxtPasswordAgain;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private Validator validator;

    private ProgressDialog progressDialog;
    Boolean isValid = true;
    private FirebaseFirestore mDb;


    @Override
    public void onValidationSucceeded() {
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        isValid = false;
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        validator = new Validator(this);
        validator.setValidationListener(this);

        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPhone = findViewById(R.id.edtTxtCode);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtPasswordAgain = findViewById(R.id.edtTxtPasswordAgain);
        btnSignUp = findViewById(R.id.btnSendCode);
        mDb = FirebaseFirestore.getInstance();

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

    private void checkPassword() {
        validator.validate();
        String strPassword = edtTxtPassword.getText().toString().trim();
        String strPasswordAgain = edtTxtPasswordAgain.getText().toString().trim();
        if (strPassword.equals(strPasswordAgain) == false || strPassword.equals("") == true)
        {
            Toast.makeText(this,"Mật khẩu không khớp, mời nhập lại",Toast.LENGTH_LONG);
            edtTxtPassword.setText("");
            edtTxtPasswordAgain.setText("");
            isValid = false;
        }
    }


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void signUp() {
        String strPhone = edtTxtPhone.getText().toString().trim();
        String strEmail = edtTxtEmail.getText().toString().trim();
        String strPassword = edtTxtPassword.getText().toString().trim();
        Log.d(strEmail,strPassword);
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            User user = new User();
                            user.setEmail(strEmail);
                            user.setUsername(strEmail.substring(0, strEmail.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());
                            user.setPassword(strPassword);

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                    .build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb
                                    .collection(getString(R.string.collection_users))
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
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



}
