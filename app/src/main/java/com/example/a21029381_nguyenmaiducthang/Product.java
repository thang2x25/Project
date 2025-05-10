package com.example.a21029381_nguyenmaiducthang;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("ProductID")
    private int id;

    @SerializedName("ProductName")
    private String name;

    @SerializedName("ProductPrice")
    private int price;

    @SerializedName("ProductDescription")
    private String description;

    @SerializedName("ProductImage")
    private int image;

    public Product()
    {

    }
    public Product(String name, int price, String description, int image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
