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
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Produtsdetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MobileModel> mobileModelArrayList;
    Context ctx;

    public MobileAdapter(Context ctx, ArrayList<MobileModel> mobileModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.mobileModelArrayList = mobileModelArrayList;
        this.ctx = ctx;

    }

    @NotNull
    @Override
    public MobileAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_phone, parent, false);
        MobileAdapter.MyViewHolder holder = new MobileAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MobileAdapter.MyViewHolder holder, int position) {

        holder.tvPrice.setText("Special Price:QAR " + mobileModelArrayList.get(position).getPrice());
        String description = "";
        if (mobileModelArrayList.get(position).getModelname().length() >= 33) {

            description = mobileModelArrayList.get(position).getModelname().substring(0, 30) + "...";
        } else {

            description = mobileModelArrayList.get(position).getModelname();

        }

        holder.tvDescription.setText(description);
        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" +
                        mobileModelArrayList.get(position).getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.Img);

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", mobileModelArrayList.get(position).getSkuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mobileModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Img;
        TextView tvDescription, tvPrice;
        LinearLayout btnDetails;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Img = itemView.findViewById(R.id.Img);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
