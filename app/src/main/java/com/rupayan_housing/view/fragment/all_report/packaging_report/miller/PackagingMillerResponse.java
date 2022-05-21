package com.rupayan_housing.view.fragment.all_report.packaging_report.miller;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.view.fragment.all_report.packaging_report.packaging_page_response.PackagingMiller;

import java.util.List;

import lombok.Data;

@Data
public class PackagingMillerResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("miller_list")
    @Expose
    private List<PackagingMiller> millerList = null;
}
