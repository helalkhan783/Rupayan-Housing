package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CashBookResponse;
import com.rupayan_housing.serverResponseModel.DayBookResponse;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class DayBookCashBookViewModel extends ViewModel {


    /**
     * For get Cash book
     */
    public MutableLiveData<CashBookResponse> getCashResponse(FragmentActivity context) {
        MutableLiveData<CashBookResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();
        Call<CashBookResponse> call = RetrofitClient.getInstance().getApi()
                .getCashBookForHomePage(token, vendorID);

        call.enqueue(new Callback<CashBookResponse>() {
            @Override
            public void onResponse(Call<CashBookResponse> call, Response<CashBookResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<CashBookResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(context, t.getMessage(), Toasty.LENGTH_LONG).show();
                Log.d("ERROR", t.getMessage());
            }
        });
        return liveData;
    }


    /**
     * for get day book
     */
    public MutableLiveData<DayBookResponse> getDaybookResponse(FragmentActivity context) {
        MutableLiveData<DayBookResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();
        Call<DayBookResponse> call = RetrofitClient.getInstance().getApi()
                .getDayBookForHomePage(token, vendorID);

        call.enqueue(new Callback<DayBookResponse>() {
            @Override
            public void onResponse(Call<DayBookResponse> call, Response<DayBookResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DayBookResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(context, t.getMessage(), Toasty.LENGTH_LONG).show();
                Log.d("ERROR", t.getMessage());
            }
        });
        return liveData;
    }
}
