package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.ItemListResponse;
import com.rupayan_housing.serverResponseModel.ItemPacketListResponse;
import com.rupayan_housing.utils.CustomViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListViewModel extends CustomViewModel {


    public MutableLiveData<ItemListResponse> getItemlist(FragmentActivity context, String pageNumber, String categoryTYpe, String itemName, String brandType) {
        MutableLiveData<ItemListResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String profileIdType = getProfileTypeId(context.getApplication());
        String storId = getStoreId(context.getApplication());

        Call<ItemListResponse> call = RetrofitClient.getInstance().getApi().getItemList(token, pageNumber, profileIdType, vendoId, storId, getUserId(context.getApplication()), brandType, itemName, categoryTYpe);
        call.enqueue(new Callback<ItemListResponse>() {
            @Override
            public void onResponse(Call<ItemListResponse> call, Response<ItemListResponse> response) {
                if (response.isSuccessful()) {
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
                  }catch (Exception e){
                      liveData.postValue(null);
                  }
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ItemListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }

    /**
     * this is for ItemPacketList
     */

    public MutableLiveData<ItemPacketListResponse> getItemPacketList(FragmentActivity context, String productId) {
        MutableLiveData<ItemPacketListResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String userId = getUserId(context.getApplication());
        String vendorID = getVendorId(context.getApplication());


        Call<ItemPacketListResponse> call = RetrofitClient.getInstance().getApi().getItemPacketList(token,userId,vendorID,productId  );
        call.enqueue(new Callback<ItemPacketListResponse>() {
            @Override
            public void onResponse(Call<ItemPacketListResponse> call, Response<ItemPacketListResponse> response) {
                if (response.isSuccessful()) {
                    try
                    {
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
                    }catch (Exception e){
                        Log.d("Error",e.getMessage());
                        liveData.postValue(null);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemPacketListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }


}
