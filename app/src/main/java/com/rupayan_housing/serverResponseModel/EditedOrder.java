
package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EditedOrder {

    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("serialID")
    @Expose
    private String serialID;
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("storeID")
    @Expose
    private String storeID;
    @SerializedName("entry_userID")
    @Expose
    private String entryUserID;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("counterNo")
    @Expose
    private String counterNo;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("return_amount")
    @Expose
    private String returnAmount;
    @SerializedName("order_month")
    @Expose
    private String orderMonth;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("order_year")
    @Expose
    private String orderYear;
    @SerializedName("vat")
    @Expose
    private String vat;
    @SerializedName("is_approved")
    @Expose
    private String isApproved;
    @SerializedName("is_confirmed")
    @Expose
    private String isConfirmed;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("is_ecommerce")
    @Expose
    private String isEcommerce;
    @SerializedName("carry_cost")
    @Expose
    private String carryCost;
    @SerializedName("po_no")
    @Expose
    private String poNo;
    @SerializedName("indent_no")
    @Expose
    private String indentNo;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("contact_person_phone")
    @Expose
    private String contactPersonPhone;
    @SerializedName("challan_number")
    @Expose
    private String challanNumber;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("discount_rate")
    @Expose
    private String discountRate;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("requisition_ref")
    @Expose
    private Object requisitionRef;
    @SerializedName("reference_user")
    @Expose
    private String referenceUser;
    @SerializedName("edited_by")
    @Expose
    private String editedBy;
    @SerializedName("edit_approved_by")
    @Expose
    private String editApprovedBy;
    @SerializedName("stage")
    @Expose
    private Object stage;
    @SerializedName("edit_attempted_by")
    @Expose
    private String editAttemptedBy;
    @SerializedName("edit_attempted_date_time")
    @Expose
    private String editAttemptedDateTime;
    @SerializedName("store_name")
    @Expose
    private String storeName;
}
