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
import com.jumbosouq.com.activities.SearchActivity;
import com.jumbosouq.com.session.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder>  {

    private LayoutInflater inflater;
    private ArrayList<SearchModel> searchModelArrayList;
    Context ctx;
    SessionManager sessionManager;
    String username;

    public SearchListAdapter(Context ctx, ArrayList<SearchModel> searchModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.searchModelArrayList = searchModelArrayList;
        this.ctx = ctx;

    }

    @Override
    public SearchListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_search, parent, false);
        SearchListAdapter.MyViewHolder holder = new SearchListAdapter.MyViewHolder(view);
        sessionManager = new SessionManager(ctx);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("DATA", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchListAdapter.MyViewHolder holder, int position) {


        String productname = "";
        if (searchModelArrayList.get(position).getProductname().length() >= 33) {
            productname = searchModelArrayList.get(position).getProductname().substring(0, 30) + "...";
        } else {
            productname = searchModelArrayList.get(position).getProductname();
        }


        holder.tvProductName.setText(productname);
        holder.tvPrice.setText("QAR "+searchModelArrayList.get(position).getPrice());
        holder.tvQuantity.setText("" +searchModelArrayList.get(position).getQuantity());
        holder.tvSubcategory.setText(searchModelArrayList.get(position).getSubcategory());
        if(searchModelArrayList.get(position).getQuantity()==0){
            holder.ll_counter.setVisibility(View.GONE);
            holder.btnAddtocart.setVisibility(View.VISIBLE);

        }else{
            holder.ll_counter.setVisibility(View.VISIBLE);
            holder.btnAddtocart.setVisibility(View.GONE);

        }
        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" +
                        searchModelArrayList.get(position).getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.ImgProduct);

        holder.layout_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", searchModelArrayList.get(position).getSkuid());
                intent.putExtra("status", searchModelArrayList.get(position).getStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        holder.btnAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.equals("Guest")) {
                    Toast.makeText(ctx, "Please Login for item add", Toast.LENGTH_SHORT).show();
                } else {

//                ((Products) ctx).addToCartDetails(searchModelArrayList.get(position));
                    searchModelArrayList.get(position).setQuantity(1);
                    ((SearchActivity) ctx).updateCart(searchModelArrayList.get(position));
                    holder.ll_counter.setVisibility(View.VISIBLE);
                    holder.btnAddtocart.setVisibility(View.GONE);
                    notifyItemChanged(position);
                }

            }
        });

        holder.ivDecrese.setOnClickListener(view -> {

            int val = searchModelArrayList.get(position).getQuantity() - 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                searchModelArrayList.get(position).setQuantity(val);
                ((SearchActivity) ctx).Editcart(searchModelArrayList.get(position));
            }else{
                val=0;
                searchModelArrayList.get(position).setQuantity(0);
                notifyItemChanged(holder.getAdapterPosition());
                ((SearchActivity) ctx).removeToCart(searchModelArrayList.get(position));
            }

        });
        holder.ivIncrease.setOnClickListener(view -> {

            int val = searchModelArrayList.get(position).getQuantity() + 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                searchModelArrayList.get(position).setQuantity(val);
                ((SearchActivity) ctx).Editcart(searchModelArrayList.get(position));
            }else{
                val=0;
                searchModelArrayList.get(position).setQuantity(0);
                notifyItemChanged(holder.getAdapterPosition());
                ((SearchActivity) ctx).removeToCart(searchModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImgProduct;
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

        }
    }
}
