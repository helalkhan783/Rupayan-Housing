package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.adapter.CustomerEditHistoryAdapter;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.AddMonitoringPageResponse;
import com.rupayan_housing.serverResponseModel.CustomerEditHistoryResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.MillEditHistoryResponse;
import com.rupayan_housing.serverResponseModel.MillerListByZoneIdResponse;
import com.rupayan_housing.serverResponseModel.MonitoringHistoryListResponse;
import com.rupayan_housing.serverResponseModel.QcQaHistoryResponse;
import com.rupayan_housing.serverResponseModel.MonitoringModel;
import com.rupayan_housing.serverResponseModel.UpdateMonitoringPageResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringViewModel extends ViewModel {

    public MutableLiveData<AddMonitoringPageResponse> getAppMonitoringPageData(FragmentActivity context) {

        MutableLiveData<AddMonitoringPageResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();


        Call<AddMonitoringPageResponse> call = RetrofitClient.getInstance().getApi()
                .addMonitoringPageData(token, vendorId);

        call.enqueue(new Callback<AddMonitoringPageResponse>() {
            @Override
            public void onResponse(Call<AddMonitoringPageResponse> call, Response<AddMonitoringPageResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AddMonitoringPageResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", "ERROR");
            }
        });


        return liveData;
    }

    public MutableLiveData<MillerListByZoneIdResponse> getMillerByZone(FragmentActivity context, String zoneId) {

        MutableLiveData<MillerListByZoneIdResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        Call<MillerListByZoneIdResponse> call = RetrofitClient.getInstance().getApi()
                .getMillerByZone(token, vendorId, userId, zoneId);

        call.enqueue(new Callback<MillerListByZoneIdResponse>() {
            @Override
            public void onResponse(Call<MillerListByZoneIdResponse> call, Response<MillerListByZoneIdResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MillerListByZoneIdResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", "ERROR");
            }
        });


        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> addNewMonitoring(
            FragmentActivity context, String monitoringDate, String publishDate, String zoneID, String millID, String monitoringType,
            MultipartBody.Part document, String monitoringSummary, String otherMonitoringTypename) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        RequestBody vendorid = RequestBody.create(MediaType.parse("multipart/form-data"), vendorId);
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        RequestBody addMonitoring = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody monitoringdate = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringDate);
        RequestBody publishdate = RequestBody.create(MediaType.parse("multipart/form-data"), publishDate);
        RequestBody zoneiD = RequestBody.create(MediaType.parse("multipart/form-data"), zoneID);
        RequestBody milliD = RequestBody.create(MediaType.parse("multipart/form-data"), millID);
        RequestBody monitoringtype = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringType);
        RequestBody monitoringsummary = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringSummary);
        RequestBody otherMonitoringTypeName = RequestBody.create(MediaType.parse("multipart/form-data"), otherMonitoringTypename);


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().addNewMonitoring(
                token, addMonitoring, userid, vendorid, monitoringdate,
                publishdate, zoneiD, milliD, monitoringtype, document, monitoringsummary, otherMonitoringTypeName
        );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());

                    } else {
                        liveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", "ERROR");
            }
        });


        return liveData;
    }


    public MutableLiveData<MonitoringModel> getMonitoringList(FragmentActivity context, String pageNumber, String startDate, String endDate, String PublishedDate, String monitoringProcessType, String zoneId) {
        MutableLiveData<MonitoringModel> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<MonitoringModel> call = RetrofitClient.getInstance().getApi().getMonitoringList(token, pageNumber, vendorId, userId, startDate, endDate, PublishedDate, monitoringProcessType, zoneId);
        call.enqueue(new Callback<MonitoringModel>() {
            @Override
            public void onResponse(Call<MonitoringModel> call, Response<MonitoringModel> response) {
                if (response.isSuccessful()) {
                    try {
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
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                } else {
                    liveData.postValue(null);
                    return;
                }

            }

            @Override
            public void onFailure(Call<MonitoringModel> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                return;
            }
        });

        return liveData;
    }


    public MutableLiveData<UpdateMonitoringPageResponse> getUpdateMonitoringPageData(FragmentActivity context, String id) {
        MutableLiveData<UpdateMonitoringPageResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        Call<UpdateMonitoringPageResponse> call = RetrofitClient.getInstance().getApi()
                .updateMonitoringPageData(token, id, vendorId);
        call.enqueue(new Callback<UpdateMonitoringPageResponse>() {
            @Override
            public void onResponse(Call<UpdateMonitoringPageResponse> call, Response<UpdateMonitoringPageResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;

                    }
                    liveData.postValue(response.body());
                    return;
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateMonitoringPageResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> updateMonitoring(
            FragmentActivity context, String id, String monitoringDate, String publishDate, String zoneId, String monitoringType,
            String millId, String monitoringSummary, MultipartBody.Part document, String otherMonitoringTypename) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();


        RequestBody iD = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        RequestBody profileTypeid = RequestBody.create(MediaType.parse("multipart/form-data"), profileTypeId);
        RequestBody vendorid = RequestBody.create(MediaType.parse("multipart/form-data"), vendorId);
        RequestBody monitoringdate = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringDate);
        RequestBody publishdate = RequestBody.create(MediaType.parse("multipart/form-data"), publishDate);
        RequestBody zoneid = RequestBody.create(MediaType.parse("multipart/form-data"), zoneId);
        RequestBody monitoringtype = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringType);
        RequestBody millid = RequestBody.create(MediaType.parse("multipart/form-data"), millId);
        RequestBody monitoringsummary = RequestBody.create(MediaType.parse("multipart/form-data"), monitoringSummary);
        RequestBody otherMonitoringTypeName = RequestBody.create(MediaType.parse("multipart/form-data"), otherMonitoringTypename);


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().updateMonitoring(
                token, iD, userid, profileTypeid, vendorid, monitoringdate, publishdate, zoneid, monitoringtype, millid, monitoringsummary,
                document, otherMonitoringTypeName);

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
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
                        return;
                    }
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public MutableLiveData<QcQaHistoryResponse> getQcqaHistoryList(FragmentActivity context, String id) {
        MutableLiveData<QcQaHistoryResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<QcQaHistoryResponse> call = RetrofitClient.getInstance().getApi().getQcqaHistoryList(token, userId, vendorId, id);
        call.enqueue(new Callback<QcQaHistoryResponse>() {
            @Override
            public void onResponse(Call<QcQaHistoryResponse> call, Response<QcQaHistoryResponse> response) {
                if (response.isSuccessful()) {
                    try {
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
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                } else {
                    liveData.postValue(null);
                    return;
                }

            }

            @Override
            public void onFailure(Call<QcQaHistoryResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                return;
            }
        });

        return liveData;
    }

    public MutableLiveData<CustomerEditHistoryResponse> getCustomerAndSupplierEditHistory(FragmentActivity context, String id, String type) {
        MutableLiveData<CustomerEditHistoryResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<CustomerEditHistoryResponse> call = RetrofitClient.getInstance().getApi().getCustomerAndSupplierEditHistory(token, userId, vendorId, id, type);
        call.enqueue(new Callback<CustomerEditHistoryResponse>() {
            @Override
            public void onResponse(Call<CustomerEditHistoryResponse> call, Response<CustomerEditHistoryResponse> response) {
                if (response.isSuccessful()) {
                    try {
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
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                } else {
                    liveData.postValue(null);
                    return;
                }

            }

            @Override
            public void onFailure(Call<CustomerEditHistoryResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                return;
            }
        });

        return liveData;
    }

    public MutableLiveData<MonitoringHistoryListResponse> getMonitoringHistoryList(FragmentActivity context, String id) {
        MutableLiveData<MonitoringHistoryListResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<MonitoringHistoryListResponse> call = RetrofitClient.getInstance().getApi().getMonitoringHistoryList(token, userId, vendorId, id);
        call.enqueue(new Callback<MonitoringHistoryListResponse>() {
            @Override
            public void onResponse(Call<MonitoringHistoryListResponse> call, Response<MonitoringHistoryListResponse> response) {
                if (response.isSuccessful()) {
                    try {
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
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                } else {
                    liveData.postValue(null);
                    return;
                }

            }

            @Override
            public void onFailure(Call<MonitoringHistoryListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                return;
            }
        });

        return liveData;
    }

    public MutableLiveData<MillEditHistoryResponse> getMillEditHistory(FragmentActivity context, String id) {
        MutableLiveData<MillEditHistoryResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
         String storeId = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String storeAccess = PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess();

        Call<MillEditHistoryResponse> call = RetrofitClient.getInstance().getApi().getMillEditHistory(token, userId, vendorId, id, storeId, storeAccess, profileTypeId);
        call.enqueue(new Callback<MillEditHistoryResponse>() {
            @Override
            public void onResponse(Call<MillEditHistoryResponse> call, Response<MillEditHistoryResponse> response) {
                if (response.isSuccessful()) {
                    try {
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
                    } catch (Exception e) {
                        Log.d("ERROR", "" + e.getMessage());
                    }
                } else {
                    liveData.postValue(null);
                    return;
                }

            }

            @Override
            public void onFailure(Call<MillEditHistoryResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                liveData.postValue(null);
                return;
            }
        });

        return liveData;
    }

}
