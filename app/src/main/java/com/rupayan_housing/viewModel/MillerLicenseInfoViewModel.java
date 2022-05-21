package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.MillerLicenseInfoEditResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MillerLicenseInfoViewModel extends ViewModel {

    public MutableLiveData<MillerLicenseInfoEditResponse> getMillerPreviousOwnerLicenseInfo(FragmentActivity context, String slid) {
        MutableLiveData<MillerLicenseInfoEditResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();

        Call<MillerLicenseInfoEditResponse> call = RetrofitClient.getInstance().getApi().getLicensePreviousInfo(token,slid);
        call.enqueue(new Callback<MillerLicenseInfoEditResponse>() {
            @Override
            public void onResponse(Call<MillerLicenseInfoEditResponse> call, Response<MillerLicenseInfoEditResponse> response) {
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
                    Log.d("ERROR", "" + e.getMessage());
                    liveData.postValue(null);
                    return;
                }
            }

            @Override
            public void onFailure(Call<MillerLicenseInfoEditResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }


}
