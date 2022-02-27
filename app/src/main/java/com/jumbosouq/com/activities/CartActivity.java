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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.jumbosouq.com.Modelclass.CartModel;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.Modelclass.WeeklyModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.CartAdapter;
import com.jumbosouq.com.adapterclass.WeeklyDealsRecyclerAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;
import com.jumbosouq.com.utils.CustomProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ImageView footer_cart_img, iv;
    DrawerLayout dl;
    RelativeLayout rl_Logout, btnAboutus, btnContactus, btnTandC, btnFAQ, btnCancel, btnRefund, btnNewsletter;
    LinearLayout btnPrivacypolicy;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApi;
    String token, username, customertoken, firstname, latname;
    private static final String TAG = "Myapp";
    RelativeLayout rl_main, rl_search;
    TextView user_profilename,footer_Cart_text, tvLogout, tvtotalAmount, tvGrandtotal, tvSubtotal;
    LinearLayout footer_Home_ll, footer_categories_ll,footer_myAccount_ll;
    ArrayList<CartModel> cartModelArrayList = new ArrayList<>();;
    CartAdapter cartAdapter;
    RecyclerView rvCart;
    JSONObject jsonBody;
    String itemid, sku, name, price, product_type, quote_id;
    LinearLayout ll_emptyCart, ll_proceedCheckout;
    ScrollView scrollVw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
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
        footer_Home_ll = findViewById(R.id.footer_Home_ll);
        footer_categories_ll = findViewById(R.id.footer_categories_ll);
        footer_myAccount_ll = findViewById(R.id.footer_myAccount_ll);
        footer_cart_img = findViewById(R.id.footer_cart_img);
        footer_Cart_text = findViewById(R.id.footer_Cart_text);
        rvCart = findViewById(R.id.rvCart);
        ll_emptyCart = findViewById(R.id.ll_emptyCart);
        scrollVw = findViewById(R.id.scrollVw);
        ll_proceedCheckout = findViewById(R.id.ll_proceedCheckout);
        tvLogout = findViewById(R.id.tvLogout);
        tvtotalAmount = findViewById(R.id.tvtotalAmount);
        tvGrandtotal = findViewById(R.id.tvGrandtotal);
        tvSubtotal = findViewById(R.id.tvSubtotal);


        btnAboutus = findViewById(R.id.btnAboutus);
        btnContactus = findViewById(R.id.btnContactus);
        btnTandC = findViewById(R.id.btnTandC);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnCancel = findViewById(R.id.btnCancel);
        btnRefund = findViewById(R.id.btnRefund);
        btnNewsletter = findViewById(R.id.btnNewsletter);
        btnPrivacypolicy = findViewById(R.id.btnPrivacypolicy);

        footer_cart_img.setColorFilter(getApplication().getResources().getColor(R.color.secondary_color_red));

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
        setBottomNavigation();
        cartList();


    }
    private void setBottomNavigation() {
        footer_Cart_text.setTextColor(getApplication().getResources().getColor(R.color.secondary_color_red));
    }
    public void Onclick(){


        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/about-us/");
                startActivity(intent);
            }
        });

        btnContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/contact/");
                startActivity(intent);

            }
        });

        btnTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/terms-conditions/");
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/faq/index/");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/cancellation-returns/");
                startActivity(intent);
            }
        });

        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
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


                Intent intent = new Intent(CartActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/privacy-policy/");
                startActivity(intent);
            }
        });

        footer_Home_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        footer_myAccount_ll.setOnClickListener(v -> {

            Intent intent = new Intent(CartActivity.this, MyAccount.class);
            startActivity(intent);

        });
        footer_categories_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, Category.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
        final Dialog dialog = new Dialog(CartActivity.this);
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

                            Toast.makeText(CartActivity.this, "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(CartActivity.this).add(jsonRequest);

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

                            Toast.makeText(CartActivity.this, "Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(CartActivity.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });



    }



    public void cartList(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Allurl.CartList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            //Parse Here

                            try {
                                cartModelArrayList.clear();
                                JSONArray items_data = new JSONArray(String.valueOf(response));
                                int totalvalue = 0;
                                for (int i = 0; i < items_data.length(); i++) {
                                    JSONObject responseobj = items_data.getJSONObject(i);
                                    CartModel cartModel = new CartModel();

                                    cartModel.setItem_id(responseobj.getString("item_id"));
                                    cartModel.setSku(responseobj.getString("sku"));
                                    cartModel.setQty(responseobj.getInt("qty"));
                                    cartModel.setName(responseobj.getString("name"));
                                    cartModel.setPrice(responseobj.getString("price"));
                                    cartModel.setProduct_type(responseobj.getString("product_type"));
                                    cartModel.setQuote_id(responseobj.getString("quote_id"));
                                    totalvalue = totalvalue+ (responseobj.getInt("price")*responseobj.getInt("qty"));
                                    String amaountgrand = String.valueOf(totalvalue);

                                    tvSubtotal.setText("QAR "+amaountgrand);
                                    tvtotalAmount.setText("QAR "+amaountgrand);
                                    tvGrandtotal.setText("QAR "+amaountgrand);
//                                    String total = String.valueOf(cartModel.setPrice(responseobj.getString("price")) * Integer.parseInt(price));

                                    cartModelArrayList.add(cartModel);
                                }

                                Log.d(TAG, "Total-->"+totalvalue);

                                rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                cartAdapter = new CartAdapter(CartActivity.this, cartModelArrayList);
                                rvCart.setAdapter(cartAdapter);
                                cartAdapter.notifyDataSetChanged();

                                if (items_data.length()>0){

                                    ll_emptyCart.setVisibility(View.GONE);
                                    ll_proceedCheckout.setVisibility(View.VISIBLE);
                                    scrollVw.setVisibility(View.VISIBLE);

                                }else {

                                    ll_emptyCart.setVisibility(View.VISIBLE);
                                    scrollVw.setVisibility(View.GONE);
                                    ll_proceedCheckout.setVisibility(View.GONE);

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
//                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }




    public void Editcart(CartModel cartModel){

        showProgressDialog();

        try {
            jsonBody = new JSONObject("{\"cartItem\":{\"item_id\":\""+cartModel.getItem_id()+"\",\"sku\":\""+cartModel.getSku()+"\",\"qty\":\""+cartModel.getQty()+"\",\"name\":\""+cartModel.getName()+"\",\"price\":\""+cartModel.getPrice()+"\",\"product_type\":\""+cartModel.getProduct_type()+"\",\"quote_id\":\""+cartModel.getQuote_id()+"\"}}");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, Allurl.EditCart+cartModel.getItem_id(), jsonBody, response1 -> {

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
                cartList();
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
                Toast.makeText(CartActivity.this,"Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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


    public void removeToCart(CartModel cartModel ,int pos) {


        showProgressDialog();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, Allurl.DeleteCart+cartModel.getItem_id(), null, response1 -> {

            Log.i("Response-->", String.valueOf(response1));

            cartModelArrayList.remove(pos);
            cartAdapter.notifyDataSetChanged();
            cartList();
            hideProgressDialog();

            //TODO: handle success
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(CartActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                cartModelArrayList.remove(pos);
                cartAdapter.notifyDataSetChanged();
                cartList();
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


        Toast.makeText(CartActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(CartActivity.this, GuestLoginActivity.class));
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
