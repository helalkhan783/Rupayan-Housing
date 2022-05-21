package com.rupayan_housing.viewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;

import java.util.Set;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayDueAmountViewModel extends ViewModel {
    public PayDueAmountViewModel() {
    }

    /**
     * api call for due payment for current user selected user
     */
    public void apiCallForPayDueAmount(FragmentActivity context, String vendorId, Set<String> orders, String collectedPaidAmount, String totalDuee, String storeId, String userId, String permissions,
                                       String profileTypeId, String paymentTypeVal, String paymentSubType, String bankId, String date, String payment_remarks) {

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .payDuyAmount(
                        PreferenceManager.getInstance(context).getUserCredentials().getToken(),
                        vendorId, orders, collectedPaidAmount, totalDuee, storeId, userId, permissions,
                        profileTypeId, paymentTypeVal, paymentSubType, bankId, date, payment_remarks
                );
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        Log.d("ok", "ok");
                        Toasty.success(context, "" + response.body().getMessage(), Toasty.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
