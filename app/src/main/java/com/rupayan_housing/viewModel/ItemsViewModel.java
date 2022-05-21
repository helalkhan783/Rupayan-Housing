package com.rupayan_housing.viewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsViewModel extends ViewModel {

    public MutableLiveData<DuePaymentResponse> itemDeleteFromItemTrashList(
            FragmentActivity context, String itemsVendorID, String itemsStoreID, String productID
    ) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().itemDeleteFromItemTrashList(
                token, profileTypeId, itemsVendorID, itemsStoreID, productID, userId
        );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response == null) {
                    liveData.postValue(null);
                    return;
                }
                if (response.body().getStatus() == 400) {
                    liveData.postValue(response.body());
                    return;
                }
                liveData.postValue(response.body());
                return;
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }
    public MutableLiveData<DuePaymentResponse> saveAddTagResponse(FragmentActivity context, String productId, String packetid) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().saveAddTagResponse(token, vendorID, userId, productId, packetid);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response == null) {
                    liveData.postValue(null);
                    return;
                }
                if (response.body().getStatus() == 400 || response.body().getStatus() == 500) {
                    liveData.postValue(response.body());
                    return;
                }
                liveData.postValue(response.body());
                return;
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }


}
