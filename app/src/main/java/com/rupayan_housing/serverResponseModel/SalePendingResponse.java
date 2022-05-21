package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class SalePendingResponse {
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
    private List<SalePendingList> lists = null;

}
