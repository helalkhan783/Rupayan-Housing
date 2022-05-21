package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.auth.sign_up.SuccessResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends CustomViewModel {

    /**
     * phoneVerify and get otp
     */

    public MutableLiveData<SuccessResponse> sendNumberForForgotPassword(FragmentActivity context, String phone) {
        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();


        Call<SuccessResponse> call = RetrofitClient.getInstance().getApi().verifyNumberForForgotPw(phone);

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus() == 200) {
                            Log.d("TYPE", new Gson().toJson(response.body()));
                            liveData.postValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    /**
     * send phone and otp to the sarver
     */

    public MutableLiveData<SuccessResponse> sendNumberAndOtpforForget(FragmentActivity context, String code, String phone) {
        MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();

        Call<SuccessResponse> call = RetrofitClient.getInstance().getApi().sendNumberAndOtp(code, phone);

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null || response.body().getStatus() == 500){
                        liveData.postValue(null);
                        return;
                    }
                    if (response .body().getStatus() ==400){
                        liveData.postValue(response.body());
                        return;
                    }

                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                    }
                }

                else  liveData.postValue(null);
            }


            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }
/**
 * update password
 * **/
public MutableLiveData<SuccessResponse> sendNewpassword(FragmentActivity context,String phone,String secretkey,String newpassword ) {
    MutableLiveData<SuccessResponse> liveData = new MutableLiveData<>();

    Call<SuccessResponse> call = RetrofitClient.getInstance().getApi().sendNewPassword(phone,secretkey,newpassword);

    call.enqueue(new Callback<SuccessResponse>() {
        @Override
        public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

            if (response.isSuccessful()) {
                assert response.body() != null;
                if (response.body().getStatus() == 200) {
                    Log.d("TYPE", new Gson().toJson(response.body()));
                    liveData.postValue(response.body());
                }
            }
        }


        @Override
        public void onFailure(Call<SuccessResponse> call, Throwable t) {
            Log.d("ERROR", t.getMessage());
            liveData.postValue(null);
        }
    });

    return liveData;
}

}
