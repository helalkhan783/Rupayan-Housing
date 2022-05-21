package com.rupayan_housing.viewModel.report_all_view_model;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.MonitoringReportListResponse;
import com.rupayan_housing.serverResponseModel.QcQaReportListResponse;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.view.fragment.all_report.licence_expire_report.list.MillerLicenceExpireReportListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LicenceExpireViewModel extends CustomViewModel {
    /**
     * for list
     */
    public MutableLiveData<MillerLicenceExpireReportListResponse> getMillerLicenceExpireReportList(FragmentActivity context, String startDate, String endDate, String associationId, String millerId, String certificateTypeID) {
        MutableLiveData<MillerLicenceExpireReportListResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());

        Call<MillerLicenceExpireReportListResponse> call = RetrofitClient.getInstance().getApi().getMillerLicenceExpireReportList(token,getUserId(context.getApplication()), startDate, endDate, associationId, millerId, certificateTypeID);
        call.enqueue(new Callback<MillerLicenceExpireReportListResponse>() {
            @Override
            public void onResponse(Call<MillerLicenceExpireReportListResponse> call, Response<MillerLicenceExpireReportListResponse> response) {
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
            public void onFailure(Call<MillerLicenceExpireReportListResponse> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
                liveData.setValue(null);
            }
        });

        return liveData;

    }

    public MutableLiveData<QcQaReportListResponse> qcQaReportList(FragmentActivity context, String startDate, String endDate, String associationId, String millerId) {

        MutableLiveData<QcQaReportListResponse> liveData = new MutableLiveData<>();


        Call<QcQaReportListResponse> call = RetrofitClient.getInstance().getApi().qcQaReportList(getToken(context.getApplication()),getVendorId(context.getApplication()),getUserId(context.getApplication()),startDate,endDate,associationId,millerId);
       call.enqueue(new Callback<QcQaReportListResponse>() {
           @Override
           public void onResponse(Call<QcQaReportListResponse> call, Response<QcQaReportListResponse> response) {
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
           public void onFailure(Call<QcQaReportListResponse> call, Throwable t) {
               Log.d("ERROR", "onFailure: " + t.getMessage());
               liveData.setValue(null);
           }
       });


        return liveData;
    }

    public MutableLiveData<MonitoringReportListResponse> monitoringReportList(FragmentActivity context, String startDate, String endDate, String zone, String type) {

        MutableLiveData<MonitoringReportListResponse> liveData = new MutableLiveData<>();


        Call<MonitoringReportListResponse> call = RetrofitClient.getInstance().getApi().monitoringReportList(getToken(context.getApplication()),getVendorId(context.getApplication()),getUserId(context.getApplication()),startDate,endDate,zone,type);
       call.enqueue(new Callback<MonitoringReportListResponse>() {
           @Override
           public void onResponse(Call<MonitoringReportListResponse> call, Response<MonitoringReportListResponse> response) {
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
           public void onFailure(Call<MonitoringReportListResponse> call, Throwable t) {
               Log.d("ERROR", "onFailure: " + t.getMessage());
               liveData.setValue(null);
           }
       });


        return liveData;
    }


}
