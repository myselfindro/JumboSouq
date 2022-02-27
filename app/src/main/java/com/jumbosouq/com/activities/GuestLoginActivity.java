package com.jumbosouq.com.activities;

import android.app.Person;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jumbosouq.com.Helper.Config;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.SignInModel;
import com.jumbosouq.com.Network.Api;
import com.jumbosouq.com.R;
import com.jumbosouq.com.session.SessionManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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


public class GuestLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView  lgsignup_button;
    RelativeLayout continueasguest;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton login_button;
    SignInButton sign_in_button;
    LinearLayout ll_facebook, ll_google;
    private GoogleApiClient mGoogleApi;
    private static final String TAG = "Myapp";
    private static final int RC_SIGN_IN = 7;
    SessionManager sessionManager;
    String username, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guestlogin_activity);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
//        login_button = findViewById(R.id.login_button);
//        login_button.setReadPermissions(Arrays.asList(EMAIL));
        lgsignup_button = findViewById(R.id.lgsignup_button);
        continueasguest = findViewById(R.id.continueasguest);
//        ll_facebook = findViewById(R.id.ll_facebook);
//        ll_google = findViewById(R.id.ll_google);
//        sign_in_button = findViewById(R.id.sign_in_button);
        sessionManager = new SessionManager(getApplicationContext());
        password = "googlesignin";

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        callbackManager = CallbackManager.Factory.create();


        onClick();


    }


    public void onClick(){


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

//        ll_facebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                login_button.performClick();
//            }
//        });

//        ll_google.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent signIngoogle = Auth.GoogleSignInApi.getSignInIntent(mGoogleApi);
//                startActivityForResult(signIngoogle,RC_SIGN_IN);
//
//            }
//        });


//        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
//                Log.d("API123", loggedIn + " ??");
//                Intent i = new Intent(GuestLoginActivity.this, MainActivity.class);
//                startActivity(i);
//                // App code
//            }

//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });

        lgsignup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(GuestLoginActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        continueasguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adminSignin();


            }
        });

    }


    public void adminSignin(){


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


        Call<String> userCall = apiInterface.signInadmin(signUpRequestData);
        userCall.enqueue(new Callback<String>() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.body() != null) {


                    Set<String> strings = new LinkedHashSet<>();
                    String serverToken = response.body().toString();
                    String  result = serverToken.replaceAll("\"","");
                    strings.add("Bearer"); strings.add(result);
                    String token = String.join(" ", strings);
                    Log.d(TAG, "token-->"+token);
                    Config.token = token;
                    SharedPreferences.Editor editor = GuestLoginActivity.this.getSharedPreferences("DATA", Context.MODE_PRIVATE).edit();
                    editor.putString("token", token);
                    editor.putString("username", "Guest");
                    editor.putString("Login_status", "false");

                    sessionManager.setPrefString("token", token);
                    sessionManager.setPrefString("username", "Guest");
                    sessionManager.setPrefBoolean("Login_status", false);
                    sessionManager.setPrefBoolean(SessionManager.IS_GUEST, true);
                    editor.apply();
                    hideProgressDialog();
                    Intent i = new Intent(GuestLoginActivity.this, MainActivity.class);
                    startActivity(i);

                } else {
                    // error case
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(GuestLoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(GuestLoginActivity.this, "Unauthorized", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(GuestLoginActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(GuestLoginActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(GuestLoginActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GuestLoginActivity.this, getResources().getString(R.string.errormsg), Toast.LENGTH_SHORT).show();

            }
        });



    }

    public ProgressDialog mProgressDialog;

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("GoogleLogincode :", String.valueOf(requestCode));


        if (requestCode == RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handelSigninresult(result);
        }
    }




    private void handelSigninresult(GoogleSignInResult result) {

        Log.e("GoogleLogin :", String.valueOf(result.isSuccess()));
        final GoogleSignInAccount acct = result.getSignInAccount();
        String name = acct.getDisplayName();
        username = acct.getEmail();
        Log.e(TAG,"Gmail-->"+username);

        if (result.isSuccess()){

            sessionManager.createLoginSession(username, password,true);
            Toast.makeText(getApplicationContext(), "Sign in Success", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(GuestLoginActivity.this, MainActivity.class);
            startActivity(i);


        }else {

            Toast.makeText(getApplicationContext(), "Google Sign in not Success", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    private void getUserProfile(AccessToken currentAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(
//                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.d("TAG", object.toString());
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
//                            String id = object.getString("id");
//                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//
//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
//                            Picasso.with(MainActivity.this).load(image_url).into(imageView);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }


}
