package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QcQaHistoryResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("enterprize_list")
    @Expose
    public  List<Enterprize> enterprizeList = null;
    @SerializedName("lists")
    @Expose
    public List<QcqaList> lists = null;

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

    public List<Enterprize> getEnterprizeList() {
        return enterprizeList;
    }

    public void setEnterprizeList(List<Enterprize> enterprizeList) {
        this.enterprizeList = enterprizeList;
    }

    public List<QcqaList> getLists() {
        return lists;
    }

    public void setLists(List<QcqaList> lists) {
        this.lists = lists;
    }
}
