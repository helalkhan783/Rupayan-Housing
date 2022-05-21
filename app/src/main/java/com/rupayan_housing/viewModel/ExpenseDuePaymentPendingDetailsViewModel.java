package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.PendingExpensePaymentDueResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class ExpenseDuePaymentPendingDetailsViewModel extends ViewModel {

    public MutableLiveData<DuePaymentResponse> declineExpenseDuePaymentApprovalDetails(FragmentActivity context,String batch) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog  = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .declineDuePaymentApprovalDetails(token,vendorId,batch,userId);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()){
                    assert response.body()!=null;
                    if (response.body().getStatus()==200){
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ERROR",t.getMessage());
            }
        });
        return liveData;
    }








    public MutableLiveData<DuePaymentResponse> approveExpenseDuePaymentApprovalDetails(FragmentActivity context,String batch) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();

        ProgressDialog progressDialog  = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .approveDuePaymentApprovalDetails(token,vendorId,batch,userId);
        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()){
                    assert response.body()!=null;
                    if (response.body().getStatus()==200){
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("ERROR",t.getMessage());
            }
        });
        return liveData;
    }


    public MutableLiveData<PendingExpensePaymentDueResponse> getExpenseDuePaymentApprovalDetails(FragmentActivity context, String batch, String customerId) {
        MutableLiveData<PendingExpensePaymentDueResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();

        Call<PendingExpensePaymentDueResponse> call = RetrofitClient.getInstance().getApi()
                .getExpenseDuePaymentApprovalDetails(token, vendorID, batch, customerId);

        call.enqueue(new Callback<PendingExpensePaymentDueResponse>() {
            @Override
            public void onResponse(Call<PendingExpensePaymentDueResponse> call, Response<PendingExpensePaymentDueResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<PendingExpensePaymentDueResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });

        return liveData;
    }
}
