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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WashingAndCrushingViewmodel extends ViewModel {
    public WashingAndCrushingViewmodel() {
    }


    public MutableLiveData<DuePaymentResponse> approveWashingAndCrushing(FragmentActivity context, String orderId, String note) {

        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().approveWashingAndCrushing(
                token, vendorId, orderId, userId, note);

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    liveData.postValue(response.body());
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                liveData.postValue(null);
            }
        });

        return liveData;
    }


    public MutableLiveData<DuePaymentResponse> declineWashingAndCrushing(
            FragmentActivity context, String orderId, String note
    ) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        Call<DuePaymentResponse> call =
                RetrofitClient.getInstance().getApi()
                        .declineWashingAndCrushing(token, vendorId, orderId, userId, note);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        liveData.postValue(null);
                        return;
                    }
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", "" + t.getLocalizedMessage());
            }
        });
        return liveData;
    }
}
