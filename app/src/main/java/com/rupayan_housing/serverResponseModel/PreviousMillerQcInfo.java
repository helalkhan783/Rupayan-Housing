package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PreviousMillerQcInfo {
    @SerializedName("slId")
    @Expose
    private String slId;
    @SerializedName("profileID")
    @Expose
    private String profileID;
    @SerializedName("haveLaboratory")
    @Expose
    private String haveLaboratory;
    @SerializedName("standardProcedure")
    @Expose
    private String standardProcedure;
    @SerializedName("trainedLaboratoryPerson")
    @Expose
    private String trainedLaboratoryPerson;
    @SerializedName("useTestKit")
    @Expose
    private String useTestKit;
    @SerializedName("laboratoryPerson")
    @Expose
    private String laboratoryPerson;
    @SerializedName("labRemarks")
    @Expose
    private String labRemarks;
    @SerializedName("status")
    @Expose
    private String status;

    public String getSlId() {
        return slId;
    }

    public void setSlId(String slId) {
        this.slId = slId;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getHaveLaboratory() {
        return haveLaboratory;
    }

    public void setHaveLaboratory(String haveLaboratory) {
        this.haveLaboratory = haveLaboratory;
    }

    public String getStandardProcedure() {
        return standardProcedure;
    }

    public void setStandardProcedure(String standardProcedure) {
        this.standardProcedure = standardProcedure;
    }

    public String getTrainedLaboratoryPerson() {
        return trainedLaboratoryPerson;
    }

    public void setTrainedLaboratoryPerson(String trainedLaboratoryPerson) {
        this.trainedLaboratoryPerson = trainedLaboratoryPerson;
    }

    public String getUseTestKit() {
        return useTestKit;
    }

    public void setUseTestKit(String useTestKit) {
        this.useTestKit = useTestKit;
    }

    public String getLaboratoryPerson() {
        return laboratoryPerson;
    }

    public void setLaboratoryPerson(String laboratoryPerson) {
        this.laboratoryPerson = laboratoryPerson;
    }

    public String getLabRemarks() {
        return labRemarks;
    }

    public void setLabRemarks(String labRemarks) {
        this.labRemarks = labRemarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
