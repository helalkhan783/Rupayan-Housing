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
import com.rupayan_housing.serverResponseModel.EditedWashingCrushingDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditedWashingAndCurshingViewmodel extends ViewModel {
    public EditedWashingAndCurshingViewmodel() {
    }

    public MutableLiveData<DuePaymentResponse> declineEditedWashingCrushing(FragmentActivity context, String orderId,String note){
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();
        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .declineEditedWashingCrushing(token, vendorId, orderId,userId, note);
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



    public MutableLiveData<DuePaymentResponse> approveEditedWashingCrushing(FragmentActivity context, String orderId,String note) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();
        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .approveEditedWashingCrushing(token, vendorId, orderId,userId, note);
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


    public MutableLiveData<EditedWashingCrushingDetailsResponse> getEditedWashingAndCrushingDetails(FragmentActivity context, String orderId) {
        MutableLiveData<EditedWashingCrushingDetailsResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        progressDialog.show();

        Call<EditedWashingCrushingDetailsResponse> call =
                RetrofitClient.getInstance().getApi()
                        .getEditedWashingAndCrushingDetails(token, vendorId, orderId);

        call.enqueue(new Callback<EditedWashingCrushingDetailsResponse>() {
            @Override
            public void onResponse(Call<EditedWashingCrushingDetailsResponse> call, Response<EditedWashingCrushingDetailsResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditedWashingCrushingDetailsResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return liveData;
    }
}
