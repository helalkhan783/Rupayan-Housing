package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.PaymentInstruction;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentInstructionListViewModel extends ViewModel {
    private ProgressDialog progressDialog;
    private final MutableLiveData<PaymentInstruction> paymentInstructionList;

    public PaymentInstructionListViewModel() {
        paymentInstructionList = new MutableLiveData<>();
    }


    public MutableLiveData<PaymentInstruction> getPaymentInstructionList() {
        if (paymentInstructionList != null) {
            return paymentInstructionList;
        }
        return null;
    }


    /**
     * for get payment instruction list
     *
     * @param vendorID
     */
    public void apiCallForGetPaymentInstructionTotalList(FragmentActivity context,String vendorID) {

        progressDialog=  new ProgressDialog(context);
        progressDialog.show();

        Call<PaymentInstruction> call = RetrofitClient.getInstance().getApi().getPaymentInstructionList(vendorID);
        call.enqueue(new Callback<PaymentInstruction>() {
            @Override
            public void onResponse(@NotNull Call<PaymentInstruction> call, @NotNull Response<PaymentInstruction> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        paymentInstructionList.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<PaymentInstruction> call, @NotNull Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }

}
