package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PendingExpenseDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseViewModel extends ViewModel {

    public MutableLiveData<PendingExpenseDetails> getPendingExpenseDetails(FragmentActivity context, String id) {
        MutableLiveData<PendingExpenseDetails> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();

        Call<PendingExpenseDetails> call = RetrofitClient.getInstance().getApi()
                .getPendingExpenseDetails(token, vendorId, id);
        Log.d("TYPE", id);
        call.enqueue(new Callback<PendingExpenseDetails>() {
            @Override
            public void onResponse(Call<PendingExpenseDetails> call, Response<PendingExpenseDetails> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        Log.d("TYPE", new Gson().toJson(response.body()));
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingExpenseDetails> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return liveData;
    }
}
