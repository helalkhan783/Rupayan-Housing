package com.rupayan_housing.viewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.PermissionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentPermissionViewModel extends ViewModel {

    public MutableLiveData<PermissionResponse> getCurrentUserRealtimePermissions(String token, String userId) {
        MutableLiveData<PermissionResponse> liveData = new MutableLiveData<>();

        Call<PermissionResponse> call
                = RetrofitClient.getInstance().getApi().getCurrentUserPermissions(token, userId, "1");
        call.enqueue(new Callback<PermissionResponse>() {
            @Override
            public void onResponse(Call<PermissionResponse> call, Response<PermissionResponse> response) {
                try {
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
                } catch (Exception e) {
                    liveData.postValue(null);
                    return;
                }

            }


            @Override
            public void onFailure(Call<PermissionResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });


        return liveData;
    }
}
