package com.rupayan_housing.view.fragment.all_report.employee_report.page_data_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EmployeeReportAsociationList {
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("zoneID")
    @Expose
    private String zoneID;
}
