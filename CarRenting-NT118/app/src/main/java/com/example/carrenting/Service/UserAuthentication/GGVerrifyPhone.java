package com.example.carrenting.Service.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.carrenting.R;

public class GGVerrifyPhone extends AppCompatActivity {

    private Button btnContinue;
    private EditText inputPhone;

    private void init()
    {
        btnContinue = findViewById(R.id.btn_continue);
        inputPhone = findViewById(R.id.input_phone_number);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ggverrify_phone);

        init();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GGVerrifyPhone.this, ValidatePhoneActivity.class);
                intent.putExtra("phone", inputPhone.getText().toString());
                startActivity(intent);
            }
        });
    }
}