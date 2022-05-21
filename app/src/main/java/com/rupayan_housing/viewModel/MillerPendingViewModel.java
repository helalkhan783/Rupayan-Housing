package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.MillerPendingResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MillerPendingViewModel extends CustomViewModel {
    public MutableLiveData<MillerPendingResponse> getPendingMillerList(FragmentActivity context,String pageNumber, String processType, String millerType, String selectedDivision, String selectedDistrict, String zoneID) {
        MutableLiveData<MillerPendingResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String storeID = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();
        String storeAccess = String.valueOf(PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess());
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String userId = getUserId(context.getApplication());

        Call<MillerPendingResponse> call = RetrofitClient.getInstance().getApi()
                .getPendingMillerList(token, pageNumber, vendorId, userId, storeID, storeAccess, profileTypeId, processType, millerType, zoneID);
        call.enqueue(new Callback<MillerPendingResponse>() {
            @Override
            public void onResponse(Call<MillerPendingResponse> call, Response<MillerPendingResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;
                    }
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MillerPendingResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;
    }

}
