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
import android.text.Html;
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
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.MainActivity;
import com.jumbosouq.com.Modelclass.MobileModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.MobileAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Produtsdetails extends AppCompatActivity {

    private static final String TAG = "TAG";
    ImageView btnBack, ImgProduct, cart;
    String skuid, productDetailsurl, token, username, sku1, sku2, sku3, sku4, sku5, status, customertoken, firstname, latname;
    TextView tvProdutname, tvShortdesciption, tvLongdescription,
            tvPrice, tvSku, user_profilename, tvtotalAmount, tvQuantity2, tvNotavailable, tvLogout;
    ImageView iv;
    DrawerLayout dl;
    RelativeLayout rl_Logout, btnAboutus, btnContactus, btnTandC, btnFAQ, btnCancel, btnRefund, btnNewsletter;
    LinearLayout btnPrivacypolicy;
    SessionManager sessionManager;
    private GoogleApiClient mGoogleApi;
    RecyclerView alsolike_rv;
    ArrayList<MobileModel> mobileModelArrayList;
    private MobileAdapter mobileAdapter;
    LinearLayout ll_alsolike, ll_counter, btnAddtocart, ll_cartdetails, btnCart;
    ImageView ivDecrese, ivIncrease, btnClose;
    int count = 0;
    RelativeLayout rl_quantity;
    String itemid, sku, name, price, product_type, quote_id;
    JSONObject jsonBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtsdetails);
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        customertoken = sharedPreferences.getString("customertoken", "");
        firstname = sharedPreferences.getString("firstname", "");
        latname = sharedPreferences.getString("lastname","");
        Log.d(TAG, "token-->" + token);
        Log.d(TAG, "token-->" + token);
        iv = findViewById(R.id.iv);
        dl = (DrawerLayout) findViewById(R.id.dl);
        rl_Logout = findViewById(R.id.rl_Logout);
        btnBack = findViewById(R.id.btnBack);
        ImgProduct = findViewById(R.id.ImgProduct);
        tvProdutname = findViewById(R.id.tvProdutname);
        tvShortdesciption = findViewById(R.id.tvShortdesciption);
        tvLongdescription = findViewById(R.id.tvLongdescription);
        tvSku = findViewById(R.id.tvSku);
        tvPrice = findViewById(R.id.tvPrice);
        alsolike_rv = findViewById(R.id.alsolike_rv);
        ll_alsolike = findViewById(R.id.ll_alsolike);
        ivDecrese = findViewById(R.id.ivDecrese);
        ivIncrease = findViewById(R.id.ivIncrease);
        tvQuantity2 = findViewById(R.id.tvQuantity2);
        ll_counter = findViewById(R.id.ll_counter);
        tvNotavailable = findViewById(R.id.tvNotavailable);
        btnAddtocart = findViewById(R.id.btnAddtocart);
        rl_quantity = findViewById(R.id.rl_quantity);
        btnCart = findViewById(R.id.btnCart);
        cart = findViewById(R.id.cart);

        btnAboutus = findViewById(R.id.btnAboutus);
        btnContactus = findViewById(R.id.btnContactus);
        btnTandC = findViewById(R.id.btnTandC);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnCancel = findViewById(R.id.btnCancel);
        btnRefund = findViewById(R.id.btnRefund);
        btnNewsletter = findViewById(R.id.btnNewsletter);
        btnPrivacypolicy = findViewById(R.id.btnPrivacypolicy);

        Intent intent = getIntent();
        skuid = intent.getStringExtra("skuId");
        status = intent.getStringExtra("status");
        productDetailsurl = Allurl.Productdetailsl + skuid;
        Log.d(TAG, "url-->" + productDetailsurl);
        user_profilename = findViewById(R.id.user_profilename);
        tvtotalAmount = findViewById(R.id.tvtotalAmount);
        ll_cartdetails = findViewById(R.id.ll_cartdetails);
        tvLogout = findViewById(R.id.tvLogout);
        user_profilename.setText(username);
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


        productDetails();
        OnClick();

    }


    public void OnClick() {

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, CartActivity.class);
                startActivity(intent);
            }
        });


        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/about-us/");
                startActivity(intent);
            }
        });

        btnContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/contact/");
                startActivity(intent);

            }
        });

        btnTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/terms-conditions/");
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/faq/index/");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/cancellation-returns/");
                startActivity(intent);
            }
        });

        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
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


                Intent intent = new Intent(Produtsdetails.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/privacy-policy/");
                startActivity(intent);
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Produtsdetails.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateCart();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });


        ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count + 1;
                if (count > 0) {
                    tvQuantity2.setText("" + count);
                    Editcart();
                } else {
                    count = 0;
                    tvQuantity2.setText("" + 0);
                    removeToCart();
                }
            }
        });

        ivDecrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count--;
                if (count > 0) {
                    tvQuantity2.setText("" + count);
                    Editcart();
                } else {
                    count = 0;
                    tvQuantity2.setText("" + 0);
                    removeToCart();
                    btnAddtocart.setVisibility(View.VISIBLE);
                    rl_quantity.setVisibility(View.GONE);
                    ll_cartdetails.setVisibility(View.GONE);
                }

            }
        });

        rl_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Produtsdetails.this);
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
        final Dialog dialog = new Dialog(Produtsdetails.this);
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

                            Toast.makeText(Produtsdetails.this, "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Produtsdetails.this).add(jsonRequest);

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

                            Toast.makeText(Produtsdetails.this, "Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Produtsdetails.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });



    }



    public void productDetails() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productDetailsurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                String productname = result.getString("name");
                                String price = result.getString("price");
                                String sku = result.getString("sku");
                                tvProdutname.setText(productname);
                                tvPrice.setText("QAR " + price);
                                tvSku.setText("SKU " + sku);
//                                tvtotalAmount.setText("QAR " + price);
                                JSONArray attributes_data = result.getJSONArray("custom_attributes");
                                for (int i = 0; i < attributes_data.length(); i++) {

                                    JSONObject responseobj = attributes_data.getJSONObject(i);
                                    if (responseobj.getString("attribute_code").equals("description")) {
                                        String description = responseobj.getString("value");
                                        tvLongdescription.setText(Html.fromHtml(description));
                                    }
                                    if (responseobj.getString("attribute_code").equals("short_description")) {
                                        String shortdescription = responseobj.getString("value");
                                        tvShortdesciption.setText(Html.fromHtml(shortdescription));
                                    }

                                }
                                JSONArray gallery_data = result.getJSONArray("media_gallery_entries");
                                for (int j = 0; j < gallery_data.length(); j++) {

                                    JSONObject galleryobj = gallery_data.getJSONObject(j);

                                    String imgfile = galleryobj.getString("file");
                                    Glide.with(getApplicationContext())
                                            .load("https://www.jumbosouq.com/pub/media/catalog/product" + imgfile)
                                            .placeholder(R.drawable.loading)
                                            .into(ImgProduct);


                                }

                                JSONArray product_links = result.getJSONArray("product_links");

                                JSONObject productobj = product_links.getJSONObject(0);
                                JSONObject productobj2 = product_links.getJSONObject(1);
                                JSONObject productobj3 = product_links.getJSONObject(2);
                                JSONObject productobj4 = product_links.getJSONObject(3);
                                JSONObject productobj5 = product_links.getJSONObject(4);

                                sku1 = productobj.getString("linked_product_sku");
                                sku2 = productobj2.getString("linked_product_sku");
                                sku3 = productobj3.getString("linked_product_sku");
                                sku4 = productobj4.getString("linked_product_sku");
                                sku5 = productobj5.getString("linked_product_sku");

                                Log.d(TAG, "sku--->" + sku1 + "," + sku2 + "," + sku3);

                                alsolike();


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
//                            Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void alsolike() {

        String alsolikeurl = "https://www.jumbosouq.com/rest/default/V1/products?searchCriteria[filter_groups][0][filters][0][field]=sku&searchCriteria[filter_groups][0][filters][0][value]="
                + sku1 + "," + sku2 + "," + sku3 + "," + sku4 + "," + sku5 + "&searchCriteria[filter_groups][0][filters][0][condition_type]=in";
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, alsolikeurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("alsolikeresponse-->", String.valueOf(response));

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            mobileModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            Log.d(TAG, "length-->" + items_data.length());
                            if (items_data.length() > 0) {
                                ll_alsolike.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < items_data.length(); i++) {

                                MobileModel mobileModel = new MobileModel();
                                JSONObject responseobj = items_data.getJSONObject(i);
                                mobileModel.setModelname(responseobj.getString("name"));
                                mobileModel.setPrice(responseobj.getString("price"));
                                mobileModel.setSkuid(responseobj.getString("sku"));

                                JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");

                                for (int j = 0; j < gallery_data.length(); j++) {
                                    JSONObject galleryobj = gallery_data.getJSONObject(j);
                                    mobileModel.setImagefile(galleryobj.getString("file"));
                                }

                                mobileModelArrayList.add(mobileModel);


                                mobileAdapter = new MobileAdapter(getApplicationContext(), mobileModelArrayList);
                                alsolike_rv.setAdapter(mobileAdapter);
                                alsolike_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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
//                        Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    }


    public void updateCart() {


        if (customertoken.length() > 0) {

            count = count + 1;
            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Allurl.CreateCart,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Responsevalue-->", String.valueOf(response));
                            String cartid = String.valueOf(response);
//                            String quantity = searchModel.getQuantity();

                            try {
                                jsonBody = new JSONObject("{\"cartItem\":{\"sku\":\"" + skuid + "\",\"qty\":\"" + count + "\",\"quote_id\":\"" + cartid + "\"}}");
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
                                    String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                                    tvtotalAmount.setText("QAR " + totalamount);


                                    btnAddtocart.setVisibility(View.GONE);
                                    rl_quantity.setVisibility(View.VISIBLE);
                                    ll_cartdetails.setVisibility(View.VISIBLE);
                                    tvQuantity2.setText("" + 1);

                                    Toast.makeText(Produtsdetails.this, name + "Successfully item added to cart", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                hideProgressDialog();


                                //TODO: handle success
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Produtsdetails.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    btnAddtocart.setVisibility(View.VISIBLE);
                                    rl_quantity.setVisibility(View.GONE);
                                    ll_cartdetails.setVisibility(View.GONE);
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
                            Toast.makeText(Produtsdetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void Editcart() {

        showProgressDialog();

        try {
            jsonBody = new JSONObject("{\"cartItem\":{\"item_id\":\"" + itemid + "\",\"sku\":\"" + sku + "\",\"qty\":\"" + count + "\",\"name\":\"" + name + "\",\"price\":\"" + price + "\",\"product_type\":\"" + product_type + "\",\"quote_id\":\"" + quote_id + "\"}}");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, Allurl.EditCart + itemid, jsonBody, response1 -> {

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
                String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                tvtotalAmount.setText("QAR " + totalamount);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            hideProgressDialog();

            //TODO: handle success
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count = count - 1;
                tvQuantity2.setText("" + count);
                Toast.makeText(Produtsdetails.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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


    public void removeToCart() {


        showProgressDialog();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, Allurl.DeleteCart + itemid, null, response1 -> {

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


        Toast.makeText(Produtsdetails.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(Produtsdetails.this, GuestLoginActivity.class));
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