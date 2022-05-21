package com.rupayan_housing.view.fragment.all_report.reconcilation_report.list_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReconciliationReportProfuctList {

    @SerializedName("categoryID")
    @Expose
    private String categoryID;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("entry_date")
    @Expose
    private String entryDate;
    @SerializedName("brand_name")
    @Expose
    private Object brandName;
    @SerializedName("transfer_type")
    @Expose
    private String transferType;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("brandID")
    @Expose
    private String brandID;
    @SerializedName("product_dimensions")
    @Expose
    private String productDimensions;
    @SerializedName("product_isbn")
    @Expose
    private Object productIsbn;
    @SerializedName("reconciliation_type")
    @Expose
    private String reconciliationType;
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("buying_price")
    @Expose
    private String buyingPrice;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("enterprize_name")
    @Expose
    private String enterprizeName;
    @SerializedName("customer_fname")
    @Expose
    private Object customerFname;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Object getBrandName() {
        return brandName;
    }

    public void setBrandName(Object brandName) {
        this.brandName = brandName;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getProductDimensions() {
        return productDimensions;
    }

    public void setProductDimensions(String productDimensions) {
        this.productDimensions = productDimensions;
    }

    public Object getProductIsbn() {
        return productIsbn;
    }

    public void setProductIsbn(Object productIsbn) {
        this.productIsbn = productIsbn;
    }

    public String getReconciliationType() {
        return reconciliationType;
    }

    public void setReconciliationType(String reconciliationType) {
        this.reconciliationType = reconciliationType;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEnterprizeName() {
        return enterprizeName;
    }

    public void setEnterprizeName(String enterprizeName) {
        this.enterprizeName = enterprizeName;
    }

    public Object getCustomerFname() {
        return customerFname;
    }

    public void setCustomerFname(Object customerFname) {
        this.customerFname = customerFname;
    }
}
