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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jumbosouq.com.Modelclass.CategoryModel;
import com.jumbosouq.com.R;
import com.jumbosouq.com.activities.Products;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CategoryModel> categoryModelArrayList;
    Context ctx;

    public CategoryAdapter(Context ctx, ArrayList<CategoryModel> categoryModelArrayList) {
        this.categoryModelArrayList = categoryModelArrayList;
        this.ctx = ctx;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.from(ctx).inflate(R.layout.rv_categorylist, parent, false);
        CategoryAdapter.MyViewHolder holder = new CategoryAdapter.MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryAdapter.MyViewHolder holder, int position) {

        CategoryModel mCategoryModel = categoryModelArrayList.get(position);
        holder.tvCategoryname.setText(mCategoryModel.getCategoryName());
        String catid = mCategoryModel.getCategoryid();

        holder.ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (categoryModelArrayList.get(position).getSubcategoryName() != null && categoryModelArrayList.get(position).getSubcategoryName().size() > 0) {

                    if (categoryModelArrayList.get(position).getSubcategoryName() != null && holder.rv_subcategory.getVisibility() != View.VISIBLE) {

                        holder.rv_subcategory.setVisibility(View.VISIBLE);
                        holder.noncollaspeIcon.setVisibility(View.GONE);
                        holder.collaspeIcon.setVisibility(View.VISIBLE);
                        holder.rv_subcategory.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
                        SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(ctx, mCategoryModel.getSubcategoryName());
                        holder.rv_subcategory.setAdapter(subcategoryAdapter);
                    } else {
                        holder.rv_subcategory.setVisibility(View.GONE);
                        holder.collaspeIcon.setVisibility(View.GONE);
                        holder.noncollaspeIcon.setVisibility(View.VISIBLE);
                    }

                } else {

                    Intent intent = new Intent(ctx, Products.class);
                    intent.putExtra("id", catid);
                    intent.putExtra("name", mCategoryModel.getCategoryName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryname;
        LinearLayout ll_category;
        RecyclerView rv_subcategory;
        ImageView noncollaspeIcon, collaspeIcon;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvCategoryname = itemView.findViewById(R.id.tvCategoryname);
            ll_category = itemView.findViewById(R.id.ll_category);
            rv_subcategory = itemView.findViewById(R.id.rv_subcategory);
            noncollaspeIcon = itemView.findViewById(R.id.noncollaspeIcon);
            collaspeIcon = itemView.findViewById(R.id.collaspeIcon);

        }
    }
}
