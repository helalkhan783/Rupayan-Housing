package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.SalesHistoryResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleHistoryViewModel extends CustomViewModel {

    public MutableLiveData<SalesHistoryResponse> getSaleHistoryList(FragmentActivity context,String pageNumber,String startDate,String endDate,String companyId,String enterPrise) {
        MutableLiveData<SalesHistoryResponse> liveData = new MutableLiveData<>();
        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());


        Call<SalesHistoryResponse> call = RetrofitClient.getInstance().getApi().getSaleHistory(token, pageNumber,userId, vendoId,startDate,endDate,companyId,enterPrise);
        call.enqueue(new Callback<SalesHistoryResponse>() {
            @Override
            public void onResponse(Call<SalesHistoryResponse> call, Response<SalesHistoryResponse> response) {
                if (response.isSuccessful()) {
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
                    }
                }else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SalesHistoryResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;

    }



}
