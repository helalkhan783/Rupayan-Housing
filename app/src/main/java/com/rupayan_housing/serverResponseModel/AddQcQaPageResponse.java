package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class AddQcQaPageResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("test_list")
    @Expose
    private List<TestList> testList = null;
    @SerializedName("enterprize_list")
    @Expose
    private List<EnterprizeList> enterprizeList = null;
    @SerializedName("qcID")
    @Expose
    private Integer qcID;

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

    public List<TestList> getTestList() {
        return testList;
    }

    public void setTestList(List<TestList> testList) {
        this.testList = testList;
    }

    public List<EnterprizeList> getEnterprizeList() {
        return enterprizeList;
    }

    public void setEnterprizeList(List<EnterprizeList> enterprizeList) {
        this.enterprizeList = enterprizeList;
    }

    public Integer getQcID() {
        return qcID;
    }

    public void setQcID(Integer qcID) {
        this.qcID = qcID;
    }
}
