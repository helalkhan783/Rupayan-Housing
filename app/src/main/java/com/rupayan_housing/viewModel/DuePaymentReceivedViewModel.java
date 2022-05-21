package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.AccountNumberListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuePaymentReceivedViewModel extends ViewModel {
    private MutableLiveData<AccountNumberListResponse> accountNumberListResponseMutableLiveData;

    public DuePaymentReceivedViewModel() {
        accountNumberListResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AccountNumberListResponse> getAccountListByBankId(){
        return accountNumberListResponseMutableLiveData;
    }

    public void apiCallForGetAccountListByBankNameId(FragmentActivity context,String vendorID, String bankNameId) {
         ProgressDialog progressDialog = new ProgressDialog(context);
         progressDialog.show();
        Call<AccountNumberListResponse> call = RetrofitClient.getInstance().getApi().getAccountListByBankId(vendorID, bankNameId);
        call.enqueue(new Callback<AccountNumberListResponse>() {
            @Override
            public void onResponse(Call<AccountNumberListResponse> call, Response<AccountNumberListResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        progressDialog.dismiss();
                        accountNumberListResponseMutableLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountNumberListResponse> call, Throwable t) {
                Log.d("ERROR",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
