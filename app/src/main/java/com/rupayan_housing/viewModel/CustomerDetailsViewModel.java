package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CustomerDetailsResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDetailsViewModel extends CustomViewModel {

    public MutableLiveData<CustomerDetailsResponse> customerDetails(FragmentActivity context, String customerId, String typeId) {
        MutableLiveData<CustomerDetailsResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());
        String storeId = getStoreId(context.getApplication());
        Call<CustomerDetailsResponse> call = RetrofitClient.getInstance().getApi().customerDetails(token, vendorId, userId, storeId, typeId, customerId);


        call.enqueue(new Callback<CustomerDetailsResponse>() {
            @Override
            public void onResponse(Call<CustomerDetailsResponse> call, Response<CustomerDetailsResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response == null) {
                            liveData.postValue(null);
                            return;
                        }
                        if (response.body().getStatus() == 400) {
                            liveData.postValue(response.body());
                            return;
                        }
                        if (response.body().getStatus() == 200) {
                            liveData.postValue(response.body());
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", e.getMessage());
                        liveData.postValue(null);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerDetailsResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;

    }

    public MutableLiveData<DuePaymentResponse> customerDetailsApprove(FragmentActivity context,String customerId,String typeId, String note) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());
        String storeId = getStoreId(context.getApplication());

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().approveCustomerEditDetails(token,vendorId,userId,storeId,typeId,customerId,note);


        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response == null) {
                            liveData.postValue(null);
                            return;
                        }
                        if (response.body().getStatus() == 400) {
                            liveData.postValue(response.body());
                            return;
                        }
                        if (response.body().getStatus() == 200) {
                            liveData.postValue(response.body());
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", e.getMessage());
                        liveData.postValue(null);
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });


        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> declineCustomerEditDetails(FragmentActivity context,String customerId,String typeId, String note) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());
        String storeId = getStoreId(context.getApplication());

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().declineCustomerEditDetails(token,vendorId,userId,storeId,typeId,customerId,note);


        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response == null) {
                            liveData.postValue(null);
                            return;
                        }
                        if (response.body().getStatus() == 400) {
                            liveData.postValue(response.body());
                            return;
                        }
                        if (response.body().getStatus() == 200) {
                            liveData.postValue(response.body());
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("ERROR", e.getMessage());
                        liveData.postValue(null);
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });


        return liveData;
    }

}
