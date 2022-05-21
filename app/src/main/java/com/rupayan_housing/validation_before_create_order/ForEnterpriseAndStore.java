package com.rupayan_housing.validation_before_create_order;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.rupayan_housing.permission.SharedPreferenceForStore;
import com.rupayan_housing.serverResponseModel.EnterpriseList;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.serverResponseModel.GetEnterpriseResponse;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ForEnterpriseAndStore {
    FragmentActivity context;
    SmartMaterialSpinner enterPrice, store;
    private SalesRequisitionViewModel salesRequisitionViewModel;
    private SaleViewModel saleViewModel;

    LifecycleOwner viewLifecycleOwner;
   public SharedPreferenceForStore sharedPreferenceForStore;
    String enterpriseId,storeId;
    /**
     * For enterprise
     */
    List<EnterpriseResponse> enterpriseResponseList;
    List<String> enterpriseNameList;
    /**
     * for store
     */
    List<EnterpriseList> storeResponseList;
    List<String> storeNameList;

    public ForEnterpriseAndStore(FragmentActivity context, SmartMaterialSpinner enterPrice, SmartMaterialSpinner store, LifecycleOwner viewLifecycleOwner) {
        this.context = context;
        this.enterPrice = enterPrice;
        this.store = store;
        this.viewLifecycleOwner = viewLifecycleOwner;
        salesRequisitionViewModel = new ViewModelProvider(context).get(SalesRequisitionViewModel.class);
        saleViewModel = new ViewModelProvider(context).get(SaleViewModel.class);
        sharedPreferenceForStore = new SharedPreferenceForStore(context);
    }

    public void getPageDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        salesRequisitionViewModel.getEnterpriseResponse(context)
                .observe(viewLifecycleOwner, response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        Toasty.info(context.getApplication(), "Something Wrong", Toasty.LENGTH_LONG).show();

                        return;
                    }
                    if (response.getStatus() != 200) {
                        Toasty.info(context.getApplication(), response.getMessage(), Toasty.LENGTH_LONG).show();

                        return;
                    }
                    /**
                     * all Ok now set data to view
                     */
                    setPageData(response);
                });
    }

    private void setPageData(GetEnterpriseResponse response) {

        enterpriseResponseList = new ArrayList<>();
        enterpriseResponseList.clear();
        enterpriseNameList = new ArrayList<>();
        enterpriseNameList.clear();
        enterpriseResponseList.addAll(response.getEnterprise());

        try {
            for (int i = 0; i < response.getEnterprise().size(); i++) {
                enterpriseNameList.add("" + response.getEnterprise().get(i).getStoreName());
                if (sharedPreferenceForStore.getEnterpriseId() != null) {
                    if (sharedPreferenceForStore.getEnterpriseId().equals(response.getEnterprise().get(i).getStoreID())) {
                        enterPrice.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {
        }

        enterPrice.setItem(enterpriseNameList);
      /*  if (enterpriseResponseList.size() == 1) {
            enterPrice.setSelection(0);
            selectedEnterPrice = enterpriseResponseList.get(0).getStoreID();
        }*/
    }

    public String enterpriseID() {
        enterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  enterpriseId = enterpriseResponseList.get(position).getStoreID();
                  sharedPreferenceForStore.saveStoreId(enterpriseId,storeId);
                  nowSetStoreListByEnterPriseId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return enterpriseId;
    }

    public  String storeId(){
        store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeId = storeResponseList.get(position).getStoreID();
                sharedPreferenceForStore.saveStoreId(enterpriseId,storeId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return storeId;
    }


    private void nowSetStoreListByEnterPriseId() {
        saleViewModel.getStoreListByOptionalEnterpriseId(context, enterpriseId)
                .observe(viewLifecycleOwner, response -> {
                    if (response == null) {
                        Toasty.info(context.getApplication(), "Something Wrong", Toasty.LENGTH_LONG).show();

                         return;
                    }
                    if (response.getStatus() != 200) {
                        Toasty.info(context.getApplication(), "Something Wrong", Toasty.LENGTH_LONG).show();
                         return;
                    }

                    storeResponseList = new ArrayList<>();
                    storeResponseList.clear();
                    storeNameList = new ArrayList<>();
                    storeNameList.clear();

                    storeResponseList.addAll(response.getEnterprise());

                    for (int i = 0; i < response.getEnterprise().size(); i++) {
                        storeNameList.add(response.getEnterprise().get(i).getStoreName());
                        if (sharedPreferenceForStore.getStoreId() !=null){
                            if (sharedPreferenceForStore.getStoreId().equals(response.getEnterprise().get(i).getStoreID())){
                               store.setSelection(i);
                            }
                        }
                    }
                    store.setItem(storeNameList);

                   /* if (storeResponseList.size() == 1) {
                        store.setSelection(0);
                        selectedStore = storeResponseList.get(0).getStoreID();
                    }*/
                });
    }


}
