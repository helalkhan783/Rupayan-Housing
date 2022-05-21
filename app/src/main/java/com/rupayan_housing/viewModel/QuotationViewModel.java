package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PendingQuotationResponse;

import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@NoArgsConstructor
public class QuotationViewModel extends ViewModel {

    public MutableLiveData<PendingQuotationResponse> getQuotationDetails(FragmentActivity context, String orderId) {
        MutableLiveData<PendingQuotationResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();

        Call<PendingQuotationResponse> call = RetrofitClient.getInstance().getApi()
                .getPendingQuotationDetails(token, orderId, vendorId);


        call.enqueue(new Callback<PendingQuotationResponse>() {
            @Override
            public void onResponse(Call<PendingQuotationResponse> call, Response<PendingQuotationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingQuotationResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return liveData;

    }
}
