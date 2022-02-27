package com.jumbosouq.com.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jumbosouq.com.Helper.Config;
import com.jumbosouq.com.Modelclass.Customer;
import com.jumbosouq.com.Modelclass.SignUpData;
import com.jumbosouq.com.Modelclass.SignUpModel;
import com.jumbosouq.com.Network.Api;
import com.jumbosouq.com.R;
import com.subhrajyoti.passwordview.PasswordView;

import java.io.IOException;
import java.util.Calendar;
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

public class SignupActivity extends AppCompatActivity/* implements AdapterView.OnItemSelectedListener*/ {

    private static final String TAG = "Tag";
    Button Continue_btn;
    TextView user_Date_of_birth_edt;
    EditText user_fname_edt, user_lstname_edt, user_email_edt;
    EditText user_wedani_edt, user_mukafa_membership_edt, phone_edit;
    PasswordView user_Password_edt, user_confirm_Password_edt;
    int pos = 1;
    String[] gender = {"Male", "Female", "Not Specified"};
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView btn_back;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        init();

        user_Date_of_birth_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DOB();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_fname_edt.getText().toString().matches("")) {
                    user_fname_edt.setError("Enter First name");
                    Config.showToast(SignupActivity.this, "Enter First name", Config.SHORT);
                } else if (user_lstname_edt.getText().toString().matches("")) {
                    user_lstname_edt.setError("Enter Last name");
                    Config.showToast(SignupActivity.this, "Enter Last name", Config.SHORT);
                } else if (!user_email_edt.getText().toString().trim().matches(emailPattern)) {
                    user_email_edt.setError("Enter a valid Email");
                    Config.showToast(SignupActivity.this, "Enter a valid Email", Config.SHORT);
                } else if (user_email_edt.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Email Address.", Toast.LENGTH_SHORT).show();
                }
