package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AccountResponse {
    @SerializedName("bankID")
    @Expose
    private String bankID;
    @SerializedName("mainBankID")
    @Expose
    private String mainBankID;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bankBranch")
    @Expose
    private String bankBranch;
    @SerializedName("bankAddress")
    @Expose
    private String bankAddress;
    @SerializedName("accountant_name")
    @Expose
    private String accountantName;
    @SerializedName("notes")
    @Expose
    private Object notes;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("accountType")
    @Expose
    private String accountType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("routing_no")
    @Expose
    private String routingNo;
    @SerializedName("contact_no")
    @Expose
    private Object contactNo;
    @SerializedName("keyperson_name")
    @Expose
    private String keypersonName;
    @SerializedName("keyperson_contact")
    @Expose
    private String keypersonContact;
}
