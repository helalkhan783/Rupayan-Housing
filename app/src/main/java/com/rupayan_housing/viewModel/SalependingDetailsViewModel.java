package com.rupayan_housing.viewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.adapter.SalePendingListAdapter;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.serverResponseModel.SalePendingDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalependingDetailsViewModel extends CustomViewModel {
    public MutableLiveData<SalePendingDetailsResponse> getSalependingDetails(FragmentActivity context) {
        MutableLiveData<SalePendingDetailsResponse> liveData = new MutableLiveData<>();
        String vendorId = getVendorId(context.getApplication());
        String token = getToken(context.getApplication());
        String userId = getUserId(context.getApplication());


    /* *//**//*   Call<SalePendingDetailsResponse> call = RetrofitClient.getInstance().getApi().getSalePendingDetails(SalePendingListAdapter.serialId, token, vendorId, userId );
        call.enqueue(new Callback<SalePendingDetailsResponse>() {
            @Override
            public void onResponse(Call<SalePendingDetailsResponse> call, Response<SalePendingDetailsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    if (response.body().getStatus()==400){
                        liveData.postValue(response.body());
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        liveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<SalePendingDetailsResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        }
        );*/

        return liveData;

    }

}
