package com.jumbosouq.com.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.CategoryModel;
import com.jumbosouq.com.Modelclass.SubcategoryModel;
import com.jumbosouq.com.Modelclass.SupersubcategoryModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.CategoryAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category extends AppCompatActivity {

    ImageView iv, footer_categories_img, btnSearch, cart;
    DrawerLayout dl;
    RelativeLayout rl_Logout, btnAboutus, btnContactus, btnTandC, btnFAQ, btnCancel, btnRefund, btnNewsletter;
    LinearLayout btnPrivacypolicy;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApi;
    String token, username, customertoken, firstname, latname;
    private static final String TAG = "Myapp";
    RelativeLayout rl_main, rl_search;
    TextView user_profilename,footer_categories_txt, tvLogout;
    RecyclerView rv_category;
    ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter categoryAdapter;
    LinearLayout footer_Home_ll, footer_cart_ll,footer_myAccount_ll;
    JSONObject jsonBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        customertoken = sharedPreferences.getString("customertoken", "");
        firstname = sharedPreferences.getString("firstname", "");
        latname = sharedPreferences.getString("lastname","");

        iv = findViewById(R.id.iv);
        dl = (DrawerLayout) findViewById(R.id.dl);
        rl_Logout = findViewById(R.id.rl_Logout);
        user_profilename = findViewById(R.id.user_profilename);
        user_profilename.setText(username);
        rl_main = findViewById(R.id.rl_main);
        rv_category = findViewById(R.id.rv_category);
        footer_Home_ll = findViewById(R.id.footer_Home_ll);
        footer_cart_ll = findViewById(R.id.footer_cart_ll);
        footer_myAccount_ll = findViewById(R.id.footer_myAccount_ll);
        footer_categories_img = findViewById(R.id.footer_categories_img);
        btnSearch = findViewById(R.id.btnSearch);
        tvLogout = findViewById(R.id.tvLogout);
        cart = findViewById(R.id.cart);

        btnAboutus = findViewById(R.id.btnAboutus);
        btnContactus = findViewById(R.id.btnContactus);
        btnTandC = findViewById(R.id.btnTandC);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnCancel = findViewById(R.id.btnCancel);
        btnRefund = findViewById(R.id.btnRefund);
        btnNewsletter = findViewById(R.id.btnNewsletter);
        btnPrivacypolicy = findViewById(R.id.btnPrivacypolicy);

        footer_categories_txt = findViewById(R.id.footer_categories_txt);
        footer_categories_img.setColorFilter(getApplication().getResources().getColor(R.color.secondary_color_red));


        mGoogleApi = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        if (username == null) {
            username = "Hello, Guest";
            tvLogout.setText("Login");
        } else {
            username = "Hello, " + sharedPreferences.getString("username", "");
            tvLogout.setText("Logout");

        }

        Onclick();
        category();


        setBottomNavigation();

    }
    private void setBottomNavigation() {
        footer_categories_txt.setTextColor(getApplication().getResources().getColor(R.color.secondary_color_red));
    }

    public void Onclick() {

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/about-us/");
                startActivity(intent);
            }
        });

        btnContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/contact/");
                startActivity(intent);

            }
        });

        btnTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/terms-conditions/");
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/faq/index/");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/cancellation-returns/");
                startActivity(intent);
            }
        });

        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/returns-policy/");
                startActivity(intent);
            }
        });

        btnNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (username.equals("Hello, Guest")) {
                    Toast.makeText(getApplicationContext(), "Please Login for Subscribed", Toast.LENGTH_SHORT).show();
                } else {
                    newsletter();
                }

            }
        });


        btnPrivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Category.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/privacy-policy/");
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        footer_Home_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, MainActivity.class);
                startActivity(intent);
            }
        });
        footer_myAccount_ll.setOnClickListener(v -> {

            Intent intent = new Intent(Category.this, MyAccount.class);
            startActivity(intent);

        });
        footer_cart_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Category.this, CartActivity.class);
                startActivity(intent);

            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });


        rl_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Category.this);
                // builder.setCancelable(false);
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        dialog.cancel();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }


    public void newsletter(){


        final EditText etEmail;
        LinearLayout btnUnsubscribe, btnSubscribe;
        final Dialog dialog = new Dialog(Category.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dailouge_newsletter);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        etEmail = dialog.findViewById(R.id.etEmail);
        btnUnsubscribe = dialog.findViewById(R.id.btnUnsubscribe);
        btnSubscribe = dialog.findViewById(R.id.btnSubscribe);


        btnSubscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please Enter Valid Email");
                } else {
                    //updating note

                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


                        showProgressDialog();

                        JSONObject params = new JSONObject();

                        try {
                            jsonBody = new JSONObject("{\"customer\":{\"firstname\":\""+firstname+"\",\"lastname\":\""+latname+"\",\"email\":\""+username+"\",\"website_id\":1,\"extension_attributes\":{\"is_subscribed\":true}}}");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Subscribe, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            Toast.makeText(Category.this, "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(Category.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Category.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please Enter Valid Email");
                } else {
                    //updating note

                    if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


                        showProgressDialog();

                        JSONObject params = new JSONObject();

                        try {
                            jsonBody = new JSONObject("{\"customer\":{\"firstname\":\""+firstname+"\",\"lastname\":\""+latname+"\",\"email\":\""+username+"\",\"website_id\":1,\"extension_attributes\":{\"is_subscribed\":false}}}");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Subscribe, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            Toast.makeText(Category.this, "Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(Category.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Category.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });



    }



    public void category() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Allurl.Category,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                categoryModelArrayList = new ArrayList<>();
                                JSONArray children_data = result.getJSONArray("children_data");
                                for (int i = 0; i < children_data.length(); i++) {

                                    CategoryModel categoryModel = new CategoryModel();
                                    JSONObject responseobj = children_data.getJSONObject(i);
                                    JSONArray childrenobj = responseobj.getJSONArray("children_data");
                                    List<SubcategoryModel> subcatlist = new ArrayList<>();
                                    for (int j = 0; j < childrenobj.length(); j++) {

                                        SubcategoryModel subcategoryModel = new SubcategoryModel();
                                        JSONObject subcategoryobj = childrenobj.getJSONObject(j);
//
//                                        JSONArray superchildrenobj = subcategoryobj.getJSONArray("children_data");
//                                        List<SupersubcategoryModel> supersubcatlist = new ArrayList<>();
//                                        for (int k = 0; k < superchildrenobj.length(); k++){
//                                            SupersubcategoryModel supersubcategoryModel = new SupersubcategoryModel();
//                                            JSONObject supersubcatobj = superchildrenobj.getJSONObject(k);
//                                            supersubcategoryModel.setSupersubcategoryname(supersubcatobj.getString("name"));
//                                            supersubcategoryModel.setSupersubcategoryname(supersubcatobj.getString("id"));
//                                            supersubcatlist.add(supersubcategoryModel);
//                                        }

                                        subcategoryModel.setSubcategoryname(subcategoryobj.getString("name"));
                                        subcategoryModel.setId(subcategoryobj.getString("id"));
                                        subcatlist.add(subcategoryModel);
                                    }

                                    categoryModel.setCategoryName(responseobj.getString("name"));
                                    categoryModel.setCategoryid(responseobj.getString("id"));
                                    categoryModel.setSubcategoryName(subcatlist);

                                    categoryModelArrayList.add(categoryModel);


                                    categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryModelArrayList);
                                    rv_category.setAdapter(categoryAdapter);
                                    rv_category.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            hideProgressDialog();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
//                            Toast.makeText(Category.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    protected void onStart() {
        super.onStart();
        mGoogleApi.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApi.isConnected()) {
            mGoogleApi.disconnect();
        }
    }


    private void signOut() {
        if (mGoogleApi.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApi);
            mGoogleApi.disconnect();
            mGoogleApi.connect();
        }
    }


    public void logout() {


        Toast.makeText(Category.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(Category.this, GuestLoginActivity.class));
        finish();


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

}