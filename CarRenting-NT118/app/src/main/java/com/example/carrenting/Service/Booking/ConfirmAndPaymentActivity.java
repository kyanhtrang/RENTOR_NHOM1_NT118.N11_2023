package com.example.carrenting.Service.Booking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.carrenting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vnpay.authentication.VNP_AuthenticationActivity;
import com.vnpay.authentication.VNP_SdkCompletedCallback;


public class ConfirmAndPaymentActivity extends AppCompatActivity {
    AppCompatButton btn_payment, back ;
    String vnp_url, vnp_tmnCode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void IsLoading() {
        btn_payment.setVisibility(View.INVISIBLE);
    }

    private void IsDone() {
        btn_payment.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        init();
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSdk();
            }
        });
    }

    private void openSdk(){
        db = FirebaseFirestore.getInstance();
        db.collection("VNPayCredentials")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot info : task.getResult()){
                                vnp_url = info.get("vnp_Url").toString();
                                vnp_tmnCode = info.get("vnp_TmnCode").toString();
                                vnpparam();
                                VNP_AuthenticationActivity.setSdkCompletedCallback(new VNP_SdkCompletedCallback() {
                                    @Override
                                    public void sdkAction(String s) {
                                        Log.wtf("Payment", "action" + s);
                                    }});
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ConfirmAndPayment", e.toString());
                        return;
                    }
                });

    }

    private void init() {
        btn_payment = findViewById(R.id.btn_checkout_pay);
    }
    private void vnpparam(){
        Intent intent = new Intent(this, VNP_AuthenticationActivity.class);
        intent.putExtra("url",vnp_url);
        intent.putExtra("tmp_code",vnp_tmnCode);
        intent.putExtra("scheme","RequestSuccessActivity");
        intent.putExtra("is_sandbox", true);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//            ZaloPaySDK.getInstance().onResult(intent);
    }
}