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
import com.rupayan_housing.serverResponseModel.EditedIodizationDetailsResponse;

import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@NoArgsConstructor
public class EditedIodizationDetailsViewModel extends ViewModel {

    /***
     * For Approve pending iodization details
     */

    public MutableLiveData<DuePaymentResponse> approvePendingIodizationDetails(FragmentActivity context, String orderId, String note) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .approveIodizationPending(token, vendorId, orderId, note, userId);

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
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
        return liveData;
    }

    /**
     * For decline iodization pending
     */
    public MutableLiveData<DuePaymentResponse> declinePendingIodizationDetails(FragmentActivity context, String orderId, String note) {

        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .declineIodizationPending(token, vendorId, orderId, note, userId);

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
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
        return liveData;
    }


    public MutableLiveData<EditedIodizationDetailsResponse> getEditedEditedIodizationDetails(FragmentActivity context, String orderId) {

        MutableLiveData<EditedIodizationDetailsResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.show();
        Call<EditedIodizationDetailsResponse> call = RetrofitClient.getInstance().getApi()
                .getIodizationEditedOrders(token, vendorId, orderId);

        call.enqueue(new Callback<EditedIodizationDetailsResponse>() {
            @Override
            public void onResponse(Call<EditedIodizationDetailsResponse> call, Response<EditedIodizationDetailsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditedIodizationDetailsResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return liveData;
    }
}
