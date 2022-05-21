package com.rupayan_housing.viewModel.report_all_view_model;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_report.sale_response.SaleReportListResponse;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleReportViewModel extends CustomViewModel {
    /**
     * Sale report List
     */
    public MutableLiveData<SaleReportListResponse> getSaleReportList(FragmentActivity context, String startDate,
                                                                     String endDate,String associationId, String millerId, String supplierId, String storeId,
                                                                     String brandId, String categoryId) {
        MutableLiveData<SaleReportListResponse> liveData = new MutableLiveData<>();
        String token = getToken(context.getApplication());

        Call<SaleReportListResponse> call = RetrofitClient.getInstance().getApi().
                getSaleReportList(token,getUserId(context.getApplication()), startDate, endDate, associationId,millerId, storeId,
                        supplierId, Arrays.asList(brandId), Arrays.asList(categoryId));
        call.enqueue(new Callback<SaleReportListResponse>() {
            @Override
            public void onResponse(Call<SaleReportListResponse> call, Response<SaleReportListResponse> response) {
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
            public void onFailure(Call<SaleReportListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.setValue(null);
            }
        });

        return liveData;
    }

}
