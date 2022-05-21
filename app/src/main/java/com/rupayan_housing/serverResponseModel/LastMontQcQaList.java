package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastMontQcQaList {
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("total_mill")
    @Expose
    private Integer totalMill;
    @SerializedName("total_qc")
    @Expose
    private String totalQc;
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getTotalMill() {
        return totalMill;
    }

    public void setTotalMill(Integer totalMill) {
        this.totalMill = totalMill;
    }

    public String getTotalQc() {
        return totalQc;
    }

    public void setTotalQc(String totalQc) {
        this.totalQc = totalQc;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
