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

import com.example.carrenting.Model.User;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.carrenting.Model.CreateOrder;
import com.example.carrenting.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ConfirmAndPaymentActivity extends AppCompatActivity {
    AppCompatButton btn_payment, back ;
    TextView txtToken;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String amount ="120000";
    private String fee="0";
    int environment = 0;
    private String merchantName = "Nhom1";
    private String merchantCode = "Nhom1";
    private String description = "Thanh toan tien thue xe";
    private User user;
    String token = "";


    private void BindView() {
        txtToken = findViewById(R.id.txtToken);
        btn_payment = findViewById(R.id.btn_Payment);
    }

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

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txtToken.setVisibility(View.INVISIBLE);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        //Bind components with ids
        //BindView();

        //back button
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

            // handle CreateOrder
            btn_payment.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    CreateOrder orderApi = new CreateOrder();

                    try {
                        JSONObject data = orderApi.createOrder(amount);
                        Log.d("Amount", amount);

                        String code = data.getString("return_code");
                        Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                        if (code.equals("1")) {
                            txtToken.setText(data.getString("zp_trans_token"));
                            IsDone();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String token = txtToken.getText().toString();
                    ZaloPaySDK.getInstance().payOrder(ConfirmAndPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ConfirmAndPaymentActivity.this)
                                            .setTitle("Payment Success")
                                            .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .setNegativeButton("Cancel", null).show();
                                }

                            });
                            IsLoading();
                        }

                        @Override
                        public void onPaymentCanceled(String zpTransToken, String appTransID) {
                            new AlertDialog.Builder(ConfirmAndPaymentActivity.this)
                                    .setTitle("User Cancel Payment")
                                    .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton("Cancel", null).show();
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                            new AlertDialog.Builder(ConfirmAndPaymentActivity.this)
                                    .setTitle("Payment Fail")
                                    .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton("Cancel", null).show();
                        }
                    });
                }
            });
        }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            ZaloPaySDK.getInstance().onResult(intent);
        }
    }