package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jumbosouq.com.Modelclass.WeeklyModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Produtsdetails;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewarrivalsAdapter extends RecyclerView.Adapter<NewarrivalsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<WeeklyModel> weeklyAdapterArrayList;
    Context ctx;

    public NewarrivalsAdapter(Context ctx, ArrayList<WeeklyModel> weeklyAdapterArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.weeklyAdapterArrayList = weeklyAdapterArrayList;
        this.ctx = ctx;

    }


    @NotNull
    @Override
    public NewarrivalsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_newarrivals, parent, false);
        NewarrivalsAdapter.MyViewHolder holder = new NewarrivalsAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        Glide.with(ctx)
                .load("https://www.jumbosouq.com/pub/media/catalog/product" +
                        weeklyAdapterArrayList.get(position).getImagefile())
                .placeholder(R.drawable.loading)
                .into(holder.rvImage);

        holder.btn_weeklydetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Produtsdetails.class);
                intent.putExtra("skuId", weeklyAdapterArrayList.get(position).getSkuid());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return (weeklyAdapterArrayList == null) ? 0 : weeklyAdapterArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView rvImage;
        RelativeLayout btn_weeklydetails;
        TextView btnBuy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rvImage = itemView.findViewById(R.id.rvImage);
            btn_weeklydetails = itemView.findViewById(R.id.btn_weeklydetails);
//            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }

}
