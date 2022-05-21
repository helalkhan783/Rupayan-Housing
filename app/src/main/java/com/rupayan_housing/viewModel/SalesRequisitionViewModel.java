package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.GetEnterpriseResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItemListResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SealsRequisitionItemSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesRequisitionViewModel extends ViewModel {
    private ProgressDialog progressDialog;
    MutableLiveData<SealsRequisitionItemSearchResponse> sealsRequisitionItemSearchResponseMutableLiveData;
    MutableLiveData<List<SalesRequisitionItems>> salesRequisitionItemsMutableLiveData;

    public SalesRequisitionViewModel() {
        sealsRequisitionItemSearchResponseMutableLiveData = new MutableLiveData<>();
        salesRequisitionItemsMutableLiveData = new MutableLiveData<>();
    }
    /**
     * for get enterprise
     */
    public MutableLiveData<GetEnterpriseResponse> getEnterpriseResponse(FragmentActivity context) {
        MutableLiveData<GetEnterpriseResponse> getEnterpriseResponseMutableLiveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String storeAccess = String.valueOf(PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess());
        String profile_type_id = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String storeId = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();

        Call<GetEnterpriseResponse> call = RetrofitClient.getInstance().getApi().getEnterprise(token, vendorId, storeAccess, profile_type_id, storeId);
        call.enqueue(new Callback<GetEnterpriseResponse>() {
            @Override
            public void onResponse(Call<GetEnterpriseResponse> call, Response<GetEnterpriseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        getEnterpriseResponseMutableLiveData.postValue(null);
                        return;
                    }
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        getEnterpriseResponseMutableLiveData.postValue(response.body());

                    } else {
                        getEnterpriseResponseMutableLiveData.postValue(null);
                    }
                } else {
                    getEnterpriseResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetEnterpriseResponse> call, Throwable t) {
                getEnterpriseResponseMutableLiveData.postValue(null);
                Log.d("ERROR", t.getMessage());
            }
        });

        return getEnterpriseResponseMutableLiveData;
    }

    public MutableLiveData<GetEnterpriseResponse> forGetUserEnterprise(FragmentActivity context) {
        MutableLiveData<GetEnterpriseResponse> getEnterpriseResponseMutableLiveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String storeAccess = String.valueOf(PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess());
        String profile_type_id = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String storeId = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();

        Call<GetEnterpriseResponse> call = RetrofitClient.getInstance().getApi().forGetUserEnterprise(token, vendorId, storeAccess, profile_type_id, storeId);
        call.enqueue(new Callback<GetEnterpriseResponse>() {
            @Override
            public void onResponse(Call<GetEnterpriseResponse> call, Response<GetEnterpriseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        getEnterpriseResponseMutableLiveData.postValue(null);
                        return;
                    }
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        getEnterpriseResponseMutableLiveData.postValue(response.body());

                    } else {
                        getEnterpriseResponseMutableLiveData.postValue(null);
                    }
                } else {
                    getEnterpriseResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetEnterpriseResponse> call, Throwable t) {
                getEnterpriseResponseMutableLiveData.postValue(null);
                Log.d("ERROR", t.getMessage());
            }
        });

        return getEnterpriseResponseMutableLiveData;
    }


    public MutableLiveData<List<SalesRequisitionItems>> getSalesRequisitionItemList(FragmentActivity context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        Call<SalesRequisitionItemListResponse> call = RetrofitClient.getInstance().getApi()
                .getSalesRequisitionItemsList(token, vendorId);

        call.enqueue(new Callback<SalesRequisitionItemListResponse>() {
            @Override
            public void onResponse(Call<SalesRequisitionItemListResponse> call, Response<SalesRequisitionItemListResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        salesRequisitionItemsMutableLiveData.postValue(response.body().getItems());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<SalesRequisitionItemListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
        return salesRequisitionItemsMutableLiveData;
    }

    /**
     * get sales requisition items by searching Name
     */

    public MutableLiveData<SealsRequisitionItemSearchResponse> getSearchSalesRequisitionItemByName() {
        return sealsRequisitionItemSearchResponseMutableLiveData;
    }

    /**
     * api call for search sales requisition item by Name
     */

    public void apiCallForSearchSalesRequisitionItemByName(FragmentActivity context, List<String> categoryIDs, String searchKey) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        Call<SealsRequisitionItemSearchResponse> call =
                RetrofitClient.getInstance().getApi().searchSalesRequisitionItemByName(token, vendorId, categoryIDs, searchKey);
        call.enqueue(new Callback<SealsRequisitionItemSearchResponse>() {
            @Override
            public void onResponse(Call<SealsRequisitionItemSearchResponse> call, Response<SealsRequisitionItemSearchResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        sealsRequisitionItemSearchResponseMutableLiveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<SealsRequisitionItemSearchResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}
