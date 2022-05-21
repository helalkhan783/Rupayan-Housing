package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PurchaseReturnPendingList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("serialID")
    @Expose
    private String serialID;
    @SerializedName("order_serial")
    @Expose
    private String order_serial;
    @SerializedName("order_vendorID")
    @Expose
    private String order_vendorID;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("end_date")
    @Expose
    private Object endDate;
    @SerializedName("enterprise_name")
    @Expose
    private String enterpriseName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("Full_Name")
    @Expose
    private String fullName;
}
