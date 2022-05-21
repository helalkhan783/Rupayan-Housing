package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ProcessType {
    @SerializedName("processTypeID")
    @Expose
    private String processTypeID;
    @SerializedName("processTypeName")
    @Expose
    private String processTypeName;


    public String getProcessTypeID() {
        return processTypeID;
    }

    public void setProcessTypeID(String processTypeID) {
        this.processTypeID = processTypeID;
    }

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }
}
