package com.jumbosouq.com.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jumbosouq.com.R;

public class OrdersActivity extends AppCompatActivity {

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);
        setUI();
    }
    private void setUI() {
        backBtn=findViewById(R.id.back);
        setClickListener();
    }

    private void setClickListener() {
        backBtn.setOnClickListener(v->{
            finish();
        });
    }

}
