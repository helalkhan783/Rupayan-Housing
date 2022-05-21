package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PendingRequisitionListResponse;
import com.rupayan_housing.serverResponseModel.PendingRequisitionPageInfoResponse;
import com.rupayan_housing.serverResponseModel.PendingRequisitionResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingRequisitionListViewModel extends ViewModel {
    public PendingRequisitionListViewModel() {
    }

    public MutableLiveData<PendingRequisitionPageInfoResponse> getPenReqDetailsPageInfo(FragmentActivity context) {
        MutableLiveData<PendingRequisitionPageInfoResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String store_access = String.valueOf(PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess());
        if (store_access.equals("null")) {//only for admin  like jahangir kobir
            store_access = null;
        }
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        Call<PendingRequisitionPageInfoResponse> call = RetrofitClient.getInstance().getApi()
                .getPendingRequisitionPageInfo(token, vendorID, store_access);

        call.enqueue(new Callback<PendingRequisitionPageInfoResponse>() {
            @Override
            public void onResponse(Call<PendingRequisitionPageInfoResponse> call, Response<PendingRequisitionPageInfoResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingRequisitionPageInfoResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return liveData;
    }


    /**
     * for get pending sales requisition list
     */
    public MutableLiveData<List<PendingRequisitionListResponse>> getPendingRequisitionList(FragmentActivity context, String storeId, String startDate, String endDate, String companyId) {
        MutableLiveData<List<PendingRequisitionListResponse>> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);


        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();
        Call<PendingRequisitionResponse> call = RetrofitClient.getInstance().getApi().getPendingRequisitionList(token, vendorID, storeId, startDate, endDate, companyId);
        call.enqueue(new Callback<PendingRequisitionResponse>() {
            @Override
            public void onResponse(@NotNull Call<PendingRequisitionResponse> call, @NotNull Response<PendingRequisitionResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        progressDialog.dismiss();
                        liveData.postValue(response.body().getLists());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<PendingRequisitionResponse> call, @NotNull Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });

        return liveData;
    }
}
