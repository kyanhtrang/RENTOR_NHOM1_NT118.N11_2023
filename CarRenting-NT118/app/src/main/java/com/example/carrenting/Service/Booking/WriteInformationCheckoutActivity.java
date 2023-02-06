package com.example.carrenting.Service.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteInformationCheckoutActivity extends AppCompatActivity {
    Button btn_request, btn_back;
    TextView NgayNhan, NgayTra;
    TextView GioNhan, GioTra;
    // Ngày giờ nhận
    Calendar Nhan;
    // Ngày giờ trả
    Calendar Tra;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_infor_booking_car);

        initComponents();

        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);


        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Writeinfor=new Intent(WriteInformationCheckoutActivity.this, CustomerNotificationFragment.class);
                startActivity(Writeinfor);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Back_DetailCar=new Intent(WriteInformationCheckoutActivity.this, CarDetailActivity.class);
                startActivity(Back_DetailCar);
            }
        });
    }

    private void initComponents(){
        btn_request=findViewById(R.id.btn_requestbooking);
        btn_back=findViewById(R.id.back);
        NgayNhan=findViewById(R.id.edt_NgayNhan);
        NgayTra=findViewById(R.id.edt_NgayTra);
        GioNhan=findViewById(R.id.edt_GioNhan);
        GioTra=findViewById(R.id.edt_GioTra);
        Nhan=Calendar.getInstance();
        Tra = Calendar.getInstance();
        NgayNhan.setText(dateFormat.format(Nhan.getTime()));
        GioNhan.setText(dateFormat.format(Nhan.getTime()));

        NgayTra.setText(dateFormat.format(Tra.getTime()));
        GioTra.setText(dateFormat.format(Tra.getTime()));
    }

}
