package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Production {
    @SerializedName("industrial")
    @Expose
    public Double industrial;
    @SerializedName("iodized")
    @Expose
    public Double iodized;

    public Double getIndustrial() {
        return industrial;
    }

    public void setIndustrial(Double industrial) {
        this.industrial = industrial;
    }

    public Double getIodized() {
        return iodized;
    }

    public void setIodized(Double iodized) {
        this.iodized = iodized;
    }
}
