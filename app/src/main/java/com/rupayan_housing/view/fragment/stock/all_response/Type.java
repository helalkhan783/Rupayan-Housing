package com.rupayan_housing.view.fragment.stock.all_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Type {
    @SerializedName("Damage")
    @Expose
    private String damage;
    @SerializedName("Increase")
    @Expose
    private String increase;
    @SerializedName("Lost")
    @Expose
    private String lost;
    @SerializedName("Expire")
    @Expose
    private String expire;

}
