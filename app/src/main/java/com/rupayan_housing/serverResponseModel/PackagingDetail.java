package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;


public class PackagingDetail {
    @SerializedName("packDetailID")
    @Expose
    private String packDetailID;
    @SerializedName("packagingID")
    @Expose
    private String packagingID;
    @SerializedName("originItemID")
    @Expose
    private String originItemID;
    @SerializedName("originItemQty")
    @Expose
    private String originItemQty;
    @SerializedName("originItemQtyUnit")
    @Expose
    private String originItemQtyUnit;
    @SerializedName("convertedItemID")
    @Expose
    private String convertedItemID;
    @SerializedName("convertedItemQty")
    @Expose
    private String convertedItemQty;
    @SerializedName("convertedItemQtyUnit")
    @Expose
    private String convertedItemQtyUnit;
    @SerializedName("packagingNote")
    @Expose
    private String packagingNote;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("origin_unit")
    @Expose
    private String originUnit;
    @SerializedName("con_unit")
    @Expose
    private String conUnit;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("entryDateTime")
    @Expose
    private String entryDateTime;
    @SerializedName("sold_from")
    @Expose
    private String soldFrom;

    public String getPackDetailID() {
        return packDetailID;
    }

    public void setPackDetailID(String packDetailID) {
        this.packDetailID = packDetailID;
    }

    public String getPackagingID() {
        return packagingID;
    }

    public void setPackagingID(String packagingID) {
        this.packagingID = packagingID;
    }

    public String getOriginItemID() {
        return originItemID;
    }

    public void setOriginItemID(String originItemID) {
        this.originItemID = originItemID;
    }

    public String getOriginItemQty() {
        return originItemQty;
    }

    public void setOriginItemQty(String originItemQty) {
        this.originItemQty = originItemQty;
    }

    public String getOriginItemQtyUnit() {
        return originItemQtyUnit;
    }

    public void setOriginItemQtyUnit(String originItemQtyUnit) {
        this.originItemQtyUnit = originItemQtyUnit;
    }

    public String getConvertedItemID() {
        return convertedItemID;
    }

    public void setConvertedItemID(String convertedItemID) {
        this.convertedItemID = convertedItemID;
    }

    public String getConvertedItemQty() {
        return convertedItemQty;
    }

    public void setConvertedItemQty(String convertedItemQty) {
        this.convertedItemQty = convertedItemQty;
    }

    public String getConvertedItemQtyUnit() {
        return convertedItemQtyUnit;
    }

    public void setConvertedItemQtyUnit(String convertedItemQtyUnit) {
        this.convertedItemQtyUnit = convertedItemQtyUnit;
    }

    public String getPackagingNote() {
        return packagingNote;
    }

    public void setPackagingNote(String packagingNote) {
        this.packagingNote = packagingNote;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getOriginUnit() {
        return originUnit;
    }

    public void setOriginUnit(String originUnit) {
        this.originUnit = originUnit;
    }

    public String getConUnit() {
        return conUnit;
    }

    public void setConUnit(String conUnit) {
        this.conUnit = conUnit;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getSoldFrom() {
        return soldFrom;
    }

    public void setSoldFrom(String soldFrom) {
        this.soldFrom = soldFrom;
    }
}
