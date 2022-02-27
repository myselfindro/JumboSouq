package com.jumbosouq.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jumbosouq.com.Allurl.Allurl;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.adapterclass.ProductListAdapter;
import com.jumbosouq.com.internet.CheckConnectivity;
import com.jumbosouq.com.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Products extends AppCompatActivity {

    ImageView btnBack, btnSearch, ImgProduct, btnShare, cart;
    TextView tvName, tvProdutname, tvShortdesciption, tvPrice, tvQuantity, tvProductcategory, tvQuantity2, txt_sort, tvtotalAmount;
    EditText etSearch;
    SmartMaterialSpinner spSort;
    RecyclerView rv_productcat, rv_search;
    String id, token, productName, searchUrl, searchkey, productname, productlistUrl, customertoken;
    SessionManager sessionManager;
    private static final String TAG = "Myapp";
    ArrayList<SearchModel> searchModelArrayList;
    ArrayList<String> supersubcategoryModelArrayList;
    List<String> sortlist;
    private ProductListAdapter productListAdapter;
    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout ll_cartItem, ll_layer, btnCancelsheet, btnAddtocart;
    ImageView ivDecrese, ivIncrease, btnClose;
    int count;
    String selectedsort = "", itemid, sku, name, price, product_type, quote_id;
    String cartid = "";
    JSONObject jsonBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        productName = intent.getStringExtra("name");
        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        customertoken = sharedPreferences.getString("customertoken", "");
        Log.d(TAG, "token-->" + token);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        tvName = findViewById(R.id.tvName);
        etSearch = findViewById(R.id.etSearch);
        rv_productcat = findViewById(R.id.rv_productcat);
        rv_search = findViewById(R.id.rv_search);
        ImgProduct = findViewById(R.id.ImgProduct);
        tvProdutname = findViewById(R.id.tvProdutname);
        tvShortdesciption = findViewById(R.id.tvShortdesciption);
        tvPrice = findViewById(R.id.tvPrice);
        ll_cartItem = findViewById(R.id.ll_cartItem);
        ivDecrese = findViewById(R.id.ivDecrese);
        ivIncrease = findViewById(R.id.ivIncrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvQuantity2 = findViewById(R.id.tvQuantity2);
        tvProductcategory = findViewById(R.id.tvProductcategory);
        ll_layer = findViewById(R.id.ll_layer);
        btnClose = findViewById(R.id.btnClose);
        btnShare = findViewById(R.id.btnShare);
        spSort = findViewById(R.id.spSort);
        txt_sort = findViewById(R.id.txt_sort);
        cart = findViewById(R.id.cart);
        tvtotalAmount = findViewById(R.id.tvtotalAmount);
        btnCancelsheet = findViewById(R.id.btnCancelsheet);
        btnAddtocart = findViewById(R.id.btnAddtocart);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);


        onClick();
        productlist();
        spSort();

    }


    public void onClick() {

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Products.this, CartActivity.class);
                startActivity(intent);
            }
        });

        txt_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Products.this, Category.class);
                startActivity(intent);
            }
        });

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedsort = sortlist.get(position);
                productlist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = productname + "\n" + "https://www.jumbosouq.com/";
                String shareSub = "";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
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


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_layer.setVisibility(View.GONE);
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        btnCancelsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_layer.setVisibility(View.GONE);
                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btnAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Products.this, CartActivity.class);
                startActivity(intent);
            }
        });


