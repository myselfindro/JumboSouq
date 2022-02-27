package com.jumbosouq.com;

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
import com.jumbosouq.com.Modelclass.MobileModel;
import com.jumbosouq.com.Modelclass.RecentlyaddModel;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.Modelclass.WeeklyModel;
import com.jumbosouq.com.activities.CartActivity;
import com.jumbosouq.com.activities.Category;
import com.jumbosouq.com.activities.GuestLoginActivity;
import com.jumbosouq.com.activities.MyAccount;
import com.jumbosouq.com.activities.WebviewActivity;
import com.jumbosouq.com.adapterclass.MainSearchAdapter;
import com.jumbosouq.com.adapterclass.MobileAdapter;
import com.jumbosouq.com.adapterclass.NewarrivalsAdapter;
import com.jumbosouq.com.adapterclass.RecentlyaddAdapter;
import com.jumbosouq.com.adapterclass.WeeklyDealsRecyclerAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView iv, btnSearch, Searhback, footer_home_img, cart;
    DrawerLayout dl;
    RelativeLayout rl_Logout, btnAboutus, btnContactus, btnTandC, btnFAQ, btnCancel, btnRefund, btnNewsletter;
    LinearLayout btnPrivacypolicy;
    SessionManager sessionManager;
    EditText etSearch;
    private GoogleApiClient mGoogleApi;
    String token, weeklydeaksUrl, newArrivalsUrl, MobilesUrl, SpeakerUrl, HeadsetUrl, RecentlyadUrl, username, searchkey, searchUrl, customertoken,
            firstname, latname;
    private static final String TAG = "Myapp";
    ArrayList<WeeklyModel> weeklyModelArrayList;
    ArrayList<MobileModel> mobileModelArrayList;
    ArrayList<RecentlyaddModel> recentlyaddModelArrayList;
    ArrayList<SearchModel> searchModelArrayList;
    private WeeklyDealsRecyclerAdapter weeklyDealsRecyclerAdapter;
    private MobileAdapter mobileAdapter;
    private NewarrivalsAdapter newarrivalsAdapter;
    private RecentlyaddAdapter recentlyaddAdapter;
    private MainSearchAdapter mainSearchAdapter;
    TextView btnPhone, btnSpeaker, btnHeadset, user_profilename, footer_home_txt, tvLogout;
    View phone_underline, speakers_underline, headset_underline;
    RecyclerView rv_phone, rv_speaker, rv_headset, recently_added_rv, rv_bestseller, rv_newarrivals, rv_weeklydeals, rv_search;
    RelativeLayout rl_main, rl_search;
    LinearLayout footer_categories_ll, footer_cart_ll, footer_myAccount_ll;
    JSONObject jsonBody;
    String itemid, sku, name, price, product_type, quote_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        customertoken = sharedPreferences.getString("customertoken", "");
        firstname = sharedPreferences.getString("firstname", "");
        latname = sharedPreferences.getString("lastname", "");
        Log.d(TAG, "token-->" + token);

        iv = findViewById(R.id.iv);
        dl = (DrawerLayout) findViewById(R.id.dl);
        rl_Logout = findViewById(R.id.rl_Logout);
        rv_weeklydeals = findViewById(R.id.rv_weeklydeals);
        rv_newarrivals = findViewById(R.id.rv_newarrivals);
        btnPhone = findViewById(R.id.btnPhone);
        btnSpeaker = findViewById(R.id.btnSpeaker);
        btnHeadset = findViewById(R.id.btnHeadset);
        phone_underline = findViewById(R.id.phone_underline);
        speakers_underline = findViewById(R.id.speakers_underline);
        headset_underline = findViewById(R.id.headset_underline);
        rv_phone = findViewById(R.id.rv_phone);
        rv_speaker = findViewById(R.id.rv_speaker);
        rv_headset = findViewById(R.id.rv_headset);
        rv_search = findViewById(R.id.rv_search);
        recently_added_rv = findViewById(R.id.recently_added_rv);
        user_profilename = findViewById(R.id.user_profilename);
        user_profilename.setText(username);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        Searhback = findViewById(R.id.Searhback);
        rl_main = findViewById(R.id.rl_main);
        rl_search = findViewById(R.id.rl_search);
        footer_home_img = findViewById(R.id.footer_home_img);
        footer_categories_ll = findViewById(R.id.footer_categories_ll);
        footer_cart_ll = findViewById(R.id.footer_cart_ll);
        footer_home_txt = findViewById(R.id.footer_home_txt);
        footer_myAccount_ll = findViewById(R.id.footer_myAccount_ll);
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


        footer_home_img.setColorFilter(getApplication().getResources().getColor(R.color.secondary_color_red));


        mGoogleApi = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        if (username == null) {
            username = "Hello, Guest";
        } else {
            username = "Hello, " + sharedPreferences.getString("username", "");

        }

        if (username.equals("Hello, Guest")) {

            tvLogout.setText("Login");

        } else {

            tvLogout.setText("Logout");

        }


        Onclick();
        weeklyDeals();
        phoneList();

