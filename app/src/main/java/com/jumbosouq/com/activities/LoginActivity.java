package com.jumbosouq.com.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jumbosouq.com.Helper.Config;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.SignInModel;
import com.jumbosouq.com.Network.Api;
import com.jumbosouq.com.R;
import com.jumbosouq.com.session.SessionManager;
import com.subhrajyoti.passwordview.PasswordView;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "myapp";
    public ProgressDialog mProgressDialog;
    Button login_btn, sign_up_btn;
    EditText user_emil_edt;
    PasswordView user_Password_edt;
    TextView forgotpassword_tex;
    String username, password, customertoken;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        login_btn = findViewById(R.id.login_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        forgotpassword_tex = findViewById(R.id.forgotpassword_tex);
        user_emil_edt = findViewById(R.id.user_emil_edt);
        user_Password_edt = findViewById(R.id.user_Password_edt);
        sessionManager = new SessionManager(getApplicationContext());


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = user_emil_edt.getText().toString();
                password = user_Password_edt.getText().toString();

                if (username.matches("")) {
                    user_emil_edt.setError(getResources().getString(R.string.nameerror));
                    Config.showToast(LoginActivity.this, getResources().getString(R.string.nameerror), Config.SHORT);
                } else if (password.matches("")) {
                    user_Password_edt.setError(getResources().getString(R.string.passerror));
                    Config.showToast(LoginActivity.this, getResources().getString(R.string.passerror), Config.SHORT);
                } else {
                    if (Config.isOnline(LoginActivity.this)) {
                        try {
                            login();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.noonternate), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);

            }
        });

        forgotpassword_tex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(i);
            }
        });

    }

    private void login() {

        System.out.println("user_emil_edt====>" + user_emil_edt.getText().toString());
        System.out.println("user_Password_edt====>" + user_Password_edt.getText().toString());

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
                        } else if (response.code() == 401) {
                            System.out.println("interceptor3=>" + response);
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

        SignInModel signUpRequestData = new SignInModel();
        signUpRequestData.setUsername(user_emil_edt.getText().toString());
        signUpRequestData.setPassword(user_Password_edt.getText().toString());


        Call<String> userCall = apiInterface.signIn(signUpRequestData);
        userCall.enqueue(new Callback<String>() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

//                dialog.dismiss();
                if (response.body() != null) {

                    Set<String> strings = new LinkedHashSet<>();
                    String serverToken = response.body();
                    String result = serverToken.replaceAll("\"", "");
                    strings.add("Bearer");
                    strings.add(result);
                    customertoken = String.join(" ", strings);
                    Log.d(TAG, "customertoken-->" + customertoken);
                    Config.customertoken = customertoken;
                    sessionManager.setPrefString("customertoken", customertoken);

                    adminSignin();

                } else {
                    // error case
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(LoginActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(LoginActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(LoginActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<String> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errormsg), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void adminSignin() {


        showProgressDialog();


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
                        } else if (response.code() == 401) {
                            System.out.println("interceptor3=>" + response);
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

        SignInModel signUpRequestData = new SignInModel();
        signUpRequestData.setUsername("carmatec");
        signUpRequestData.setPassword("SHAHID@12345");
//        signUpRequestData.setUsername(user_emil_edt.getText().toString());
//        signUpRequestData.setPassword(user_Password_edt.getText().toString());


        Call<String> userCall = apiInterface.signInadmin(signUpRequestData);
        userCall.enqueue(new Callback<String>() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.body() != null) {


                    Set<String> strings = new LinkedHashSet<>();
                    String serverToken = response.body();
                    String result = serverToken.replaceAll("\"", "");
                    strings.add("Bearer");
                    strings.add(result);
                    String token = String.join(" ", strings);
                    Log.d(TAG, "token-->" + token);
                    Config.token = token;
                    sessionManager.setPrefString("token", token);
                    sessionManager.setPrefString("username", user_emil_edt.getText().toString());
                    sessionManager.setPrefBoolean("Login_status", true);
                    sessionManager.setPrefBoolean(SessionManager.IS_GUEST, false);
                    SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).edit();
                    editor.putString("token", token);
                    editor.putString("username", user_emil_edt.getText().toString());
                    editor.putString("customertoken", customertoken);
                    editor.putString("Login_status", "true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    sessionManager.createLoginSession(username, password,false);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                } else {
                    // error case
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(LoginActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(LoginActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(LoginActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<String> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errormsg), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void onBackPressed() {


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //   android.os.Process.killProcess(android.os.Process.myPid());
        android.os.Process.killProcess(android.os.Process.myUid());
        finish();
        //onStop();
    }

}
