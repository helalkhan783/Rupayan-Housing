package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class UserListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("designation_list")
    @Expose
    private  List<Designation> designationList = null;
    @SerializedName("department_list")
    @Expose
    private  List<Department> departmentList = null;
    @SerializedName("lists")
    @Expose
    private List<UserLists> lists = null;

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

    public List<Designation> getDesignationList() {
        return designationList;
    }

    public void setDesignationList(List<Designation> designationList) {
        this.designationList = designationList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public List<UserLists> getLists() {
        return lists;
    }

    public void setLists(List<UserLists> lists) {
        this.lists = lists;
    }
}
