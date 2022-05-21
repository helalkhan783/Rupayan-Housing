package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CategoryListResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoryViewModel extends CustomViewModel {
    public MutableLiveData<CategoryListResponse> getCategoryList(FragmentActivity context, String pageNumber,String brand) {
        MutableLiveData<CategoryListResponse> liveData = new MutableLiveData<>();
        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String profileIdType = getProfileTypeId(context.getApplication());
        String storId = getStoreId(context.getApplication());

        Call<CategoryListResponse> call = RetrofitClient.getInstance().getApi().getItemCategoryList(token, pageNumber, profileIdType, vendoId, storId,brand);
        call.enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                try {
                    if (response == null) {
                        liveData.setValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400) {
                        liveData.setValue(response.body());
                        return;
                    }

                    if (response.body().getStatus() == 200) {
                        liveData.setValue(response.body());
                        return;
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }

}
