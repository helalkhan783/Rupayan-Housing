package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CustomerSearchResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.ExpenseDueResponse;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseVendorViewModel extends ViewModel {
    private ProgressDialog progressDialog;
    MutableLiveData<CustomerSearchResponse> customerResponseMutableLiveData;
    MutableLiveData<ExpenseDueResponse> expenseDueResponseMutableLiveData;

    public ExpenseVendorViewModel() {

        customerResponseMutableLiveData = new MutableLiveData<>();
        expenseDueResponseMutableLiveData = new MutableLiveData<>();
    }


    /**
     * api call for pay expense due
     */
    public void apiCallForPayExpenseDue(FragmentActivity context, String storeId, String customerId,
                                        List<String> orders, String collectedPaidAmount, String totalDueAmount, String customDiscount,
                                        String userId, String paymentTypeName, String paymentSubType, String paymentDate,
                                        String paymentRemarks, String userBankId, String branch, String userAccountNo, String bankID) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);

/*       final LottieDialogFragment lottieDialog = new LottieDialogFragment().newInstance("trophy.json",false);
        lottieDialog.setCancelable(false);
        lottieDialog.show(context.getFragmentManager(),"dialog2");*/

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String permissions = PreferenceManager.getInstance(context).getUserCredentials().getPermissions();
        String profile_type_id = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

        progressDialog.show();

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .payExpenseDue(
                        token, vendorId, storeId, customerId, permissions, profile_type_id, orders, collectedPaidAmount, totalDueAmount,
                        customDiscount, userId, paymentTypeName, paymentSubType, paymentDate, paymentRemarks,
                        userBankId, branch, userAccountNo, bankID);

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        Toasty.success(context, "Payment Successful", Toasty.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }


    /**
     * for get all due orders expense by customer and vendor i
     *
     * @return
     */
    public MutableLiveData<ExpenseDueResponse> getExpenseVendorByVendorAndCustomerId() {
        return expenseDueResponseMutableLiveData;
    }


    /**
     * api call for get due order by selected customer id and vendor id
     */
    public void apiCallForGetExpenseVendorByVendorAndCustomerId(FragmentActivity context, String selectedCustomerId) {
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        Call<ExpenseDueResponse>
                call = RetrofitClient.getInstance().getApi()
                .getExpenseOrders(token, vendorId, selectedCustomerId);
        call.enqueue(new Callback<ExpenseDueResponse>() {
            @Override
            public void onResponse(Call<ExpenseDueResponse> call, Response<ExpenseDueResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        expenseDueResponseMutableLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpenseDueResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });

    }

    /**
     * for get search ExpenseVendors
     */
    public MutableLiveData<CustomerSearchResponse> getSearchExpenseVendors() {
        return customerResponseMutableLiveData;
    }

    /**
     * api call for get ForSearchExpenseVendors
     */
    public void apiCallForSearchExpenseVendor(FragmentActivity context, String text) {
        String currentUserToken = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String currentVendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        Call<CustomerSearchResponse> call = RetrofitClient.getInstance().getApi().searchExpenseVendor(currentUserToken, text, currentVendorId);
        call.enqueue(new Callback<CustomerSearchResponse>() {
            @Override
            public void onResponse(Call<CustomerSearchResponse> call, Response<CustomerSearchResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        customerResponseMutableLiveData.postValue(response.body());
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
