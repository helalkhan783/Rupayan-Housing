package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CustomerResponse {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("customer_lname")
    @Expose
    private Object customerLname;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("thana")
    @Expose
    private String thana;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("country")
    @Expose
    private String country;
}