//        ivIncrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                count++;
//                if (count > 0) {
//                    tvQuantity2.setText("" + count);
//                } else {
//                    count = 0;
//                    tvQuantity2.setText("" + 0);
////                    ll_layer.setVisibility(View.GONE);
////                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//        });
//
//
//        ivDecrese.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                count--;
//                if (count > 0) {
//                    tvQuantity2.setText("" + count);
//                } else {
//                    count = 0;
//                    tvQuantity2.setText("" + 0);
////                    ll_layer.setVisibility(View.GONE);
////                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//
//
//            }
//        });


    }


    public void productlist() {

        if (selectedsort.length() > 0) {
            productlistUrl = "https://www.jumbosouq.com/rest/default/V1/products?searchCriteria[filterGroups][0][filters][0][field]=category_id&searchCriteria[filterGroups][0][filters][0][value]=" + id + "&searchCriteria[filterGroups][0][filters][0][conditionType]=eq&searchCriteria[sortOrders][0][field]=" + selectedsort + "&searchCriteria[sortOrders][0][direction]=ASC";

        } else {
            productlistUrl = "https://www.jumbosouq.com/rest/default/V1/products?searchCriteria[filter_groups][0][filters][0][field]=category_id&searchCriteria[filter_groups][0][filters][0][value]="
                    + id + "&searchCriteria[filter_groups][0][filters][0][condition_type]=eq";
        }


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productlistUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("ResponseList-->", String.valueOf(response));

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
                                    for (int k = 0; k < gallery_data.length(); k++) {
                                        JSONObject galleryobj = gallery_data.getJSONObject(k);
                                        searchModel.setImagefile(galleryobj.getString("file"));
                                    }


                                    searchModelArrayList.add(searchModel);
                                    tvName.setText(productName);

                                    productListAdapter = new ProductListAdapter(Products.this, searchModelArrayList);
                                    rv_productcat.setAdapter(productListAdapter);
                                    rv_productcat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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


    public void search() {

        rv_productcat.setVisibility(View.GONE);
        rv_search.setVisibility(View.VISIBLE);

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            String searchinput = searchkey.replace(" ", "%");


            String urlParams = "searchCriteria[filter_groups][0][filters][0][field]=name&searchCriteria[filter_groups][0][filters][0][value]=%"
                    + searchinput + "%&searchCriteria[filter_groups][0][filters][0][condition_type]=like&searchCriteria[pageSize]=100";
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
                                    searchModel.setProductname(responseobj.getString("name"));
                                    searchModel.setPrice(responseobj.getString("price"));
                                    searchModelArrayList.add(searchModel);

                                    JSONArray gallery_data = responseobj.getJSONArray("media_gallery_entries");
                                    for (int j = 0; j < gallery_data.length(); j++) {
                                        JSONObject galleryobj = gallery_data.getJSONObject(j);
                                        searchModel.setImagefile(galleryobj.getString("file"));

                                    }


                                    productListAdapter = new ProductListAdapter(Products.this, searchModelArrayList);
                                    rv_search.setAdapter(productListAdapter);
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


    private void spSort() {

        sortlist = new ArrayList<String>();

        sortlist.add("Name");
        sortlist.add("Price");
        sortlist.add("Type");
        sortlist.add("Rating_Summary");
        sortlist.add("Recently_Added");
        sortlist.add("Relevance");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, sortlist);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spSort.setAdapter(arrayAdapter);
    }


    public void addToCartDetails(SearchModel searchModel) {

        String productdetailsUrl = Allurl.Productdetailsl + searchModel.getSkuid();

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, productdetailsUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response1-->", String.valueOf(response));

                            try {
                                JSONObject result = new JSONObject(String.valueOf(response));
                                productname = result.getString("name");
                                String price = result.getString("price");
                                String sku = result.getString("sku");
                                tvProdutname.setText(productname);
                                tvPrice.setText("QAR " + price);
//                                tvSku.setText("SKU " + sku);
//                                tvtotalAmount.setText("QAR " + price);
                                JSONArray attributes_data = result.getJSONArray("custom_attributes");
                                for (int i = 0; i < attributes_data.length(); i++) {

                                    JSONObject responseobj = attributes_data.getJSONObject(i);
                                    if (responseobj.getString("attribute_code").equals("description")) {
                                        String description = responseobj.getString("value");
                                        tvShortdesciption.setText(Html.fromHtml(description));
                                    }
                                    if (responseobj.getString("attribute_code").equals("short_description")) {
                                        String shortdescription = responseobj.getString("value");
//                                        tvShortdesciption.setText(Html.fromHtml(shortdescription));
                                    }
                                    if (responseobj.getString("attribute_code").equals("type")) {
                                        String producategory = responseobj.getString("value");
                                        tvProductcategory.setText(producategory);
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

        if (mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
            mBottomSheetBehavior1.setDraggable(false);
            ll_layer.setVisibility(View.VISIBLE);
        } else {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }




        ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                if (count > 0) {
                    tvQuantity2.setText("" + count);

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
                                                tvQuantity.setText(qty + " Item");
                                                String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                                                tvtotalAmount.setText("QAR " + totalamount);
                                                searchModel.setQuantity(1);
                                                searchModel.setNeedtocartshow(false);
                                                productListAdapter.notifyDataSetChanged();

                                                Toast.makeText(Products.this, name + "Successfully item added to cart", Toast.LENGTH_SHORT).show();


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            hideProgressDialog();

                                            //TODO: handle success
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                searchModel.setNeedtocartshow(true);
                                                productListAdapter.notifyDataSetChanged();
                                                Toast.makeText(Products.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Products.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", customertoken);
                                return params;
                            }

                        };

                        Volley.newRequestQueue(Products.this).add(stringRequest);


                    } else {
                        Toast.makeText(Products.this, "Add item inside cart Login first", Toast.LENGTH_SHORT).show();
                    }

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
                            tvQuantity.setText(qty + " Item");
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
                            Toast.makeText(Products.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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
                } else {
                    count = 0;
                    tvQuantity2.setText("" + 0);
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
            }
        });


        ivDecrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count--;
                if (count > 0) {
                    tvQuantity2.setText("" + count);
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
                            tvQuantity.setText(qty + " Item");
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
                            Toast.makeText(Products.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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



                } else {
                    count = 0;
                    tvQuantity2.setText("" + 0);

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


            }
        });

    }




    public void addToCartDetailsShow(SearchModel searchModel) {

        ll_cartItem.setVisibility(View.VISIBLE);


    }


    public void addToCartDetailsGone(SearchModel searchModel) {

        ll_cartItem.setVisibility(View.GONE);

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
                                    tvQuantity.setText(qty + " Item");
                                    String totalamount = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(price));
                                    tvtotalAmount.setText("QAR " + totalamount);
                                    searchModel.setQuantity(1);
                                    searchModel.setNeedtocartshow(false);
                                    productListAdapter.notifyDataSetChanged();

                                    Toast.makeText(Products.this, name + "Successfully item added to cart", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                hideProgressDialog();

                                //TODO: handle success
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    searchModel.setNeedtocartshow(true);
                                    productListAdapter.notifyDataSetChanged();
                                    Toast.makeText(Products.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Products.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                tvQuantity.setText(qty + " Item");
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
                Toast.makeText(Products.this, "Product that you are trying to add is not available.", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Products.this, Category.class);
        startActivity(intent);

    }
}