package com.jumbosouq.com.Modelclass;

import java.util.List;

public class CategoryModel {

    private String categoryName;
    private String categoryid;
    private List<SubcategoryModel> subcategoryName;

    public List<SubcategoryModel> getSubcategoryName() {
        return subcategoryName;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public void setSubcategoryName(List<SubcategoryModel> subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



}
