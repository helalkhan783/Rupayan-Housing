package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.RequisitionDetailsResponse;
import com.rupayan_housing.serverResponseModel.RequisitionListResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesRequisitionListViewModel extends ViewModel {
    private ProgressDialog progressDialog;
    public SalesRequisitionListViewModel() {
    }


    /**
     * for get single sale requisition details
     */
    public MutableLiveData<RequisitionDetailsResponse> getSingleRequisitionDetails(FragmentActivity context, String selectedProductId) {
        MutableLiveData<RequisitionDetailsResponse> liveData = new MutableLiveData<>();
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        Call<RequisitionDetailsResponse> call = RetrofitClient.getInstance().getApi()
                .getSingleRequisitionDetails(selectedProductId, token, vendorID);

        call.enqueue(new Callback<RequisitionDetailsResponse>() {
            @Override
            public void onResponse(Call<RequisitionDetailsResponse> call, Response<RequisitionDetailsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequisitionDetailsResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
        return liveData;
    }


    /**
     * for get sales requisition list
     *
     * @param context
     * @return
     */
    public MutableLiveData<List<SalesRequisitionListResponse>> getSalesRequisitionList(FragmentActivity context) {
        MutableLiveData<List<SalesRequisitionListResponse>> liveData = new MutableLiveData<>();

        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        /**
         * here store,start,end,company for future implement
         */

        Call<RequisitionListResponse> call = RetrofitClient.getInstance().getApi().getRequisitionList(token, vendorID, "", "", "", "");
        call.enqueue(new Callback<RequisitionListResponse>() {
            @Override
            public void onResponse(Call<RequisitionListResponse> call, Response<RequisitionListResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body().getLists());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequisitionListResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });

        return liveData;
    }
}
