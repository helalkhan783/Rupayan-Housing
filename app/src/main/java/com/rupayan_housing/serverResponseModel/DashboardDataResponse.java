package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_mills")
    @Expose
    private String totalMills;
    @SerializedName("active_mills")
    @Expose
    private String activeMills;
    @SerializedName("inactive_mills")
    @Expose
    private Integer inactiveMills;
    @SerializedName("purchase_cm")
    @Expose
    private String purchaseCm;
    @SerializedName("production_cm")
    @Expose
    private String productionCm;
    @SerializedName("total_sale_cm")
    @Expose
    private String totalSaleCm;
    @SerializedName("iodine_purchase_cm")
    @Expose
    private String iodinePurchaseCm;
    @SerializedName("iodized_sale_cm")
    @Expose
    private String iodizedSaleCm;
    @SerializedName("total_zone")
    @Expose
    private Integer totalZone;
    @SerializedName("total_un_organization")
    @Expose
    private Integer totalUnOrganization;
    @SerializedName("go_agencies")
    @Expose
    private Integer goAgencies;
    @SerializedName("total_monitoring")
    @Expose
    private Integer totalMonitoring;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalMills() {
        return totalMills;
    }

    public void setTotalMills(String totalMills) {
        this.totalMills = totalMills;
    }

    public String getActiveMills() {
        return activeMills;
    }

    public void setActiveMills(String activeMills) {
        this.activeMills = activeMills;
    }

    public Integer getInactiveMills() {
        return inactiveMills;
    }

    public void setInactiveMills(Integer inactiveMills) {
        this.inactiveMills = inactiveMills;
    }

    public String getPurchaseCm() {
        return purchaseCm;
    }

    public void setPurchaseCm(String purchaseCm) {
        this.purchaseCm = purchaseCm;
    }

    public String getProductionCm() {
        return productionCm;
    }

    public void setProductionCm(String productionCm) {
        this.productionCm = productionCm;
    }

    public String getTotalSaleCm() {
        return totalSaleCm;
    }

    public void setTotalSaleCm(String totalSaleCm) {
        this.totalSaleCm = totalSaleCm;
    }

    public String getIodinePurchaseCm() {
        return iodinePurchaseCm;
    }

    public void setIodinePurchaseCm(String iodinePurchaseCm) {
        this.iodinePurchaseCm = iodinePurchaseCm;
    }

    public String getIodizedSaleCm() {
        return iodizedSaleCm;
    }

    public void setIodizedSaleCm(String iodizedSaleCm) {
        this.iodizedSaleCm = iodizedSaleCm;
    }

    public Integer getTotalZone() {
        return totalZone;
    }

    public void setTotalZone(Integer totalZone) {
        this.totalZone = totalZone;
    }

    public Integer getTotalUnOrganization() {
        return totalUnOrganization;
    }

    public void setTotalUnOrganization(Integer totalUnOrganization) {
        this.totalUnOrganization = totalUnOrganization;
    }

    public Integer getGoAgencies() {
        return goAgencies;
    }

    public void setGoAgencies(Integer goAgencies) {
        this.goAgencies = goAgencies;
    }

    public Integer getTotalMonitoring() {
        return totalMonitoring;
    }

    public void setTotalMonitoring(Integer totalMonitoring) {
        this.totalMonitoring = totalMonitoring;
    }
}
