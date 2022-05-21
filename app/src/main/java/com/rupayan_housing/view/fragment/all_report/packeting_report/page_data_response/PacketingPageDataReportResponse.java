package com.rupayan_housing.view.fragment.all_report.packeting_report.page_data_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PacketingPageDataReportResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("asociation_list")
    @Expose
    private List<PacketReportAssociationList> asociationList = null;
    @SerializedName("miller_list")
    @Expose
    private List<PacketReportMillerList> millerList = null;
    @SerializedName("referer")
    @Expose
    private List<PacketReportReferer> referer = null;
    @SerializedName("associationID")
    @Expose
    private String associationID;
    @SerializedName("production_type")
    @Expose
    private List<ProductionType> productionType = null;

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

    public List<PacketReportAssociationList> getAsociationList() {
        return asociationList;
    }

    public void setAsociationList(List<PacketReportAssociationList> asociationList) {
        this.asociationList = asociationList;
    }

    public List<PacketReportMillerList> getMillerList() {
        return millerList;
    }

    public void setMillerList(List<PacketReportMillerList> millerList) {
        this.millerList = millerList;
    }

    public List<PacketReportReferer> getReferer() {
        return referer;
    }

    public void setReferer(List<PacketReportReferer> referer) {
        this.referer = referer;
    }

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String associationID) {
        this.associationID = associationID;
    }

    public List<ProductionType> getProductionType() {
        return productionType;
    }

    public void setProductionType(List<ProductionType> productionType) {
        this.productionType = productionType;
    }
}
