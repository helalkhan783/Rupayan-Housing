package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PendingPurchaseNotificationDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingPurchaseApproveDeclineViewModel extends ViewModel {
    public PendingPurchaseApproveDeclineViewModel() {
    }


    public MutableLiveData<PendingPurchaseNotificationDetailsResponse> getPendingPurchaseNotificationDetails(FragmentActivity context, String refOrderId, String orderVendorId) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        MutableLiveData<PendingPurchaseNotificationDetailsResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();


        Call<PendingPurchaseNotificationDetailsResponse> call = RetrofitClient.getInstance().getApi()
                .getPendingPurchaseNotificationDetails(token, orderVendorId, refOrderId);

        call.enqueue(new Callback<PendingPurchaseNotificationDetailsResponse>() {
            @Override
            public void onResponse(Call<PendingPurchaseNotificationDetailsResponse> call, Response<PendingPurchaseNotificationDetailsResponse> response) {
                try {
                    if (response == null) {
                        liveData.setValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400) {
                        liveData.setValue(response.body());
                        return;
                    }

                    if (response.body().getStatus() == 200) {
                        liveData.setValue(response.body());
                        return;
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<PendingPurchaseNotificationDetailsResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", t.getMessage());
            }
        });
        return liveData;

    }
}
