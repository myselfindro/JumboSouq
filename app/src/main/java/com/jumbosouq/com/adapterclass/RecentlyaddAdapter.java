package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jumbosouq.com.Modelclass.MobileModel;
import com.jumbosouq.com.Modelclass.RecentlyaddModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Produtsdetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecentlyaddAdapter extends RecyclerView.Adapter<RecentlyaddAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<RecentlyaddModel> recentlyaddModelArrayList;
    Context ctx;

    public RecentlyaddAdapter(Context ctx, ArrayList<RecentlyaddModel> recentlyaddModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.recentlyaddModelArrayList = recentlyaddModelArrayList;
        this.ctx = ctx;

    }

    @NotNull
    @Override
    public RecentlyaddAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_recentlyadd, parent, false);
        RecentlyaddAdapter.MyViewHolder holder = new RecentlyaddAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentlyaddAdapter.MyViewHolder holder, int position) {

        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" +
                        recentlyaddModelArrayList.get(position).getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.Img);

        String description = "";
        if (recentlyaddModelArrayList.get(position).getProductname().length() >=33){

            description = recentlyaddModelArrayList.get(position).getProductname().substring(0, 30)+"...";
        }else {

            description = recentlyaddModelArrayList.get(position).getProductname();

        }

        holder.tvDescription.setText(description);
        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", recentlyaddModelArrayList.get(position).getSkuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentlyaddModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView tvDescription;
        LinearLayout btnDetails;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.Img);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
