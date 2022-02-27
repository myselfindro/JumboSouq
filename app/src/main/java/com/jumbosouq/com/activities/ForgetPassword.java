package com.jumbosouq.com.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.R;
import com.jumbosouq.com.internet.CheckConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {
    Button reset_btn;
    EditText etEmail;
    ImageView btn_back;
    private static final String TAG = "Myapp";
    private int mStatusCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etEmail = findViewById(R.id.etEmail);
        reset_btn = findViewById(R.id.reset_btn);
        btn_back = findViewById(R.id.btn_back);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etEmail.getText().toString().length() == 0) {

                    Toast.makeText(ForgetPassword.this, "Please Enter Email", Toast.LENGTH_SHORT).show();

                } else {

                    forgotpassword();
                }


            }
        });

    }

    public void forgotpassword() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("email", etEmail.getText().toString());
                params.put("template", "email_reset");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, Allurl.ForgotPassword, params, response -> {



//                try {
//                    JSONObject result = new JSONObject(String.valueOf(response));
//                    String msg = result.getString("message");
//                    Log.d(TAG, "msg-->" + msg);
//                    String stat = result.getString("stat");
//                    if (stat.equals("succ")) {
//
//                        Toast.makeText(Forgetpassword.this, msg, Toast.LENGTH_SHORT).show();
//                        hideProgressDialog();
//                        Intent intent = new Intent(Forgetpassword.this, Emailverification2.class);
//                        intent.putExtra("email", email);
//                        startActivity(intent);
//                        Log.d(TAG, "unsuccessfull - " + "Error");
//
//
//                    } else {
//                        Toast.makeText(Forgetpassword.this, msg, Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(ForgetPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    Intent intent = new Intent(ForgetPassword.this, ForgotPasswordEmailpage.class);
                    intent.putExtra("email", etEmail.getText().toString());
                    startActivity(intent);

                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int statusCode = response.statusCode;
                    Log.d("statuscode-->", String.valueOf(statusCode));
                    if (String.valueOf(statusCode).equals("200")) {

                        hideProgressDialog();
                        Intent intent = new Intent(ForgetPassword.this, ForgotPasswordEmailpage.class);
                        intent.putExtra("email", etEmail.getText().toString());
                        startActivity(intent);

                    } else {

                        Toast.makeText(ForgetPassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                    }

                    return null;
                }
            };

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();


        }

    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);

        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}