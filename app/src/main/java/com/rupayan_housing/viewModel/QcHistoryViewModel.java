package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.QcHistoryResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QcHistoryViewModel extends CustomViewModel {

  public MutableLiveData<QcHistoryResponse> getQcHistoryList(FragmentActivity context,String pageNumber,String fromDate,String toDate,String enterPriseId,String model) {

        MutableLiveData<QcHistoryResponse> liveData = new MutableLiveData<>();
        String token = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<QcHistoryResponse> call = RetrofitClient.getInstance().getApi().getQcHistory(token,pageNumber,userId, vendorId,fromDate,toDate,enterPriseId,model);
        call.enqueue(new Callback<QcHistoryResponse>() {
            @Override
            public void onResponse(Call<QcHistoryResponse> call, Response<QcHistoryResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus()==400){
                        liveData.postValue(response.body());
                        return;
                    }


                    if (response.body().getStatus() == 200) {
                        liveData.setValue(response.body());
                    }
                } else liveData.setValue(null);
            }

            @Override
            public void onFailure(Call<QcHistoryResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;
    }
}