//        bestSeller();


        setBottomNavigation();

    }

    private void setBottomNavigation() {
        footer_home_txt.setTextColor(getApplication().getResources().getColor(R.color.secondary_color_red));
    }

    public void Onclick() {

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/about-us/");
                startActivity(intent);
            }
        });

        btnContactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/contact/");
                startActivity(intent);

            }
        });

        btnTandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/terms-conditions/");
                startActivity(intent);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/faq/index/");
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/cancellation-returns/");
                startActivity(intent);
            }
        });

        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
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


                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", "https://www.jumbosouq.com/privacy-policy/");
                startActivity(intent);
            }
        });

        footer_categories_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Category.class);
                startActivity(intent);
            }
        });


        footer_cart_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });
        footer_myAccount_ll.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, MyAccount.class);
            startActivity(intent);

        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(Gravity.LEFT);
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneList();

            }
        });

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                speakerList();

            }
        });

        btnHeadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                headsetList();

            }
        });


        rl_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter earch key word!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        });

        Searhback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(getIntent());
            }
        });
    }


    public void newsletter() {


        final EditText etEmail;
        LinearLayout btnUnsubscribe, btnSubscribe;
        final Dialog dialog = new Dialog(MainActivity.this);
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
                            jsonBody = new JSONObject("{\"customer\":{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + latname + "\",\"email\":\"" + username + "\",\"website_id\":1,\"extension_attributes\":{\"is_subscribed\":true}}}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Subscribe, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            Toast.makeText(MainActivity.this, "Successfully Subscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(MainActivity.this).add(jsonRequest);

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
                            jsonBody = new JSONObject("{\"customer\":{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + latname + "\",\"email\":\"" + username + "\",\"website_id\":1,\"extension_attributes\":{\"is_subscribed\":false}}}");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Allurl.Subscribe, params, response -> {

                            Log.d("Response4-->", String.valueOf(response));

                            Toast.makeText(MainActivity.this, "Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            dialog.cancel();


                            //TODO: handle success
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                hideProgressDialog();
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(MainActivity.this).add(jsonRequest);

                    } else {
                        Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }


    public void search() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            rl_main.setVisibility(View.GONE);
            rl_search.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            String searchinput = searchkey.replace(" ", "%");


            String urlParams = "searchCriteria[filter_groups][0][filters][0][field]=name&searchCriteria[filter_groups][0][filters][0][value]=%" + searchinput + "%&searchCriteria[filter_groups][0][filters][0][condition_type]=like&searchCriteria[pageSize]=100";
            searchUrl = Allurl.Searh + urlParams;
            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("ResponseSearch-->", String.valueOf(response));

                            //Parse Here

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

                                    JSONArray attributes_data = responseobj.getJSONArray("custom_attributes");
                                    for (int j = 0; j < attributes_data.length(); j++) {

                                        JSONObject attributeobj = attributes_data.getJSONObject(j);
                                        if (attributeobj.getString("attribute_code").equals("type")) {
                                            searchModel.setSubcategory(attributeobj.getString("value"));
                                        }
                                    }

                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                    for (int j = 0; j < gallery_data.length(); j++) {
                                        JSONObject galleryobj = gallery_data.getJSONObject(j);
                                        searchModel.setImagefile(galleryobj.getString("file"));


                                    }

                                    searchModelArrayList.add(searchModel);
                                    mainSearchAdapter = new MainSearchAdapter(MainActivity.this, searchModelArrayList);
                                    rv_search.setAdapter(mainSearchAdapter);
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
//                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void weeklyDeals() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            String urlParams = "id=494&name=Sale&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=417&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=7";
            weeklydeaksUrl = Allurl.Weeklydeals + urlParams;

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, weeklydeaksUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            //Parse Here

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                weeklyModelArrayList = new ArrayList<>();
                                JSONArray items_data = result.getJSONArray("items");
                                for (int i = 0; i < items_data.length(); i++) {
                                    JSONObject responseobj = items_data.getJSONObject(i);
                                    WeeklyModel weeklyModel = new WeeklyModel();

                                    weeklyModel.setSkuid(responseobj.getString("sku"));

                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                    for (int j = 0; j < gallery_data.length(); j++) {
                                        JSONObject galleryobj = gallery_data.getJSONObject(j);
                                        weeklyModel.setImagefile(galleryobj.getString("file"));

                                    }
                                    weeklyModelArrayList.add(weeklyModel);


                                    weeklyDealsRecyclerAdapter = new WeeklyDealsRecyclerAdapter(getApplicationContext(), weeklyModelArrayList);
                                    rv_weeklydeals.setAdapter(weeklyDealsRecyclerAdapter);
                                    rv_weeklydeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                    newArrivals();
                                    recentlyAdded();

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
                    params.put("Authorization", token);
                    return params;
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } else {

            Toast.makeText(getApplicationContext(), "OOPS! No Internet Connection", Toast.LENGTH_SHORT).show();

        }


    }


    public void newArrivals() {


        String urlParams = "id=417&name=New Arrivals&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=417&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=7";
        newArrivalsUrl = Allurl.Weeklydeals + urlParams;

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, newArrivalsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response3-->", String.valueOf(response));

                        //Parse Here

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            weeklyModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            for (int i = 0; i < items_data.length(); i++) {

                                JSONObject responseobj = items_data.getJSONObject(i);

                                WeeklyModel weeklyModel = new WeeklyModel();
                                weeklyModel.setSkuid(responseobj.getString("sku"));

                                JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                for (int j = 0; j < gallery_data.length(); j++) {
                                    JSONObject galleryobj = gallery_data.getJSONObject(j);
                                    weeklyModel.setImagefile(galleryobj.getString("file"));

                                }


                                weeklyModelArrayList.add(weeklyModel);


                                newarrivalsAdapter = new NewarrivalsAdapter(getApplicationContext(), weeklyModelArrayList);
                                rv_newarrivals.setAdapter(newarrivalsAdapter);
                                rv_newarrivals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void phoneList() {

        rv_phone.setVisibility(View.VISIBLE);
        rv_headset.setVisibility(View.GONE);
        rv_speaker.setVisibility(View.GONE);
        phone_underline.setVisibility(View.VISIBLE);
        speakers_underline.setVisibility(View.INVISIBLE);
        headset_underline.setVisibility(View.INVISIBLE);

        String UrlParams = "id=335&name=Smartphones&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=335&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=7";
        MobilesUrl = Allurl.Weeklydeals + UrlParams;

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MobilesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response3-->", String.valueOf(response));

                        //Parse Here

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            mobileModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            for (int i = 0; i < items_data.length(); i++) {

                                JSONObject responseobj = items_data.getJSONObject(i);
                                MobileModel mobileModel = new MobileModel();
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
                                rv_phone.setAdapter(mobileAdapter);
                                rv_phone.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void speakerList() {


        rv_phone.setVisibility(View.GONE);
        rv_headset.setVisibility(View.GONE);
        rv_speaker.setVisibility(View.VISIBLE);
        phone_underline.setVisibility(View.INVISIBLE);
        speakers_underline.setVisibility(View.VISIBLE);
        headset_underline.setVisibility(View.INVISIBLE);

        String UrlParams = "id=270&name=Home Audio&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=270&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=3";
        SpeakerUrl = Allurl.Weeklydeals + UrlParams;

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SpeakerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response3-->", String.valueOf(response));

                        //Parse Here

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            mobileModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            for (int i = 0; i < items_data.length(); i++) {

                                JSONObject responseobj = items_data.getJSONObject(i);
                                MobileModel mobileModel = new MobileModel();
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
                                rv_speaker.setAdapter(mobileAdapter);
                                rv_speaker.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void headsetList() {

        rv_phone.setVisibility(View.GONE);
        rv_headset.setVisibility(View.VISIBLE);
        rv_speaker.setVisibility(View.GONE);
        phone_underline.setVisibility(View.INVISIBLE);
        speakers_underline.setVisibility(View.INVISIBLE);
        headset_underline.setVisibility(View.VISIBLE);


        String UrlParams = "id=123&name=Headphones %26 Headsets&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=123&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=3";
        HeadsetUrl = Allurl.Weeklydeals + UrlParams;

        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, HeadsetUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response3-->", String.valueOf(response));

                        //Parse Here

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            mobileModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            for (int i = 0; i < items_data.length(); i++) {

                                JSONObject responseobj = items_data.getJSONObject(i);
                                MobileModel mobileModel = new MobileModel();
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
                                rv_headset.setAdapter(mobileAdapter);
                                rv_headset.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

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
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


    public void recentlyAdded() {

        String UrlParams = "searchCriteria[sortOrders][0][field]=updated_at&searchCriteria[pageSize]=3";
        RecentlyadUrl = Allurl.Weeklydeals + UrlParams;
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RecentlyadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Response6-->", String.valueOf(response));

                        //Parse Here

                        try {
                            JSONObject result = new JSONObject(String.valueOf(response));
                            recentlyaddModelArrayList = new ArrayList<>();
                            JSONArray items_data = result.getJSONArray("items");
                            for (int i = 0; i < items_data.length(); i++) {

                                JSONObject responseobj = items_data.getJSONObject(i);
                                RecentlyaddModel recentlyaddModel = new RecentlyaddModel();
                                recentlyaddModel.setProductname(responseobj.getString("name"));
                                recentlyaddModel.setSkuid(responseobj.getString("sku"));

                                JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                for (int j = 0; j < gallery_data.length(); j++) {
                                    JSONObject galleryobj = gallery_data.getJSONObject(j);
                                    recentlyaddModel.setImagefile(galleryobj.getString("file"));

                                }


                                recentlyaddModelArrayList.add(recentlyaddModel);


                                recentlyaddAdapter = new RecentlyaddAdapter(getApplicationContext(), recentlyaddModelArrayList);
                                recently_added_rv.setAdapter(recentlyaddAdapter);
                                recently_added_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                                jsonBody = new JSONObject("{\"cartItem\":{\"sku\":\"" + skuid + "\",\"qty\":\"" + searchModel.getQuantity() + "\",\"quote_id\":\"" + cartid + "\"}}");
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


                                    Toast.makeText(MainActivity.this, name + "Successfully item added to cart", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                hideProgressDialog();

                                //TODO: handle success
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(this, "Add item inside cart Login first", Toast.LENGTH_SHORT).show();

        }


    }


    public void Editcart(SearchModel searchModel) {

        showProgressDialog();

        try {
            jsonBody = new JSONObject("{\"cartItem\":{\"item_id\":\"" + itemid + "\",\"sku\":\"" + sku + "\",\"qty\":\"" + searchModel.getQuantity() + "\",\"name\":\"" + name + "\",\"price\":\"" + price + "\",\"product_type\":\"" + product_type + "\",\"quote_id\":\"" + quote_id + "\"}}");

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
                Toast.makeText(MainActivity.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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


//    public void bestSeller(){
//
//        String UrlParams="id=335&name=Smartphones&searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]=335&searchCriteria[filter_groups][0][filters][0][condition_type]=eq&searchCriteria[pageSize]=7";
//        MobilesUrl = Allurl.Weeklydeals+UrlParams;
//
//        showProgressDialog();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, MobilesUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.i("Response3-->", String.valueOf(response));
//
//                        //Parse Here
//
//                        try {
//                            JSONObject result = new JSONObject(String.valueOf(response));
//                            mobileModelArrayList = new ArrayList<>();
//                            JSONArray items_data = result.getJSONArray("items");
//                            for (int i = 0; i < items_data.length(); i++) {
//
//                                MobileModel mobileModel = new MobileModel();
//                                JSONObject responseobj = items_data.getJSONObject(i);
//
//                                JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
//
//                                if (gallery_data.length()>1) {
//
//                                    JSONObject galleryobj = gallery_data.getJSONObject(0);
//                                    mobileModel.setModelname(responseobj.getString("name"));
//                                    mobileModel.setPrice(responseobj.getString("price"));
//                                    mobileModel.setImagefile(galleryobj.getString("file"));
//                                    mobileModelArrayList.add(mobileModel);
//
//                                }
//
//                                mobileAdapter = new MobileAdapter(getApplicationContext(), mobileModelArrayList);
//                                rv_bestseller.setAdapter(mobileAdapter);
//                                rv_bestseller.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        hideProgressDialog();
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        hideProgressDialog();
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", token);
//                return params;
//            }
//
//        };
//
//        Volley.newRequestQueue(this).add(stringRequest);
//
//
//
//
//
//    }


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


        Toast.makeText(MainActivity.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
        sessionManager.logoutUser();
        LoginManager.getInstance().logOut();
        SharedPreferences settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        signOut();
        startActivity(new Intent(MainActivity.this, GuestLoginActivity.class));
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


    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Exit this app continue?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}