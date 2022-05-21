package com.rupayan_housing.serverResponseModel.view_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EmployeeData {
    @SerializedName("fullTimeMale")
    @Expose
    private String fullTimeMale;
    @SerializedName("fullTimeFemale")
    @Expose
    private String fullTimeFemale;
    @SerializedName("partTimeMale")
    @Expose
    private String partTimeMale;
    @SerializedName("partTimeFemail")
    @Expose
    private String partTimeFemail;
    @SerializedName("totalTechMale")
    @Expose
    private String totalTechMale;
    @SerializedName("totalTechFemale")
    @Expose
    private String totalTechFemale;
    @SerializedName("totalEmployeeMale")
    @Expose
    private Integer totalEmployeeMale;
    @SerializedName("totalEmployeeFemale")
    @Expose
    private Integer totalEmployeeFemale;

    public String getFullTimeMale() {
        return fullTimeMale;
    }

    public void setFullTimeMale(String fullTimeMale) {
        this.fullTimeMale = fullTimeMale;
    }

    public String getFullTimeFemale() {
        return fullTimeFemale;
    }

    public void setFullTimeFemale(String fullTimeFemale) {
        this.fullTimeFemale = fullTimeFemale;
    }

    public String getPartTimeMale() {
        return partTimeMale;
    }

    public void setPartTimeMale(String partTimeMale) {
        this.partTimeMale = partTimeMale;
    }

    public String getPartTimeFemail() {
        return partTimeFemail;
    }

    public void setPartTimeFemail(String partTimeFemail) {
        this.partTimeFemail = partTimeFemail;
    }

    public String getTotalTechMale() {
        return totalTechMale;
    }

    public void setTotalTechMale(String totalTechMale) {
        this.totalTechMale = totalTechMale;
    }

    public String getTotalTechFemale() {
        return totalTechFemale;
    }

    public void setTotalTechFemale(String totalTechFemale) {
        this.totalTechFemale = totalTechFemale;
    }

    public Integer getTotalEmployeeMale() {
        return totalEmployeeMale;
    }

    public void setTotalEmployeeMale(Integer totalEmployeeMale) {
        this.totalEmployeeMale = totalEmployeeMale;
    }

    public Integer getTotalEmployeeFemale() {
        return totalEmployeeFemale;
    }

    public void setTotalEmployeeFemale(Integer totalEmployeeFemale) {
        this.totalEmployeeFemale = totalEmployeeFemale;
    }
}
