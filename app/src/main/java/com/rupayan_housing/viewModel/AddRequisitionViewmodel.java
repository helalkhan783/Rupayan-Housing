package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRequisitionViewmodel extends ViewModel {
    private ProgressDialog progressDialog;
    MutableLiveData<DuePaymentResponse> duePaymentResponseMutableLiveData;

    public AddRequisitionViewmodel() {
        duePaymentResponseMutableLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<DuePaymentResponse> isAddRequisitionSuccessful() {
        return duePaymentResponseMutableLiveData;
    }


    public void apiCallForCreateRequisition(FragmentActivity context,
                                            List<String> productIdList, String enterPriceId, List<String> unitList, String lastUserId, String findingCustomerId,
                                            List<String> productTitleList, List<String> sellingPriceList, List<String> quantityList, String totalDiscount,
                                            String collectedAmount, String paymentTypeVal,
                                            String startOrderDate, String endOrderDate) {

        progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String storeAccess = String.valueOf(PreferenceManager.getInstance(context).getUserCredentials().getStoreAccess());
        String profile_type_id = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        String storeId = PreferenceManager.getInstance(context).getUserCredentials().getStoreID();

        progressDialog.show();
        /**
         * here isConfirm 0 byDefault provide from backend (will change)
         */
        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().addRequisition(
                token, profile_type_id, vendorId, storeId, productIdList, enterPriceId, unitList, lastUserId,
                userId, findingCustomerId, productTitleList, sellingPriceList, quantityList, totalDiscount, "0",
                collectedAmount, "0", paymentTypeVal, startOrderDate, endOrderDate
        );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                Toasty.info(context, response.body().getMessage(), Toasty.LENGTH_LONG).show();
                Log.d("SMS", response.body().getMessage());
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        Log.d("SUCCESS", "successful");
                        duePaymentResponseMutableLiveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
