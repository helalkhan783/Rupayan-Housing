package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class UserPermissions {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("enterprises")
    @Expose
    private List<Enterprise> enterprises = null;
    @SerializedName("permisssion_lists")
    @Expose
    private List<PermisssionList> permisssionLists = null;
    @SerializedName("user_permissions")
    @Expose
    private List<String> userPermissions = null;
    @SerializedName("user_enterprise_access")
    @Expose
    private UserEnterpriseAccess userEnterpriseAccess;
}
