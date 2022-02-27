package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Products;
import com.jumbosouq.com.activities.Produtsdetails;
import com.jumbosouq.com.session.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<SearchModel> searchModelArrayList;
    Context ctx;
    SessionManager sessionManager;
    String username;

    public ProductListAdapter(Context ctx, ArrayList<SearchModel> searchModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.searchModelArrayList = searchModelArrayList;
        this.ctx = ctx;

    }

    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_productlist, parent, false);
        ProductListAdapter.MyViewHolder holder = new ProductListAdapter.MyViewHolder(view);
        sessionManager = new SessionManager(ctx);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DATA", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductListAdapter.MyViewHolder holder, int position) {
        SearchModel databean = searchModelArrayList.get(holder.getAdapterPosition());

        String productname = "";
        if (databean.getProductname().length() >= 33) {
            productname = databean.getProductname().substring(0, 30) + "...";
        } else {
            productname = databean.getProductname();
        }


        holder.tvProductName.setText(productname);
        holder.tvPrice.setText("QAR " + databean.getPrice());
        holder.tvQuantity.setText("" + databean.getQuantity());
        holder.tvSubcategory.setText(databean.getSubcategory());
        if (databean.getQuantity() == 0) {
            holder.ll_counter.setVisibility(View.GONE);
            holder.btnAddtocart.setVisibility(View.VISIBLE);
//           ((Products) ctx).addToCartDetailsGone(searchModelArrayList.get(holder.getAdapterPosition()));

        } else {
            holder.ll_counter.setVisibility(View.VISIBLE);
            holder.btnAddtocart.setVisibility(View.GONE);
//            ((Products) ctx).addToCartDetailsShow(searchModelArrayList.get(holder.getAdapterPosition()));

        }

        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" + databean.getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.ImgProduct);

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", databean.getSkuid());
                intent.putExtra("status", databean.getStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });


        if (databean.isNeedtocartshow()){

            holder.btnAddtocart.setVisibility(View.VISIBLE);
            holder.ll_counter.setVisibility(View.GONE);

        }else {
            holder.btnAddtocart.setVisibility(View.GONE);
            holder.ll_counter.setVisibility(View.VISIBLE);

        }


        holder.btnAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (username.equals("Guest")) {
                    Toast.makeText(ctx, "Please Login for item add", Toast.LENGTH_SHORT).show();
                } else {

                    //                ((Products) ctx).addToCartDetails(searchModelArrayList.get(position));
//                    databean.setQuantity(1);
                    ((Products) ctx).updateCart(databean);
//                    holder.ll_counter.setVisibility(View.VISIBLE);
//                    holder.btnAddtocart.setVisibility(View.GONE);
                    ((Products) ctx).addToCartDetailsShow(searchModelArrayList.get(position));
                    ((Products) ctx).addToCartDetailsGone(searchModelArrayList.get(position));
//                    notifyItemChanged(position);
                }


            }
        });


        holder.layout_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((Products) ctx).addToCartDetails(searchModelArrayList.get(position));
            }
        });

        holder.ivDecrese.setOnClickListener(view -> {

            int val = databean.getQuantity() - 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                databean.setNeedtocartshow(false);
                databean.setQuantity(val);
                ((Products) ctx).Editcart(databean);
            } else {
                val = 0;
                databean.setQuantity(0);
                databean.setNeedtocartshow(true);
                notifyItemChanged(holder.getAdapterPosition());
                ((Products) ctx).removeToCart(databean);
            }


        });
        holder.ivIncrease.setOnClickListener(view -> {

            int val = databean.getQuantity() + 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                databean.setNeedtocartshow(false);
                databean.setQuantity(val);
                ((Products) ctx).Editcart(databean);

            } else {
                val = 0;
                databean.setQuantity(0);
                databean.setNeedtocartshow(true);
                notifyItemChanged(holder.getAdapterPosition());
                ((Products) ctx).removeToCart(databean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImgProduct, btnDetails;
        TextView tvProductName, tvPrice, tvQuantity, tvSubcategory;
        LinearLayout btnAddtocart;
        LinearLayout layout_rv, ll_counter;
        ImageView ivDecrese;
        ImageView ivIncrease;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ImgProduct = itemView.findViewById(R.id.ImgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddtocart = itemView.findViewById(R.id.btnAddtocart);
            layout_rv = itemView.findViewById(R.id.layout_rv);
            ll_counter = itemView.findViewById(R.id.ll_counter);
            ivDecrese = itemView.findViewById(R.id.ivDecrese);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubcategory = itemView.findViewById(R.id.tvSubcategory);
            btnDetails = itemView.findViewById(R.id.btnDetails);

        }
    }
}
