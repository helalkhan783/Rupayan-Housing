package com.rupayan_housing.viewModel;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.CustomerSearchResponse;
import com.rupayan_housing.serverResponseModel.DueOrdersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DueCollectionViewModel extends ViewModel {
    private MutableLiveData<CustomerSearchResponse> customerList;
    private MutableLiveData<DueOrdersResponse> getDueOrders;
    private MutableLiveData<Double> getCurrentDue;

    public DueCollectionViewModel() {
        customerList = new MutableLiveData<>();
        getDueOrders = new MutableLiveData<>();
        getCurrentDue = new MutableLiveData<>();
    }


    public MutableLiveData<CustomerSearchResponse> getCustomerList() {
        if (customerList != null) {
            return customerList;
        }
        return null;
    }

    /**
     * for get selected customer Orders with lots of information by selected customer and vendor id.
     *
     * @return
     */
    public MutableLiveData<DueOrdersResponse> getGetDueOrders() {
        if (getDueOrders != null) {
            return getDueOrders;
        }
        return null;
    }


    public MutableLiveData<Double> getCurrentDue(double totalDue, double payDueAmount) {
        if (totalDue >= 1 && payDueAmount <= totalDue) {
            double currentDue = totalDue - payDueAmount;
            getCurrentDue.postValue(currentDue);
            return getCurrentDue;
        }
        return null;
    }





    public void apiCallForGetSupplier(FragmentActivity context, String token, String vendorID, String key) {
        Call<CustomerSearchResponse> call = RetrofitClient.getInstance().getApi().getSuppliersByKey(token, vendorID, key);
        call.enqueue(new Callback<CustomerSearchResponse>() {
            @Override
            public void onResponse(Call<CustomerSearchResponse> call, Response<CustomerSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        customerList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSearchResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public void apiCallForGetRefPerson(FragmentActivity context, String token, String vendorID, String key) {
        Call<CustomerSearchResponse> call = RetrofitClient.getInstance().getApi().getRefPersonByKey(token, vendorID, key);
        call.enqueue(new Callback<CustomerSearchResponse>() {
            @Override
            public void onResponse(Call<CustomerSearchResponse> call, Response<CustomerSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        customerList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSearchResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    /**
     * for get selected customer information
     */
    public void apiCallForGetCustomers(FragmentActivity context, String token, String vendorID, String key) {
        Call<CustomerSearchResponse> call = RetrofitClient.getInstance().getApi().getCustomersByKey(token, vendorID, key);
        call.enqueue(new Callback<CustomerSearchResponse>() {
            @Override
            public void onResponse(Call<CustomerSearchResponse> call, Response<CustomerSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response == null) {
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        customerList.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerSearchResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }












    /**
     * for get selected customer Orders with lots of information by selected customer and vendor id.
     *
     * @param context
     * @param selectedCustomerId
     * @param vendorId
     */
    public void apiCallForgetDueOrdersBySelectedCustomerIdAndVendorId(FragmentActivity context, String selectedCustomerId, String vendorId) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        String currentUserToken = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        Call<DueOrdersResponse> call = RetrofitClient.getInstance().getApi().dueOrdersByCustomerId(currentUserToken, vendorId, selectedCustomerId);
        call.enqueue(new Callback<DueOrdersResponse>() {
            @Override
            public void onResponse(Call<DueOrdersResponse> call, Response<DueOrdersResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus() == 200) {
                        getDueOrders.postValue(response.body());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DueOrdersResponse> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something Wrong Contact to Support \n" + this.getClass().getSimpleName() + " " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
