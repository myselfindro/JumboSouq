package com.jumbosouq.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.SearchListAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    ImageView iv, footer_categories_img, btnSearch;
    DrawerLayout dl;
    RelativeLayout rl_Logout, btnAboutus, btnContactus, btnTandC, btnFAQ, btnCancel, btnRefund, btnNewsletter;
    LinearLayout btnPrivacypolicy;
    EditText etSearch;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApi;
    String token, username, customertoken;
    private static final String TAG = "Myapp";
    RecyclerView rv_search;
    ArrayList<SearchModel> searchModelArrayList;
    private SearchListAdapter searchListAdapter;
    String searchkey, firstname, latname;
    String searchUrl;
    JSONObject jsonBody;
    String itemid, sku, name, price, product_type, quote_id;
    TextView tvLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        customertoken = sharedPreferences.getString("customertoken", "");
        firstname = sharedPreferences.getString("firstname", "");
        latname = sharedPreferences.getString("lastname","");

        Log.d(TAG, "token-->" + token);

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        rv_search = findViewById(R.id.rv_search);
        iv = findViewById(R.id.iv);
        dl = findViewById(R.id.dl);
        rl_Logout = findViewById(R.id.rl_Logout);
        tvLogout = findViewById(R.id.tvLogout);

        btnAboutus = findViewById(R.id.btnAboutus);
        btnContactus = findViewById(R.id.btnContactus);
        btnTandC = findViewById(R.id.btnTandC);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnCancel = findViewById(R.id.btnCancel);
        btnRefund = findViewById(R.id.btnRefund);
        btnNewsletter = findViewById(R.id.btnNewsletter);
        btnPrivacypolicy = findViewById(R.id.btnPrivacypolicy);


        if (username == null) {
            username = "Hello, Guest";
            tvLogout.setText("Login");
        } else {
            username = "Hello, " + sharedPreferences.getString("username", "");
            tvLogout.setText("Logout");

        }

        mGoogleApi = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        onClick();


    }

    public void onClick(){

        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/about-us/");
                startActivity(intent);
            }
        });

        btnContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/contact/");
                startActivity(intent);

            }
        });

        btnTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/terms-conditions/");
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/faq/index/");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/cancellation-returns/");
                startActivity(intent);
            }
        });

        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
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


                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/privacy-policy/");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                // builder.setCancelable(false);
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        logout(); //commit


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


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchkey = etSearch.getText().toString();
                if (searchkey.length() == 0) {

                    Toast.makeText(getApplicationContext(), "Please enter earch key word!", Toast.LENGTH_SHORT).show();

                } else {

                    search();
                }
            }
        });


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    btnSearch.performClick();
                    return true;
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter earch key word!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        });

    }


    public void newsletter(){


        final EditText etEmail;
        LinearLayout btnUnsubscribe, btnSubscribe;
        final Dialog dialog = new Dialog(SearchActivity.this);
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

                            Toast.makeText(SearchActivity.this, "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(SearchActivity.this).add(jsonRequest);

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

                            Toast.makeText(SearchActivity.this, "Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(SearchActivity.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });



    }





    public void search() {

        rv_search.setVisibility(View.VISIBLE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            String searchinput = searchkey.replace(" ", "%");

            String urlParams = "searchCriteria[filter_groups][0][filters][0][field]=name&searchCriteria[filter_groups][0][filters][0][value]=%"
                    +searchinput+"%&searchCriteria[filter_groups][0][filters][0][condition_type]=like&searchCriteria[pageSize]=100";
            searchUrl = Allurl.Searh+urlParams;
            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("ResponseSearch-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                searchModelArrayList = new ArrayList<>();
                                JSONArray items_data = result.getJSONArray("items");
                                for (int i = 0; i < items_data.length(); i++) {

                                    JSONObject responseobj = items_data.getJSONObject(i);


                                    SearchModel searchModel = new SearchModel();
                                    searchModel.setSkuid(responseobj.getString("sku"));
                                    searchModel.setStatus(responseobj.getString("status"));
                                    searchModel.setProductname(responseobj.getString("name"));
                                    searchModel.setPrice(responseobj.getString("price"));
                                    searchModelArrayList.add(searchModel);

                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                    for (int j = 0; j < gallery_data.length(); j++) {
                                        JSONObject galleryobj = gallery_data.getJSONObject(j);
                                        searchModel.setImagefile(galleryobj.getString("file"));

                                    }
                                    searchListAdapter = new SearchListAdapter(SearchActivity.this, searchModelArrayList);
                                    rv_search.setAdapter(searchListAdapter);
                                    rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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
//                            Toast.makeText(Products.this, error.toString(), Toast.LENGTH_SHORT).show();
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




    public void updateCart(SearchModel searchModel) {


        if (customertoken.length() > 0) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.CreateCart,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Responsevalue-->", String.valueOf(response));
                            String cartid = String.valueOf(response);
                            String skuid = searchModel.getSkuid();
//                            String quantity = searchModel.getQuantity();

                            try {
                                jsonBody = new JSONObject("{\"cartItem\":{\"sku\":\""+skuid+"\",\"qty\":\""+searchModel.getQuantity()+"\",\"quote_id\":\""+cartid+"\"}}");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Addtocart, jsonBody, response1 -> {

                                Log.i("Response-->", String.valueOf(response1));

                                try {
                                    JSONObject result = new JSONObject(String.valueOf(response1));
                                    itemid = result.getString("item_id");
                                    sku = result.getString("sku");
                                    String qty = result.getString("qty");
                                    name = result.getString("name");
                                    price = result.getString("price");
                                    product_type = result.getString("product_type");
                                    quote_id = result.getString("quote_id");
//                                    tvQuantity.setText(qty+" Item");
//                                    String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
//                                    tvtotalAmount.setText("QAR "+totalamount);


                                    Toast.makeText(SearchActivity.this, name + "Successfully item added to cart", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                hideProgressDialog();

                                //TODO: handle success
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(SearchActivity.this,"Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Authorization", customertoken);
                                    return params;
                                }
                            };

                            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Toast.makeText(SearchActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", customertoken);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);


        } else {

            Toast.makeText(this, "Add item inside cart Login first", Toast.LENGTH_SHORT).show();

        }


    }


    public void Editcart(SearchModel searchModel){

        showProgressDialog();

        try {
            jsonBody = new JSONObject("{\"cartItem\":{\"item_id\":\""+itemid+"\",\"sku\":\""+sku+"\",\"qty\":\""+searchModel.getQuantity()+"\",\"name\":\""+name+"\",\"price\":\""+price+"\",\"product_type\":\""+product_type+"\",\"quote_id\":\""+quote_id+"\"}}");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, Allurl.EditCart+itemid, jsonBody, response1 -> {

            Log.i("Response-->", String.valueOf(response1));

            try {
                JSONObject result = new JSONObject(String.valueOf(response1));
                itemid = result.getString("item_id");
                sku = result.getString("sku");
                String qty = result.getString("qty");
                name = result.getString("name");
                price = result.getString("price");
                product_type = result.getString("product_type");
                quote_id = result.getString("quote_id");
//                tvQuantity.setText(qty+" Item");
//                String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
//                tvtotalAmount.setText("QAR "+totalamount);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressDialog();

            //TODO: handle success
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this,"Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", customertoken);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);



    }


    public void removeToCart(SearchModel searchModel) {


        showProgressDialog();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, Allurl.DeleteCart+itemid, null, response1 -> {

            Log.i("Response-->", String.valueOf(response1));

            hideProgressDialog();

            //TODO: handle success
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", customertoken);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);



    }










    @Override
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


        Toast.makeText(SearchActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(SearchActivity.this, GuestLoginActivity.class));
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