package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EditUserDataResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("user_profile_infos")
    @Expose
    private UserProfileInfos userProfileInfos;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UserProfileInfos getUserProfileInfos() {
        return userProfileInfos;
    }

    public void setUserProfileInfos(UserProfileInfos userProfileInfos) {
        this.userProfileInfos = userProfileInfos;
    }
}
