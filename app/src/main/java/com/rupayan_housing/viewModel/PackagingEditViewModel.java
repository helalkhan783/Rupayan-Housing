package com.rupayan_housing.viewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.EditPackagingDataResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagingEditViewModel extends CustomViewModel {
    public MutableLiveData<EditPackagingDataResponse> getPackagingPageData(FragmentActivity context,String packagingSlId){
        MutableLiveData<EditPackagingDataResponse> liveData = new MutableLiveData<>();
        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        Call<EditPackagingDataResponse> call = RetrofitClient.getInstance().getApi().getPackagingPageData(token,vendorId,packagingSlId);


        call.enqueue(new Callback<EditPackagingDataResponse>() {
            @Override
            public void onResponse(Call<EditPackagingDataResponse> call, Response<EditPackagingDataResponse> response) {
               if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                  if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;
                    }
                   if (response.body().getStatus() != 200) {
                        liveData.postValue(null);
                        return;
                    }
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<EditPackagingDataResponse> call, Throwable t) {

            }
        });
        return liveData;

    }
}
