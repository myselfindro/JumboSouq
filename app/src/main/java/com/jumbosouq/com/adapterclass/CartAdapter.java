package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumbosouq.com.Modelclass.CartModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.CartActivity;
import com.jumbosouq.com.activities.Products;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CartModel> cartModelArrayList;
    Context ctx;


    public CartAdapter(Context ctx, ArrayList<CartModel> cartModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.cartModelArrayList = cartModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_cart, parent, false);
        CartAdapter.MyViewHolder holder = new CartAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.MyViewHolder holder, int position) {
        CartModel databean = cartModelArrayList.get(position);
        String productname = "";
        if (databean.getName().length() >= 33) {
            productname = databean.getName().substring(0, 30) + "...";
        } else {
            productname = databean.getName();
        }
        holder.tvProdutname.setText(productname);
        holder.tvPrice.setText("QAR "+databean.getPrice());
        holder.tvQuantity.setText("" +databean.getQty());
        holder.btnItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CartActivity) ctx).removeToCart(databean,holder.getAdapterPosition());


            }
        });

        holder.ivDecrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int val = databean.getQty() - 1;
                if (val > 0) {
                    holder.tvQuantity.setText("" + val);
                    databean.setQty(val);
                    ((CartActivity) ctx).Editcart(databean);
                }else{
                    val=0;
                    databean.setQty(0);
                    notifyItemChanged(holder.getAdapterPosition());
                    ((CartActivity) ctx).removeToCart(databean,holder.getAdapterPosition());
                }



            }
        });


        holder.ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int  val = databean.getQty() + 1;
                if (val > 0) {
                    holder.tvQuantity.setText("" + val);
                    databean.setQty(val);
                    ((CartActivity) ctx).Editcart(databean);

                }else{
                    val=0;
                    databean.setQty(0);
                    notifyItemChanged(holder.getAdapterPosition());
                    ((CartActivity) ctx).removeToCart(databean,holder.getAdapterPosition());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ImgProduct;
        TextView tvProdutname, tvPrice, tvQuantity;
        ImageView ivDecrese;
        ImageView ivIncrease;
        LinearLayout btnItemDelete;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ImgProduct = itemView.findViewById(R.id.ImgProduct);
            tvProdutname = itemView.findViewById(R.id.tvProdutname);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivDecrese = itemView.findViewById(R.id.ivDecrese);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            btnItemDelete = itemView.findViewById(R.id.btnItemDelete);


        }
    }
}
