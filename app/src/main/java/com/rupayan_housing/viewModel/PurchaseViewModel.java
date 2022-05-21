package com.rupayan_housing.viewModel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.retrofit.RetrofitClient;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseViewModel extends ViewModel {

    public MutableLiveData<DuePaymentResponse> newPurchase(
            FragmentActivity context, String selectedEnterpriseId, String lastOrderId,
            String selectedCustomerId, List<String> productIdList, List<String> storeIdList,
            List<String> quantityList, List<String> unitList, List<String> productTitleList,
            String personName, String personPhone, String vehicleNo, String transportName
    ) {
        MutableLiveData<DuePaymentResponse> liveData = new MutableLiveData<>();


        String token = PreferenceManager.getInstance(context).getUserCredentials().getToken();
        String vendorId = PreferenceManager.getInstance(context).getUserCredentials().getVendorID();
        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        String userId = PreferenceManager.getInstance(context).getUserCredentials().getUserId();


        List<String> totalDiscountList = new ArrayList<>();
        totalDiscountList.clear();
        List<String> productVatList = new ArrayList<>();
        productVatList.clear();
        List<String> buyingPriceList = new ArrayList<>();
        buyingPriceList.clear();
        List<String> storeIdLists = new ArrayList<>();
        storeIdLists.clear();

        for (int i = 0; i < productIdList.size(); i++) {
            totalDiscountList.add("0");
            productVatList.add("0");
            buyingPriceList.add("0");
            storeIdLists.add(storeIdList.get(0));
        }
        /**
         * set current date will send input from user
         */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);
        /*System.out.println(dtf.format(now));*/

        Call<DuePaymentResponse> call = RetrofitClient.getInstance().getApi().addNewPurchase(
                token, profileTypeId, vendorId,
                selectedEnterpriseId, lastOrderId, userId, selectedCustomerId, productIdList, storeIdLists, quantityList,
                unitList, buyingPriceList, productTitleList, totalDiscountList, productVatList, "1"
                , "0", "0", "0", "0", "0", currentDate, personName,
                personPhone, vehicleNo, transportName, "0", "0", "0", "0", ""
        );

        call.enqueue(new Callback<DuePaymentResponse>() {
            @Override
            public void onResponse(Call<DuePaymentResponse> call, Response<DuePaymentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        liveData.postValue(null);
                        return;
                    }
                    if (response.body().getStatus() == 400) {
                        liveData.postValue(response.body());
                        return;
                    }
                    if (response.body().getStatus() == 200) {
                        liveData.postValue(response.body());
                        return;

                    } else {
                        liveData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<DuePaymentResponse> call, Throwable t) {
                liveData.postValue(null);
                Log.d("ERROR", "ERROR");
            }
        });

        return liveData;
    }
}
