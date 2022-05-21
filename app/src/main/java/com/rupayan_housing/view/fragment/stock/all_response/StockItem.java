package com.rupayan_housing.view.fragment.stock.all_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class StockItem {
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("pcode")
    @Expose
    private String pcode;
    @SerializedName("product_isbn")
    @Expose
    private Object productIsbn;
    @SerializedName("product_dimensions")
    @Expose
    private String productDimensions;
    @SerializedName("product_image")
    @Expose
    private Object productImage;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("brand_name")
    @Expose
    private String brandName;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public Object getProductIsbn() {
        return productIsbn;
    }

    public void setProductIsbn(Object productIsbn) {
        this.productIsbn = productIsbn;
    }

    public String getProductDimensions() {
        return productDimensions;
    }

    public void setProductDimensions(String productDimensions) {
        this.productDimensions = productDimensions;
    }

    public Object getProductImage() {
        return productImage;
    }

    public void setProductImage(Object productImage) {
        this.productImage = productImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
