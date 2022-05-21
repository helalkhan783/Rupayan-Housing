package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.SalePendingResponse;
import com.rupayan_housing.serverResponseModel.SalePendingReturnResponse;
import com.rupayan_housing.serverResponseModel.SaleReturnPendingList;
import com.rupayan_housing.serverResponseModel.SaleReturnResponse;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.serverResponseModel.SaleDeclinedResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalePendingListViewModel extends CustomViewModel {
    public MutableLiveData<SalePendingResponse> getSalePendinglist(FragmentActivity context,String page,String startDate,String endDate,
                                                                   String companyId,String enterPriseId){
        MutableLiveData<SalePendingResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<SalePendingResponse> call = RetrofitClient.getInstance().getApi().getSalePendingList(token,page,userId,vendoId,startDate,endDate,companyId,enterPriseId);
        call.enqueue(new Callback<SalePendingResponse>() {
            @Override
            public void onResponse(Call<SalePendingResponse> call, Response<SalePendingResponse> response) {
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
            public void onFailure(Call<SalePendingResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }

/** for sale decline*/
     public MutableLiveData<SaleDeclinedResponse> getSaleDeclinedList(FragmentActivity context,String page,String startDate,String endDate,String companyId,String enterPriseID){
        MutableLiveData<SaleDeclinedResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<SaleDeclinedResponse> call = RetrofitClient.getInstance().getApi().getSalePendingDeclinedList(token,page,userId,vendoId,startDate,endDate,companyId,enterPriseID);
        call.enqueue(new Callback<SaleDeclinedResponse>() {
            @Override
            public void onResponse(Call<SaleDeclinedResponse> call, Response<SaleDeclinedResponse> response) {
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
            public void onFailure(Call<SaleDeclinedResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }
/** for sale return history*/

    public MutableLiveData<SaleReturnResponse> getSaleReturnHistoryList(FragmentActivity context,String page,String startDate,String endDate,String companyId,String enterPriseID){
        MutableLiveData<SaleReturnResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<SaleReturnResponse> call = RetrofitClient.getInstance().getApi().getSaleReturnHistoryList(token,page,userId,vendoId,startDate,endDate,companyId,enterPriseID);
        call.enqueue(new Callback<SaleReturnResponse>() {
            @Override
            public void onResponse(Call<SaleReturnResponse> call, Response<SaleReturnResponse> response) {
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
            public void onFailure(Call<SaleReturnResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }



    /**  sale pending return*/


    public MutableLiveData<SalePendingReturnResponse> getSalePendingReturnList(FragmentActivity context, String page, String startDate, String endDate, String companyId, String enterPriseID){
        MutableLiveData<SalePendingReturnResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String vendoId = getVendorId(context.getApplication());
        String userId = getUserId(context.getApplication());

        Call<SalePendingReturnResponse> call = RetrofitClient.getInstance().getApi().getSalePendingReturnList(token,page,userId,vendoId,startDate,endDate,companyId,enterPriseID);
        call.enqueue(new Callback<SalePendingReturnResponse>() {
            @Override
            public void onResponse(Call<SalePendingReturnResponse> call, Response<SalePendingReturnResponse> response) {
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
            public void onFailure(Call<SalePendingReturnResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                liveData.setValue(null);
            }
        });


        return liveData;

    }

}
