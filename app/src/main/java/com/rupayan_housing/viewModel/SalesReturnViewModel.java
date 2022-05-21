package com.rupayan_housing.viewModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.GetSalesReturnDetailsResponse;
import com.rupayan_housing.serverResponseModel.PendingSalesReturnDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReturnViewModel extends CustomerViewModel {


    public MutableLiveData<PendingSalesReturnDetailsResponse> getSalesReturnDetailsDetails(
            FragmentActivity context, String id
    ) {
        MutableLiveData<PendingSalesReturnDetailsResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        Call<PendingSalesReturnDetailsResponse> call
                = RetrofitClient.getInstance().getApi()
                .getReturnSalesOrderDetails(token, vendorId, id);
        call.enqueue(new Callback<PendingSalesReturnDetailsResponse>() {
            @Override
            public void onResponse(Call<PendingSalesReturnDetailsResponse> call, Response<PendingSalesReturnDetailsResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        if (response == null) {
                            liveData.setValue(null);
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
                    } else {
                        liveData.postValue(null);
                    }
                } catch (Exception e) {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<PendingSalesReturnDetailsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> approveSalesReturnWholeOrderCancel(FragmentActivity context, String note, String id, String vendorId) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String userID = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .salesWholeOrderCancelApprove(token, vendorId, id, userID, profileTypeId, note);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        if (response == null) {
                            liveData.setValue(null);
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
                    }
                } catch (Exception e) {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> declineApproveSalesReturnWholeOrderCancel(FragmentActivity context, String note, String id, String vendorId) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String userID = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .salesWholeOrderCancelDecline(token, vendorId, id, userID, profileTypeId, note);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        if (response == null) {
                            liveData.setValue(null);
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
                    } else {
                        liveData.postValue(null);
                    }
                } catch (Exception e) {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }


}
