package com.jumbosouq.com.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumbosouq.com.Modelclass.CategoryModel;
import com.jumbosouq.com.Modelclass.SearchModel;
import com.jumbosouq.com.Modelclass.SubcategoryModel;
import com.jumbosouq.com.Modelclass.SupersubcategoryModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Products;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<SubcategoryModel> categoryModelArrayList;
    Context ctx;

    public SubcategoryAdapter(Context ctx, List<SubcategoryModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
        this.ctx = ctx;
    }


    @Override
    public SubcategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.from(ctx).inflate(R.layout.rv_subcategorylist, parent, false);
        SubcategoryAdapter.MyViewHolder holder = new SubcategoryAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubcategoryAdapter.MyViewHolder holder, int position) {


        holder.tvsubCategoryname.setText(categoryModelArrayList.get(position).getSubcategoryname());
        String catid = categoryModelArrayList.get(position).getId();

        holder.ll_subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(ctx, Products.class);
                    intent.putExtra("id", catid);
                    intent.putExtra("name",categoryModelArrayList.get(position).getSubcategoryname());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvsubCategoryname;
        LinearLayout ll_subcategory;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvsubCategoryname = itemView.findViewById(R.id.tvsubCategoryname);
            ll_subcategory = itemView.findViewById(R.id.ll_subcategory);

        }
    }
}
