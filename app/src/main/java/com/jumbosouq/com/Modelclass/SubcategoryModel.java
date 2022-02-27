package com.jumbosouq.com.Modelclass;

import java.util.List;

public class SubcategoryModel {

    private String subcategoryname, id;
    private List<SupersubcategoryModel> supersubcategoryModelList;

    public List<SupersubcategoryModel> getSupersubcategoryModelList() {
        return supersubcategoryModelList;
    }

    public void setSupersubcategoryModelList(List<SupersubcategoryModel> supersubcategoryModelList) {
        this.supersubcategoryModelList = supersubcategoryModelList;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
