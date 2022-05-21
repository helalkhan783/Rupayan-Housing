package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class MillerHistoryResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("process_type")
    @Expose
    private  List<ProcessType> processType = null;
    @SerializedName("mill_type")
    @Expose
    private  List<MillType> millType = null;
    @SerializedName("lists")
    @Expose
    private List<HistoryList> lists = null;

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

    public List<ProcessType> getProcessType() {
        return processType;
    }

    public void setProcessType(List<ProcessType> processType) {
        this.processType = processType;
    }

    public List<MillType> getMillType() {
        return millType;
    }

    public void setMillType(List<MillType> millType) {
        this.millType = millType;
    }

    public List<HistoryList> getLists() {
        return lists;
    }

    public void setLists(List<HistoryList> lists) {
        this.lists = lists;
    }
}
