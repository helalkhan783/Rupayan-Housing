package com.rupayan_housing.serverResponseModel.order_sale_details;

import android.telecom.Call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Janina {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("details")
    @Expose
    private  OrderDetails details;
}
