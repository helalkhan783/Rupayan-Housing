
package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EditedCustomer {

    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("customer_fname")
    @Expose
    private String customerFname;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("customer_lname")
    @Expose
    private Object customerLname;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("typeID")
    @Expose
    private String typeID;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("alt_phone")
    @Expose
    private String altPhone;
    @SerializedName("age")
    @Expose
    private Object age;
    @SerializedName("entry_userID")
    @Expose
    private String entryUserID;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("thana")
    @Expose
    private String thana;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("tin")
    @Expose
    private String tin;
    @SerializedName("bazar")
    @Expose
    private String bazar;
    @SerializedName("due_limit")
    @Expose
    private Object dueLimit;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("customer_note")
    @Expose
    private String customerNote;
}
