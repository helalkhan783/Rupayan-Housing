package com.rupayan_housing.view.fragment.all_report.lisence_report;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.MonitoringReportPageDataResponse;
import com.rupayan_housing.utils.CustomViewModel;
import com.rupayan_housing.serverResponseModel.list_response.MillerLicenceReportListResponse;
import com.rupayan_housing.serverResponseModel.miller_response.MillerLicenceResponse;
import com.rupayan_housing.serverResponseModel.response.MillerLicenceReportResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LicenceReportViewModel extends CustomViewModel {
    public MutableLiveData<MillerLicenceReportResponse> getLicencePageData(FragmentActivity context, String profileID) {

        MutableLiveData<MillerLicenceReportResponse> liveData = new MutableLiveData<>();


        String token = getToken(context.getApplication());
        String storeID = getStoreId(context.getApplication());
        String storeAccessId = getStoreAccessId(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String profileTypeId = getProfileTypeId(context.getApplication());
        String profileId = getProfileId(context.getApplication());
        String userID = getUserId(context.getApplication());
        Call<MillerLicenceReportResponse> call = RetrofitClient.getInstance().getApi()
                .getMillerLicencePageData(token, userID,vendorId, storeID, storeAccessId, profileTypeId, profileId);


        call.enqueue(new Callback<MillerLicenceReportResponse>() {
            @Override
            public void onResponse(Call<MillerLicenceReportResponse> call, Response<MillerLicenceReportResponse> response) {
                try {
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
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                    liveData.postValue(null);
                }
            }


            @Override
            public void onFailure(Call<MillerLicenceReportResponse> call, Throwable t) {
                liveData.setValue(null);
                Log.d("Error", "onFailure: " + t.getMessage());

            }
        });

        return liveData;

    }


/**
 * monitoring report page data
 * */
    public MutableLiveData<MonitoringReportPageDataResponse> monitoringReportPageData(FragmentActivity context) {

        MutableLiveData<MonitoringReportPageDataResponse> liveData = new MutableLiveData<>();
        String token    = getToken(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        Call<MonitoringReportPageDataResponse> call = RetrofitClient.getInstance().getApi()
                .monitoringReportPageData(token,getUserId(context.getApplication()), vendorId);
        call.enqueue(new Callback<MonitoringReportPageDataResponse>() {
            @Override
            public void onResponse(Call<MonitoringReportPageDataResponse> call, Response<MonitoringReportPageDataResponse> response) {
                try {
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
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                    liveData.postValue(null);
                }
            }
            @Override
            public void onFailure(Call<MonitoringReportPageDataResponse> call, Throwable t) {
                liveData.setValue(null);
                Log.d("Error", "onFailure: " + t.getMessage());

            }
        });

        return liveData;

    }


    public MutableLiveData<MillerLicenceResponse> getMillerData(FragmentActivity context, String associationId) {


        MutableLiveData<MillerLicenceResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());
        String storeID = getStoreId(context.getApplication());
        String storeAccessId = getStoreAccessId(context.getApplication());
        String vendorId = getVendorId(context.getApplication());
        String profileTypeId = getProfileTypeId(context.getApplication());
        Call<MillerLicenceResponse> call = RetrofitClient.getInstance().getApi().getLicenceMiller(token, profileTypeId, storeID, vendorId, associationId, storeAccessId);


        call.enqueue(new Callback<MillerLicenceResponse>() {
            @Override
            public void onResponse(Call<MillerLicenceResponse> call, Response<MillerLicenceResponse> response) {
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


            @Override
            public void onFailure(Call<MillerLicenceResponse> call, Throwable t) {
                liveData.setValue(null);
                Log.d("Error", "onFailure: " + t.getMessage());

            }
        });

        return liveData;

    }

    /**
     * for list
     */
    public MutableLiveData<MillerLicenceReportListResponse> licenceReportList(FragmentActivity context, String startDate, String endDate, String associationId, String millerId, String certificateTypeID) {
        MutableLiveData<MillerLicenceReportListResponse> liveData = new MutableLiveData<>();

        String token = getToken(context.getApplication());

        Call<MillerLicenceReportListResponse> call = RetrofitClient.getInstance().getApi().getMillerLicenceReportList(token,getUserId(context.getApplication()), startDate, endDate, associationId, millerId, certificateTypeID);
        call.enqueue(new Callback<MillerLicenceReportListResponse>() {
            @Override
            public void onResponse(Call<MillerLicenceReportListResponse> call, Response<MillerLicenceReportListResponse> response) {
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
            public void onFailure(Call<MillerLicenceReportListResponse> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
                liveData.setValue(null);
            }
        });

        return liveData;

    }
}
