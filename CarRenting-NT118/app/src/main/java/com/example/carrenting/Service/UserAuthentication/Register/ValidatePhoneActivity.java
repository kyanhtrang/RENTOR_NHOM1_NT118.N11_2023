package com.example.carrenting.Service.UserAuthentication.Register;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrenting.ActivityPages.CustomerMainActivity;
import com.example.carrenting.FragmentPages.Customer.UserInfor.MyProfileFragment;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.concurrent.TimeUnit;

public class ValidatePhoneActivity extends AppCompatActivity {

    private EditText otpNumberOne, getOtpNumberTwo, getOtpNumberThree, getOtpNumberFour, getOtpNumberFive, otpNumberSix;
    private Button btnSendCode;
    private TextView tvResend;

    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    String phoneNumber;
    Boolean otpValid = true;

    private FirebaseAuth mAuth;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore mDb;
    PhoneAuthCredential phoneAuthCredential;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_phone);

        initUI();
        Intent data = getIntent();
        phoneNumber = data.getStringExtra("phone");

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        if (!phoneNumber.startsWith("+84"))
        {
            phoneNumber = "+84" + phoneNumber;
        }
        Toast.makeText(this,phoneNumber,Toast.LENGTH_LONG).show();
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateField(otpNumberOne);
                validateField(getOtpNumberTwo);
                validateField(getOtpNumberThree);
                validateField(getOtpNumberFour);
                validateField(getOtpNumberFive);
                validateField(otpNumberSix);

                if(otpValid) {
                    // send otp to the user
                    String otp = otpNumberOne.getText().toString() + getOtpNumberTwo.getText().toString() + getOtpNumberThree.getText().toString() + getOtpNumberFour.getText().toString() +
                            getOtpNumberFive.getText().toString() + otpNumberSix.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

                    verifyAuthentication(credential);

                    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                            .build();
                    mDb.setFirestoreSettings(settings);
                    DocumentReference newUserRef = mDb
                            .collection(getString(R.string.collection_users))
                            .document(FirebaseAuth.getInstance().getUid());

                    newUserRef.update("phoneNumber", phoneNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ValidatePhoneActivity.this, "Update phone number successful", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Intent intent = new Intent(ValidatePhoneActivity.this, SignProfileActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });


        SendOtpCode(phoneNumber);

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReSendOtpCode(phoneNumber);
          }

        });

    }
    private void VerifyPhoneNumber(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //verifyAuthentication(phoneAuthCredential);
                        tvResend.setVisibility(View.GONE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ValidatePhoneActivity.this, "OTP Verification Failed.", Toast.LENGTH_SHORT).show();
                    }

                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        Log.d(TAG, "onCodeSent:" + forceResendingToken);
                        mVerificationId = verificationId;
                        mResendToken = forceResendingToken;
                        tvResend.setVisibility(View.GONE);

                    }
                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        tvResend.setVisibility(View.VISIBLE);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void ReSendOtpCode(String phoneNumber) {
        VerifyPhoneNumber(phoneNumber);
    }

    private void SendOtpCode(String phoneNumber) {
        VerifyPhoneNumber(phoneNumber);
    }
    public void verifyAuthentication(PhoneAuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(ValidatePhoneActivity.this, "Acccount Created and Linked.", Toast.LENGTH_SHORT).show();
                // send to dashboard.
            }
        });
    }

    public void validateField(EditText field){
        if(field.getText().toString().isEmpty()){
            field.setError("Required");
            otpValid = false;
        }else {
            otpValid = true;
        }
    }
    private void initUI() {
        otpNumberOne = findViewById(R.id.otpNumberOne);
        getOtpNumberTwo = findViewById(R.id.otpNumberTwo);
        getOtpNumberThree = findViewById(R.id.otpNumberThree);
        getOtpNumberFour = findViewById(R.id.otpNumberFour);
        getOtpNumberFive = findViewById(R.id.otpNumberFive);
        otpNumberSix = findViewById(R.id.optNumberSix);

        btnSendCode = findViewById(R.id.btnSendCode);
        tvResend = findViewById(R.id.tvResend);
    }
}