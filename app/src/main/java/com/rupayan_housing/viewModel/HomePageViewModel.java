package com.rupayan_housing.viewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.HomePageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageViewModel extends ViewModel {
    public MutableLiveData<HomePageResponse> getHomePageData(FragmentActivity context) {
        MutableLiveData<HomePageResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<HomePageResponse> call =
                RetrofitClient.getInstance().getApi().getHomePageData(token, vendorId, userId);
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
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
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;

    }
}
