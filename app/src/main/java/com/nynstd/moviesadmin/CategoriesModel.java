package com.nynstd.moviesadmin;

public class CategoriesModel {

    String cat_Name;

    public CategoriesModel() {
    }

    public CategoriesModel(String cat_Name) {
        this.cat_Name = cat_Name;
    }

    public String getCat_Name() {
        return cat_Name;
    }

    public void setCat_Name(String cat_Name) {
        this.cat_Name = cat_Name;
    }
}
