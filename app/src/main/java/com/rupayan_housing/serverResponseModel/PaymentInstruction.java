package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PaymentInstruction {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("parties_with_limit")
    @Expose
    private List<PaymentInstructionResponse> paymentInstructionResponseList = null;
    @SerializedName("lists")
    @Expose
    private List<PaymentInstructionListResponse> paymentInstructionListResponseList = null;
    @SerializedName("instructions")
    @Expose
    private List<AddNewLimitInstructionResponse> addNewPaymentInstructionsList = null;


}
