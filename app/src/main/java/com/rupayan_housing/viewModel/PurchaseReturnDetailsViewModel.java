package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PurchaseDetailsResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseReturnDetailsViewModel extends CustomViewModel {

    public MutableLiveData<PurchaseDetailsResponse> getPurchaseDetails(FragmentActivity context, String id, String currentVendorId) {
        MutableLiveData<PurchaseDetailsResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        Call<PurchaseDetailsResponse> call = RetrofitClient.getInstance().getApi().purchaseDetails(token, currentVendorId, id);

        call.enqueue(new Callback<PurchaseDetailsResponse>() {
            @Override
            public void onResponse(Call<PurchaseDetailsResponse> call, Response<PurchaseDetailsResponse> response) {
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
                        Log.d("Error", e.getMessage());
                        liveData.postValue(null);
                        return;
                    }


                }
            }

            @Override
            public void onFailure(Call<PurchaseDetailsResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }
}
