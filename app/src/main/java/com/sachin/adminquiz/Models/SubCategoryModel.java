package com.sachin.adminquiz.Models;

public class SubCategoryModel {

    private String categoryName,key;

    public SubCategoryModel() {
    }

    public SubCategoryModel(String categoryName){
        this.categoryName = categoryName;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}