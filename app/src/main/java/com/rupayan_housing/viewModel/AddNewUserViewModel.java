package com.rupayan_housing.viewModel;

import android.app.Application;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.view.fragment.auth.sign_up.SuccessResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewUserViewModel extends CustomerViewModel {
    public MutableLiveData<SuccessResponse> sendPhoneNumberForGetOtp(FragmentActivity context, String phoneNumber) {
        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();

        Call<SuccessResponse> call = RetrofitClient.getInstance().getApi().sendPhoneNumberForGetOtp(token , phoneNumber);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
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
                    return;
                }
                liveData.postValue(new SuccessResponse());
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                liveData.postValue(new SuccessResponse());
            }
        });
        return liveData;
    }


    public MutableLiveData<SuccessResponse> sendOTPCode(FragmentActivity context, String phoneNumber,String otpCode){
        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();

        Call<SuccessResponse> call = RetrofitClient.getInstance().getApi().sendPhoneNumberForGetOtp(token, phoneNumber);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
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
                    return;
                }
                liveData.postValue(new SuccessResponse());
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                liveData.postValue(new SuccessResponse());
            }
        });
        return liveData;
    }

}
