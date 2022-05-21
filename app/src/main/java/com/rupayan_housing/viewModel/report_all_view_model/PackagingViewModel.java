package com.rupayan_housing.viewModel.report_all_view_model;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PacketingListResponse;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.all_report.packaging_report.list.PacketingReportListResponse;
import com.rupayan_housing.view.fragment.all_report.packeting_report.list.PackegingReportListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagingViewModel extends CustomViewModel {
    /**
     * for list
     */
    public MutableLiveData<PacketingListResponse> packeging(FragmentActivity context, String starDate , String endDate , String associationId , String mllerId , String storeId , String referrerId) {
        String token = getToken(context.getApplication());
        MutableLiveData<PacketingListResponse> liveData = new MutableLiveData<>();

        Call<PacketingListResponse> call = RetrofitClient.getInstance().getApi().getPacketingList(token,getUserId(context.getApplication()),starDate,endDate,associationId,mllerId,storeId,referrerId);
        call.enqueue(new Callback<PacketingListResponse>() {
            @Override
            public void onResponse(Call<PacketingListResponse> call, Response<PacketingListResponse> response) {
                if (response.isSuccessful()){
                    if (response == null) {
                        liveData.setValue(null);
                        return; }

                    if (response.body().getStatus() == 400) {
                        liveData.setValue(response.body());
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        liveData.setValue(response.body());
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<PacketingListResponse> call, Throwable t) {
                Log.d("Error", "onFailure: " + t.getMessage());
                liveData.setValue(null);
                return;
            }
        });

        return liveData;

    }



}
