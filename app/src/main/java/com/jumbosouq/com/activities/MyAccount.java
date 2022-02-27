package com.jumbosouq.com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.R;
import com.jumbosouq.com.session.SessionManager;

public class MyAccount extends AppCompatActivity {
    LinearLayout Profile_linear_lay, footer_myAccount_ll,
            Orders_linear_lay, Addresses_lay, MyWishlist_linear_lay;
    LinearLayout footer_Home_ll, footer_cart_ll, footer_categories_ll;
    ImageView footer_profile_img;
    TextView footer_profile_text;
    TextView Profile, Orders, Addresses_up,user_profilename;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        Profile = findViewById(R.id.Profile);
        user_profilename = findViewById(R.id.user_profilename);
        Orders = findViewById(R.id.Orders);
        Addresses_up = findViewById(R.id.Addresses_up);

        Profile_linear_lay = findViewById(R.id.Profile_linear_lay);
        footer_myAccount_ll = findViewById(R.id.footer_myAccount_ll);
        Orders_linear_lay = findViewById(R.id.Orders_linear_lay);
        Addresses_lay = findViewById(R.id.Addresses_lay);
        MyWishlist_linear_lay = findViewById(R.id.MyWishlist_linear_lay);
        footer_profile_img = findViewById(R.id.footer_profile_img);
        footer_profile_img.setBackgroundResource(R.drawable.profile_red);
        footer_Home_ll = findViewById(R.id.footer_Home_ll);
        footer_cart_ll = findViewById(R.id.footer_cart_ll);
        footer_categories_ll = findViewById(R.id.footer_categories_ll);
        footer_profile_text = findViewById(R.id.footer_profile_text);
        sessionManager= new SessionManager(getApplicationContext());

        user_profilename.setText("Hello, "+sessionManager.getPrefString("username"));

        if (sessionManager.isUserGuest()){
            showLoginDialog();
        }

        setBottomNavigation();
        clicklistener();



    }

    private void showLoginDialog() {

    }

    private void setBottomNavigation() {
        footer_profile_img.setColorFilter(getApplication().getResources().getColor(R.color.secondary_color_red));
        footer_profile_text.setTextColor(getApplication().getResources().getColor(R.color.secondary_color_red));
    }

    private void clicklistener() {
        footer_Home_ll.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccount.this, MainActivity.class);
            startActivity(intent);

        });
        footer_cart_ll.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccount.this, CartActivity.class);
            startActivity(intent);
        });
        footer_categories_ll.setOnClickListener(v -> {
            Intent intent = new Intent(MyAccount.this, Category.class);
            startActivity(intent);
        });
        Profile_linear_lay.setOnClickListener(v -> {
            setTintColor(1);
            Intent i = new Intent(MyAccount.this, Profile.class);
            startActivity(i);


        });

        Orders_linear_lay.setOnClickListener(v -> {
            setTintColor(2);
            Intent i = new Intent(MyAccount.this, OrdersActivity.class);
            startActivity(i);


        });

        Addresses_lay.setOnClickListener(v -> {
            setTintColor(3);
            Intent i = new Intent(MyAccount.this, AddressActivity.class);
            startActivity(i);

        });

        MyWishlist_linear_lay.setOnClickListener(v -> {

            Intent i = new Intent(MyAccount.this, MyWishlistActivity.class);
            startActivity(i);

        });
    }

    public void setTintColor(int tintColor) {

        if (tintColor == 1) {
            setTextViewDrawableColor(Profile, R.color.secondary_color_red);
            setTextViewDrawableColor(Orders, R.color.primary_color_blue);
            setTextViewDrawableColor(Addresses_up, R.color.primary_color_blue);

        } else if (tintColor == 2) {
            setTextViewDrawableColor(Profile, R.color.primary_color_blue);
            setTextViewDrawableColor(Orders, R.color.secondary_color_red);
            setTextViewDrawableColor(Addresses_up, R.color.primary_color_blue);
        } else if (tintColor == 3) {
            setTextViewDrawableColor(Addresses_up, R.color.secondary_color_red);
            setTextViewDrawableColor(Profile, R.color.primary_color_blue);
            setTextViewDrawableColor(Orders, R.color.primary_color_blue);
        }

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(getColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

}