//                else if (phone_edit.getText().toString().matches("")) {
//                    phone_edit.setError("Enter Your Mobile number");
//                    Config.showToast(SignupActivity.this, "Enter Your Mobile number", Config.SHORT);
//                }
                /* else if (user_wedani_edt.getText().toString().matches("")) {
                    user_wedani_edt.setError("Fill the space");
                    Config.showToast(SignupActivity.this, "Fill the space", Config.SHORT);
                } else if (user_Date_of_birth_edt.getText().toString().matches("")) {
                    user_Date_of_birth_edt.setError("Enter your DOB");
                    Config.showToast(SignupActivity.this, "Enter your DOB", Config.SHORT);
                } else if (user_mukafa_membership_edt.getText().toString().matches("")) {
                    user_mukafa_membership_edt.setError("Fill the space");
                    Config.showToast(SignupActivity.this, "Fill the space", Config.SHORT);
                }*/
                else if (user_Password_edt.getText().toString().matches("")) {
                    user_Password_edt.setError("Enter password");
                    Config.showToast(SignupActivity.this, "Enter password", Config.SHORT);
                } else if (!user_confirm_Password_edt.getText().toString().matches(user_Password_edt.getText().toString())) {
                    user_Password_edt.setError("password Not match");
                    Config.showToast(SignupActivity.this, "password Not match", Config.SHORT);
                } else {

                    Registration();
                }
            }
        });

        /*user_gender_spinner.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                gender);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        user_gender_spinner.setAdapter(ad);*/

    }

    private void DOB() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        user_Date_of_birth_edt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void Registration() {

        final ProgressDialog dialog;
        dialog = new ProgressDialog(SignupActivity.this);
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

        /*CustomAttribute customAttribute = null;
        List<CustomAttribute> customAttributes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            customAttribute.setAttributeCode("wedding_anniversary");
            customAttribute.setValue(user_Date_of_birth_edt.getText().toString());
            customAttribute.setAttributeCode("loyalty_number");
            customAttribute.setValue(user_mukafa_membership_edt.getText().toString());
            customAttributes.add(customAttribute);
            //customAttribute = new CustomAttribute();
        }*/

        Customer details = new Customer();
        //details.setCustomAttributes(customAttributes);
        details.setDob(user_Date_of_birth_edt.getText().toString());
        details.setEmail(user_email_edt.getText().toString());
        details.setFirstname(user_fname_edt.getText().toString());
        details.setGender(pos);
        details.setLastname(user_lstname_edt.getText().toString());

        SignUpModel RequestData = new SignUpModel();
        RequestData.setPassword(user_Password_edt.getText().toString());
        RequestData.setCustomer(details);

        System.out.println("details==>" + details);
        System.out.println("user_Password_edt==>" + user_Password_edt.getText().toString());
        System.out.println("user_Date_of_birth_edt==>" + user_Date_of_birth_edt.getText().toString());
        System.out.println("user_email_edt==>" + user_email_edt.getText().toString());
        System.out.println("firstname==>" + user_fname_edt.getText().toString());
        System.out.println("user_lstname_edt==>" + user_lstname_edt.getText().toString());


        Call<SignUpData> userCall = apiInterface.signUp(RequestData);
        userCall.enqueue(new Callback<SignUpData>() {
            @Override
            public void onResponse(Call<SignUpData> call, Response<SignUpData> response) {
                dialog.dismiss();
                if (response.body() != null) {

                    Toast.makeText(SignupActivity.this, "Successfully registered, Login to continue...", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = SignupActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).edit();
                    editor.putString("Login_status", "true");
                    editor.putString("firstname", user_fname_edt.getText().toString());
                    editor.putString("lastname", user_lstname_edt.getText().toString());
                    editor.apply();

                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    // error case
                    switch (response.code()) {
                        case 400:
                            try {
                                String errorbody = response.errorBody().string();
                                String errorbodynew = errorbody.replaceAll("\\{|\\}|\\:","");
                                String errorbodynew2 = errorbodynew.replace("\"","");
                                String replaceString=errorbodynew2.replace("message","");
                                if (replaceString.contains("A customer")){

                                    Toast.makeText(SignupActivity.this, "Error! Wrong Password", Toast.LENGTH_SHORT).show();

                                }else if (replaceString.contains("The password needs")){

                                    Toast.makeText(SignupActivity.this, "The password needs at least one special characters. Create a new password and try again.", Toast.LENGTH_SHORT).show();

                                }else if (replaceString.contains("Minimum of")) {

                                    Toast.makeText(SignupActivity.this, "Minimum of different classes of characters in password is %1. Classes of characters Lower Case, Upper Case, Digits, Special Characters.,parameters[3]", Toast.LENGTH_SHORT).show();

                                }else {

                                    Toast.makeText(SignupActivity.this, replaceString, Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 401:
                            Toast.makeText(SignupActivity.this, "Unauthorized " + response.message(), Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(SignupActivity.this, "Forbidden " + response.message(), Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(SignupActivity.this, "Not Found " + response.message(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SignupActivity.this, "unknown error " + response.message(), Toast.LENGTH_SHORT).show();
                            break;
                    }

                    System.out.println("code=>" + response.code());
                    System.out.println("message=>" + response.message());
                    System.out.println("headers=>" + response.headers());
                    System.out.println("errorBody=>" + response.errorBody());

                }

            }

            @Override
            public void onFailure(Call<SignUpData> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.errormsg), Toast.LENGTH_SHORT).show();

            }
        });

    }



    private void init() {
        phone_edit = findViewById(R.id.phone_edit);
        Continue_btn = findViewById(R.id.Continue_btn);
        user_wedani_edt = findViewById(R.id.user_wedani_edt);
        user_Date_of_birth_edt = findViewById(R.id.user_Date_of_birth_edt);
        user_mukafa_membership_edt = findViewById(R.id.user_mukafa_membership_edt);
        user_Password_edt = findViewById(R.id.user_Password_edt);
        user_confirm_Password_edt = findViewById(R.id.user_confirm_Password_edt);
        user_fname_edt = findViewById(R.id.user_fname_edt);
        user_lstname_edt = findViewById(R.id.user_lstname_edt);
        // user_gender_edt = findViewById(R.id.user_gender_edt);
        user_email_edt = findViewById(R.id.user_email_edt);
        btn_back = findViewById(R.id.btn_back);
        // user_gender_spinner = findViewById(R.id.user_gender_spinner);
    }

}



