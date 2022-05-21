package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DeclinedRequisitionListResponse;
import com.rupayan_housing.serverResponseModel.DeclinedRequisitionResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclinedRequisitionListViewModel extends ViewModel {
    private ProgressDialog progressDialog;
    public DeclinedRequisitionListViewModel() {
    }

    /**
     * for get sales declined requisition list
     */
    public MutableLiveData<List<DeclinedRequisitionListResponse>> getDeclinedRequisitionList(FragmentActivity context) {
        MutableLiveData<List<DeclinedRequisitionListResponse>> liveData = new MutableLiveData<>();
        progressDialog = new ProgressDialog(context);
        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorID = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        progressDialog.show();
        Call<DeclinedRequisitionResponse> call = RetrofitClient.getInstance().getApi().getDeclinedRequisitionList(token, vendorID, "", "", "", "");
        call.enqueue(new Callback<DeclinedRequisitionResponse>() {
            @Override
            public void onResponse(@NotNull Call<DeclinedRequisitionResponse> call, @NotNull Response<DeclinedRequisitionResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body().getLists());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<DeclinedRequisitionResponse> call, @NotNull Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return liveData;
    }
}
