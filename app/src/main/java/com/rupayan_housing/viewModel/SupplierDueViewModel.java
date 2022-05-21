package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CustomerSearchResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.SupplierOrdersResponse;

import java.util.Set;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierDueViewModel extends ViewModel {
    MutableLiveData<CustomerSearchResponse> customerSearchResponseMutableLiveData;
    MutableLiveData<SupplierOrdersResponse> supplierOrdersResponseMutableLiveData;

    public SupplierDueViewModel() {
        customerSearchResponseMutableLiveData = new MutableLiveData<>();
        supplierOrdersResponseMutableLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<CustomerSearchResponse> getAllSuppliers() {
        return customerSearchResponseMutableLiveData;
    }

    /**
     * for get supplier order list
     *
     * @return
     */
    public MutableLiveData<SupplierOrdersResponse> getSupplierOrders() {
        return supplierOrdersResponseMutableLiveData;
    }
    /**
     * api call for pay supplier due
     */
    public void apiCallForPaySupplierDue(FragmentActivity context, String token, Set<String> customerOrderIdList, String collectedPaidAmount, String totalDuee, String storeId, String userId, String permissions, String profileTypeId, String customerId, String vendorId, String paymentTypeVal, String paymentSubType,
                                         String customDiscount, String bankId, String branch,
                                         String accountNo, String remarks, String date, String selectedPaymentToOption) {

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().supplierPaymentSend(
                token, customerOrderIdList, collectedPaidAmount, totalDuee, storeId, userId, permissions, profileTypeId, customerId, vendorId,
                paymentTypeVal, paymentSubType, customDiscount, bankId, branch, accountNo, remarks, date,selectedPaymentToOption);


        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
//                        Toasty.success(context, "payment Successful", Toasty.LENGTH_LONG).show();
                        Toasty.success(context, response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });

    }

    /**
     * api call for get supplier order
     */
    public void apiCallForGetSupplierOrder(FragmentActivity context, String supplierId) {
        String currentUserToken = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String currentUserVendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        Call<SupplierOrdersResponse> call = RetrofitClient.getInstance().getApi().getSupplierOrders(
                currentUserToken, supplierId, currentUserVendorId);
        call.enqueue(new Callback<SupplierOrdersResponse>() {
            @Override
            public void onResponse(Call<SupplierOrdersResponse> call, Response<SupplierOrdersResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        supplierOrdersResponseMutableLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<SupplierOrdersResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });


    }


    public void apiCallForSearchSuppliers(String token, String vendorId, String text) {
        Call<CustomerSearchResponse> call = RetrofitClient.getInstance().getApi().searchSuppliersByKey(token, vendorId, text);
        call.enqueue(new Callback<CustomerSearchResponse>() {
            @Override
            public void onResponse(Call<CustomerSearchResponse> call, Response<CustomerSearchResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        customerSearchResponseMutableLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSearchResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}
