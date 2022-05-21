package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class HomePageResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("slider_lists")
    @Expose
    private List<SliderList> sliderLists = null;
    @SerializedName("raw_salt_buy")
    @Expose
    private Double rawSaltBuy;
    @SerializedName("edible_salt_sale")
    @Expose
    private Double edibleSaltSale;
    @SerializedName("industrial_salt_sale")
    @Expose
    private Double industrialSaltSale;
    @SerializedName("total_sale")
    @Expose
    private Double totalSale;
    @SerializedName("enterprise_info")
    @Expose
    private EnterpriseInfo enterpriseInfo;
    @SerializedName("store_added")
    @Expose
    private Double storeAdded;
    @SerializedName("store_approved")
    @Expose
    private Double storeApproved;
    @SerializedName("mill_id")
    @Expose
    private String mill_id;
    @SerializedName("licence_expire")
    @Expose
    private List<LicenceExpire> licenceExpire = null;
    @SerializedName("user_info")
    @Expose
    private DashBoardUserInfo userInfo;

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

    public List<SliderList> getSliderLists() {
        return sliderLists;
    }

    public void setSliderLists(List<SliderList> sliderLists) {
        this.sliderLists = sliderLists;
    }

    public Double getRawSaltBuy() {
        return rawSaltBuy;
    }

    public void setRawSaltBuy(Double rawSaltBuy) {
        this.rawSaltBuy = rawSaltBuy;
    }

    public Double getEdibleSaltSale() {
        return edibleSaltSale;
    }

    public void setEdibleSaltSale(Double edibleSaltSale) {
        this.edibleSaltSale = edibleSaltSale;
    }

    public Double getIndustrialSaltSale() {
        return industrialSaltSale;
    }

    public void setIndustrialSaltSale(Double industrialSaltSale) {
        this.industrialSaltSale = industrialSaltSale;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public EnterpriseInfo getEnterpriseInfo() {
        return enterpriseInfo;
    }

    public void setEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        this.enterpriseInfo = enterpriseInfo;
    }

    public Double getStoreAdded() {
        return storeAdded;
    }

    public void setStoreAdded(Double storeAdded) {
        this.storeAdded = storeAdded;
    }

    public Double getStoreApproved() {
        return storeApproved;
    }

    public void setStoreApproved(Double storeApproved) {
        this.storeApproved = storeApproved;
    }

    public String getMill_id() {
        return mill_id;
    }

    public void setMill_id(String mill_id) {
        this.mill_id = mill_id;
    }

    public List<LicenceExpire> getLicenceExpire() {
        return licenceExpire;
    }

    public void setLicenceExpire(List<LicenceExpire> licenceExpire) {
        this.licenceExpire = licenceExpire;
    }

    public DashBoardUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(DashBoardUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
