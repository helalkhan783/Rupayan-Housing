package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PackagingDetailsResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagingDetailsViewModel extends CustomViewModel {
    public MutableLiveData<PackagingDetailsResponse> getPackagingDetails(FragmentActivity context,String packagingVendorId,String packagingSlId){
        MutableLiveData<PackagingDetailsResponse> liveData = new MutableLiveData();

        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<PackagingDetailsResponse> call = RetrofitClient.getInstance().getApi().getPackagingDetails(token,packagingSlId,vendorId,userId,packagingVendorId);

        call.enqueue(new Callback<PackagingDetailsResponse>() {
            @Override
            public void onResponse(Call<PackagingDetailsResponse> call, Response<PackagingDetailsResponse> response) {
                if (response.isSuccessful()){
                 try {
                     if (response == null){
                         liveData.postValue(null);
                         return;
                     }

                     liveData.postValue(response.body());
                 }catch (Exception e){
                     liveData.postValue(null);
                 }
                 }

            }

            @Override
            public void onFailure(Call<PackagingDetailsResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;
    }
}
