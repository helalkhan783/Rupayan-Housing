package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.EditedPurchaseOrderResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSalesViewModel extends ViewModel {
    public EditSalesViewModel() {
    }


    public MutableLiveData<EditedPurchaseOrderResponse> getEditableSalesDetails(FragmentActivity context, String orderId) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        MutableLiveData<EditedPurchaseOrderResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        progressDialog.show();
        Call<EditedPurchaseOrderResponse> call = RetrofitClient.getInstance().getApi()
                .getSalesEditDetails(token, vendorId, orderId);
        call.enqueue(new Callback<EditedPurchaseOrderResponse>() {
            @Override
            public void onResponse(Call<EditedPurchaseOrderResponse> call, Response<EditedPurchaseOrderResponse> response) {
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
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditedPurchaseOrderResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return liveData;
    }
}
