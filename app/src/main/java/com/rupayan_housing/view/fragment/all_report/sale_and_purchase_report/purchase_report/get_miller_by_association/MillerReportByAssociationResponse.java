package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.get_miller_by_association;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MillerReportByAssociationResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("miller_list")
    @Expose
    private List<PurchaseMillerList> millerList = null;

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

    public List<PurchaseMillerList> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<PurchaseMillerList> millerList) {
        this.millerList = millerList;
    }
}
