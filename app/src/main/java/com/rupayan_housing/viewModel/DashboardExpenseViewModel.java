package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DashBoardExpenseResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class DashboardExpenseViewModel extends ViewModel {


    public MutableLiveData<DashBoardExpenseResponse> getDashboardExpenseList(FragmentActivity context) {
        MutableLiveData<DashBoardExpenseResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();


        progressDialog.show();
        Call<DashBoardExpenseResponse> call = RetrofitClient.getInstance().getApi()
                .dashBoardExpenseList(token, vendorId);
        call.enqueue(new Callback<DashBoardExpenseResponse>() {
            @Override
            public void onResponse(Call<DashBoardExpenseResponse> call, Response<DashBoardExpenseResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }


            }

            @Override
            public void onFailure(Call<DashBoardExpenseResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });


        return liveData;

    }
}
