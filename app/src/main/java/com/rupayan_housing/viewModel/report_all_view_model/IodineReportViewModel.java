package com.rupayan_housing.viewModel.report_all_view_model;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.all_report.iodine_used_report.list.IodineReportListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IodineReportViewModel extends CustomViewModel {
    public MutableLiveData<IodineReportListResponse> getIodineReportList(FragmentActivity context, String starDate, String endDate, String associationId, String mllerId, String storeId) {

        String token = getToken(context.getApplication());

        MutableLiveData<IodineReportListResponse> liveData = new MutableLiveData<>();


        Call<IodineReportListResponse> call = RetrofitClient.getInstance().getApi().getIodineReportList(token, starDate, endDate, associationId, mllerId, storeId);
        call.enqueue(new Callback<IodineReportListResponse>() {
            @Override
            public void onResponse(Call<IodineReportListResponse> call, Response<IodineReportListResponse> response) {
                if (response.isSuccessful()) {
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
                }
            }

            @Override
            public void onFailure(Call<IodineReportListResponse> call, Throwable t) {
                Log.d("Error", "onFailure: " + t.getMessage());
                liveData.setValue(null);
                return;
            }
        });

        return liveData;

    }
}
