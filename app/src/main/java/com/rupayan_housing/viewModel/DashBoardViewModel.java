package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.AgencyMonitoringResponse;
import com.rupayan_housing.serverResponseModel.DashBoardResponse;
import com.rupayan_housing.serverResponseModel.DashboardDataResponse;
import com.rupayan_housing.serverResponseModel.IssueMonitoringResponse;
import com.rupayan_housing.serverResponseModel.JaninaResponse;
import com.rupayan_housing.serverResponseModel.LastMonthIodineStockResponse;
import com.rupayan_housing.serverResponseModel.LastMonthIodizationResponse;
import com.rupayan_housing.serverResponseModel.LastMonthQcQaResponse;
import com.rupayan_housing.serverResponseModel.LastMonthSaleResponse;
import com.rupayan_housing.serverResponseModel.PieChartDataresponse;
import com.rupayan_housing.serverResponseModel.TopTenMillerResponse;
import com.rupayan_housing.serverResponseModel.ZoneWiseMonitoringResponse;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardViewModel extends ViewModel {
    public MutableLiveData<DashBoardResponse> getDashBoardResponse(FragmentActivity context) {
        MutableLiveData<DashBoardResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();

        Call<DashBoardResponse> call = RetrofitClient.getInstance().getApi()
                .getDashBoardResponse(token, vendorID);
        call.enqueue(new Callback<DashBoardResponse>() {
            @Override
            public void onResponse(Call<DashBoardResponse> call, Response<DashBoardResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DashBoardResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                Toasty.error(context, "" + t.getMessage(), Toasty.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<DashboardDataResponse> dashboardData(FragmentActivity context) {
        MutableLiveData<DashboardDataResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<DashboardDataResponse> call = RetrofitClient.getInstance().getApi()
                .dashBoardData(token,userId, vendorID);
        call.enqueue(new Callback<DashboardDataResponse>() {
            @Override
            public void onResponse(Call<DashboardDataResponse> call, Response<DashboardDataResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardDataResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthIodizationResponse> lastMontIodizationList(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<LastMonthIodizationResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthIodizationResponse> call = RetrofitClient.getInstance().getApi()
                .lastMontIodizationList(token,userId, vendorID,startDate,endDate,zone);
        call.enqueue(new Callback<LastMonthIodizationResponse>() {
            @Override
            public void onResponse(Call<LastMonthIodizationResponse> call, Response<LastMonthIodizationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthIodizationResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthIodineStockResponse> lastMontStock(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<LastMonthIodineStockResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthIodineStockResponse> call = RetrofitClient.getInstance().getApi()
                .lastMontStock(token,userId, vendorID,startDate,endDate,zone);
        call.enqueue(new Callback<LastMonthIodineStockResponse>() {
            @Override
            public void onResponse(Call<LastMonthIodineStockResponse> call, Response<LastMonthIodineStockResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthIodineStockResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthQcQaResponse> lastMontQCQA(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<LastMonthQcQaResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthQcQaResponse> call = RetrofitClient.getInstance().getApi()
                .lastMontQCQA(token,userId, vendorID,startDate,endDate,zone);
        call.enqueue(new Callback<LastMonthQcQaResponse>() {
            @Override
            public void onResponse(Call<LastMonthQcQaResponse> call, Response<LastMonthQcQaResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthQcQaResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<ZoneWiseMonitoringResponse> zoneWiseMonitoringList(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<ZoneWiseMonitoringResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        progressDialog.show();

        Call<ZoneWiseMonitoringResponse> call = RetrofitClient.getInstance().getApi()
                .zoneWiseMonitoringList(token,userId, vendorID,startDate,endDate,zone);
        call.enqueue(new Callback<ZoneWiseMonitoringResponse>() {
            @Override
            public void onResponse(Call<ZoneWiseMonitoringResponse> call, Response<ZoneWiseMonitoringResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ZoneWiseMonitoringResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<AgencyMonitoringResponse> agencyMonitoringlist(FragmentActivity context,String startDate,String endDate) {
        MutableLiveData<AgencyMonitoringResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<AgencyMonitoringResponse> call = RetrofitClient.getInstance().getApi()
                .agencyMonitoringlist(token,userId, vendorID,startDate,endDate);
        call.enqueue(new Callback<AgencyMonitoringResponse>() {
            @Override
            public void onResponse(Call<AgencyMonitoringResponse> call, Response<AgencyMonitoringResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<AgencyMonitoringResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }

    public MutableLiveData<IssueMonitoringResponse> issueMonitoringList(FragmentActivity context  ,String startDate,String endDate) {
        MutableLiveData<IssueMonitoringResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<IssueMonitoringResponse> call = RetrofitClient.getInstance().getApi()
                .issueMonitoringList(token,userId, vendorID,startDate,endDate);
        call.enqueue(new Callback<IssueMonitoringResponse>() {
            @Override
            public void onResponse(Call<IssueMonitoringResponse> call, Response<IssueMonitoringResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<IssueMonitoringResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthSaleResponse> lastMonthsale(FragmentActivity context,String startDate,String endDate) {
        MutableLiveData<LastMonthSaleResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthSaleResponse> call = RetrofitClient.getInstance().getApi()
                .lastMonthsale(token,userId, vendorID,startDate,endDate);
        call.enqueue(new Callback<LastMonthSaleResponse>() {
            @Override
            public void onResponse(Call<LastMonthSaleResponse> call, Response<LastMonthSaleResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthSaleResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthSaleResponse> lastProduction(FragmentActivity context,String startDate,String endDate) {
        MutableLiveData<LastMonthSaleResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthSaleResponse> call = RetrofitClient.getInstance().getApi()
                .lastProduction(token,userId, vendorID,startDate,endDate);
        call.enqueue(new Callback<LastMonthSaleResponse>() {
            @Override
            public void onResponse(Call<LastMonthSaleResponse> call, Response<LastMonthSaleResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthSaleResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<LastMonthSaleResponse> lastPurchase(FragmentActivity context,String startDate,String endDate) {
        MutableLiveData<LastMonthSaleResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<LastMonthSaleResponse> call = RetrofitClient.getInstance().getApi()
                .lastPurchase(token,userId, vendorID,startDate,endDate);
        call.enqueue(new Callback<LastMonthSaleResponse>() {
            @Override
            public void onResponse(Call<LastMonthSaleResponse> call, Response<LastMonthSaleResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<LastMonthSaleResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<TopTenMillerResponse> topTenMillList(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<TopTenMillerResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<TopTenMillerResponse> call = RetrofitClient.getInstance().getApi()
                .topTenMillList(token,userId, vendorID,startDate,endDate,zone);
        call.enqueue(new Callback<TopTenMillerResponse>() {
            @Override
            public void onResponse(Call<TopTenMillerResponse> call, Response<TopTenMillerResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<TopTenMillerResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<JaninaResponse> janinaList(FragmentActivity context,String startDate,String endDate,String zone) {
        MutableLiveData<JaninaResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<JaninaResponse> call = RetrofitClient.getInstance().getApi()
                .janinaList(token,userId, vendorID,  startDate,  endDate,  zone);
        call.enqueue(new Callback<JaninaResponse>() {
            @Override
            public void onResponse(Call<JaninaResponse> call, Response<JaninaResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<JaninaResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                 progressDialog.dismiss();
            }
        });
        return liveData;

    }
    public MutableLiveData<PieChartDataresponse> pieChartData(FragmentActivity context) {
        MutableLiveData<PieChartDataresponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();

        Call<PieChartDataresponse> call = RetrofitClient.getInstance().getApi()
                .pieChartData(token,userId, vendorID);
        call.enqueue(new Callback<PieChartDataresponse>() {
            @Override
            public void onResponse(Call<PieChartDataresponse> call, Response<PieChartDataresponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PieChartDataresponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
        return liveData;

    }
}
