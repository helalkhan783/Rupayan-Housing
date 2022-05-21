package com.rupayan_housing.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.serverResponseModel.Company;
import com.rupayan_housing.serverResponseModel.Enterprize;
import com.rupayan_housing.serverResponseModel.PurchaseReturnHistoryList;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseDeclineResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("enterprize_list")
    @Expose
    private List<Enterprize> enterprizeList = null;
    @SerializedName("company_list")
    @Expose
    private List<Company> companyList = null;
    @SerializedName("lists")
    @Expose
    private List<PurchaseDeclineList> lists = null;
}
