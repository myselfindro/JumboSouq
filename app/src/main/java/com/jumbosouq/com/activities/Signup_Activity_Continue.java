package com.jumbosouq.com.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jumbosouq.com.Helper.Config;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.CustomAttribute;
import com.jumbosouq.com.Modelclass.Customer;
import com.jumbosouq.com.Modelclass.SignInModel;
import com.jumbosouq.com.Modelclass.SignUpData;
import com.jumbosouq.com.Modelclass.SignUpModel;
import com.jumbosouq.com.Network.Api;
import com.jumbosouq.com.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signup_Activity_Continue extends AppCompatActivity {

    String user_id, user_lstname, user_email;
    int gender;
    EditText user_wedani_edt, user_Date_of_birth_edt, user_mukafa_membership_edt,
            user_Password_edt, user_confirm_Password_edt;
    Button reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_continue);

        // getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.splashbg));
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        init();

        user_id = (Signup_Activity_Continue.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).getString("user_id", ""));
        user_lstname = (Signup_Activity_Continue.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).getString("user_lstname", ""));
        user_email = (Signup_Activity_Continue.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).getString("user_email", ""));
        gender = (Signup_Activity_Continue.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).getInt("gender", 0));

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_wedani_edt.getText().toString().matches("")) {
                    user_wedani_edt.setError("Fill the space");
                    Config.showToast(Signup_Activity_Continue.this, "Fill the space", Config.SHORT);
                } else if (user_Date_of_birth_edt.getText().toString().matches("")) {
                    user_Date_of_birth_edt.setError("Enter your DOB");
                    Config.showToast(Signup_Activity_Continue.this, "Enter your DOB", Config.SHORT);
                } else if (user_mukafa_membership_edt.getText().toString().matches("")) {
                    user_mukafa_membership_edt.setError("Fill the space");
                    Config.showToast(Signup_Activity_Continue.this, "Fill the space", Config.SHORT);
                } else if (user_Password_edt.getText().toString().matches("")) {
                    user_Password_edt.setError("Enter passwprd");
                    Config.showToast(Signup_Activity_Continue.this, "Enter passwprd", Config.SHORT);
                } else {

                    Registration();
                }
            }
        });

    }

    private void Registration() {

        final ProgressDialog dialog;
        dialog = new ProgressDialog(Signup_Activity_Continue.this);
        dialog.setMessage("Loading, Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        // todo deal with the issues the way you need to
                        if (response.code() == 400) {
                            //Toast.makeText(Confirmation.this, "400", Toast.LENGTH_SHORT).show();
                            System.out.println("interceptor=>" + response);
                            return response;
                        } else if (response.code() == 200) {
                            System.out.println("interceptor2=>" + response);
                        } else {
                            System.out.println("interceptor_=>" + response);
                        }

                        return response;
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Config.BaseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api apiInterface = retrofit.create(Api.class);




        CustomAttribute customAttribute = null;

        List<CustomAttribute> customAttributes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            customAttribute.setAttributeCode("wedding_anniversary");
            customAttribute.setValue(user_Date_of_birth_edt.getText().toString());
            customAttribute.setAttributeCode("loyalty_number");
            customAttribute.setValue(user_mukafa_membership_edt.getText().toString());
            customAttributes.add(customAttribute);
            //customAttribute = new CustomAttribute();
        }

        Customer details = new Customer();
        details.setCustomAttributes(customAttributes);
        details.setDob(user_Date_of_birth_edt.getText().toString());
        details.setEmail(user_email);
        details.setFirstname(user_id);
        details.setGender(gender);
        details.setLastname(user_lstname);

        SignUpModel RequestData = new SignUpModel();
        RequestData.setPassword(user_Password_edt.getText().toString());
        RequestData.setCustomer(details);


        Call<SignUpData> userCall = apiInterface.signUp(RequestData);
        userCall.enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                dialog.dismiss();
                if (response.body() != null) {

                    Toast.makeText(Signup_Activity_Continue.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = Signup_Activity_Continue.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).edit();
                    editor.putString("Login_status", "true");
                    editor.apply();

                    Intent i = new Intent(Signup_Activity_Continue.this, MainActivity.class);
                    startActivity(i);
                } else {
                    // error case
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(Signup_Activity_Continue.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(Signup_Activity_Continue.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(Signup_Activity_Continue.this, "Forbidden", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(Signup_Activity_Continue.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(Signup_Activity_Continue.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //Toast.makeText(Dashboard.this, response.message(), Toast.LENGTH_SHORT).show();
                    System.out.println("code=>" + response.code());
                    System.out.println("message=>" + response.message());
                    System.out.println("headers=>" + response.headers());
                    System.out.println("errorBody=>" + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Signup_Activity_Continue.this, getResources().getString(R.string.errormsg), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void init() {
        user_wedani_edt = findViewById(R.id.user_wedani_edt);
        user_Date_of_birth_edt = findViewById(R.id.user_Date_of_birth_edt);
        user_mukafa_membership_edt = findViewById(R.id.user_mukafa_membership_edt);
        user_Password_edt = findViewById(R.id.user_Password_edt);
        user_confirm_Password_edt = findViewById(R.id.user_confirm_Password_edt);
        reg_btn = findViewById(R.id.reg_btn);
    }


}
