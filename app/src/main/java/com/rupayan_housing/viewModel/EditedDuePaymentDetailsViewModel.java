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
import com.rupayan_housing.serverResponseModel.EditedPaymentDueResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
public class EditedDuePaymentDetailsViewModel extends ViewModel {


    /**
     * For decline edited payment
     */

    public MutableLiveData<DuePaymentResponse> declineEditedPayment(FragmentActivity context,String id,String note){
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();
        ProgressDialog progressDialog= new ProgressDialog(context);


        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userID = PreferenceManager.getInstance(context).getUserCredentials().getUserId();

        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .declineEditedPendingPayment(token,vendorId,id,userID,note);
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
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




        return liveData;
    }


    /**
     * For approve edited payment
     */


    public MutableLiveData<DuePaymentResponse> approveEditedPayment(FragmentActivity context, String id,String note) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();


        ProgressDialog progressDialog = new ProgressDialog(context);

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();
        progressDialog.show();


        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi()
                .approveEditedPayment(token, vendorId, id, userId,note);
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


    /**
     * For get EditedPaymentDueDetails
     */
    public MutableLiveData<EditedPaymentDueResponse> getEditedPaymentDueDetails(FragmentActivity context, String orderId) {

        MutableLiveData<EditedPaymentDueResponse> liveData = new MutableLiveData<>();

        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();


        Call<EditedPaymentDueResponse> call = RetrofitClient.getInstance().getApi()
                .getEditedPaymentDueDetailsResponseCall(token, vendorId, orderId);
        call.enqueue(new Callback<EditedPaymentDueResponse>() {
            @Override
            public void onResponse(Call<EditedPaymentDueResponse> call, Response<EditedPaymentDueResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<EditedPaymentDueResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return liveData;

    }


}
