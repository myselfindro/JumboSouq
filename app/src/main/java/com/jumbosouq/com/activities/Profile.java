package com.jumbosouq.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jumbosouq.com.R;

public class Profile extends AppCompatActivity {
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
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
