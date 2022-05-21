package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.AddNewPaymentLimitResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewLimitInstructionViewModel extends ViewModel {
    private final MutableLiveData<AddNewPaymentLimitResponse> addNewPaymentInstructionList;

    public AddNewLimitInstructionViewModel() {
        addNewPaymentInstructionList = new MutableLiveData<>();
    }


    public MutableLiveData<AddNewPaymentLimitResponse> getAddNewPaymentInstructionList() {
        if (addNewPaymentInstructionList != null) {
            return addNewPaymentInstructionList;
        }
        return null;
    }


    /**
     * for get add new payment instruction list
     *
     * @param //vendorID
     * @param //date
     */
    public void apiCallForSubmitNewPaymentLimit(FragmentActivity context, List<String> paymentLimitArray, List<String> customerIdArray, String userId, String vendorId, String storeId, String note, String paymentDate) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        Call<AddNewPaymentLimitResponse> call = RetrofitClient.getInstance().getApi().submitNewPaymentLimit(paymentLimitArray, customerIdArray, userId, vendorId, storeId, note, paymentDate);
        call.enqueue(new Callback<AddNewPaymentLimitResponse>() {
            @Override
            public void onResponse(@NotNull Call<AddNewPaymentLimitResponse> call, @NotNull Response<AddNewPaymentLimitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        progressDialog.dismiss();
                        addNewPaymentInstructionList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<AddNewPaymentLimitResponse> call, @NotNull Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
