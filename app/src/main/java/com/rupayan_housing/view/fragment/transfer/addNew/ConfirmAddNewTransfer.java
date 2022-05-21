package com.rupayan_housing.view.fragment.transfer.addNew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ConfirmNewSaleSelectedProductListAdapter;
import com.rupayan_housing.databinding.FragmentConfirmAddNewTransferBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.EnterpriseList;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.serverResponseModel.GetEnterpriseResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.AddNewTransferViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ConfirmAddNewTransfer extends BaseFragment {

    private FragmentConfirmAddNewTransferBinding binding;
    private SaleViewModel saleViewModel;
    private AddNewTransferViewModel addNewTransferViewModel;
    private SalesRequisitionViewModel salesRequisitionViewModel;

    private MyDatabaseHelper myDatabaseHelper;

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

    /**
     * Store previous fragment Data
     */
    private String selectedEnterpriseId, selectedStoreId;
    private String currentSelectedEnterpriseId, currentStoreId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_add_new_transfer, container, false);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        addNewTransferViewModel = new ViewModelProvider(this).get(AddNewTransferViewModel.class);
        salesRequisitionViewModel = new ViewModelProvider(this).get(SalesRequisitionViewModel.class);
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        binding.toolbar.toolbarTitle.setText("Confirm Transfer");
        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        getDataFromPreviousFragment();
        /**
         * show quantity updated product list
         */
        showSelectedDataToRecyclerView();
        /**
         * now get current enterprise and store list from server
         */
        getEnterPriseListFromServer();
        //nowSetStoreListByEnterPriseId();

        binding.enterPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    currentSelectedEnterpriseId = enterpriseResponseList.get(position).getStoreID();
                    binding.enterPrice.setEnableErrorLabel(false);
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                    return;
                }
                /**
                 * now get current selected store list by selected enterprise id
                 */
                nowSetStoreListByEnterPriseId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.store.setOnEmptySpinnerClickListener(() -> {
            hideKeyboard(getActivity());
            infoMessage(getActivity().getApplication(), "Select Enterprise first");
        });
        binding.store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentSelectedEnterpriseId == null) {
                    infoMessage(getActivity().getApplication(), "Select Enterprise first");
                    return;
                }
                try {
                    currentStoreId = storeResponseList.get(position).getStoreID();
                    binding.store.setEnableErrorLabel(false);
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * for submit
         */
        binding.save.setOnClickListener(v -> {
            if (currentSelectedEnterpriseId == null) {
                infoMessage(getActivity().getApplication(), "Please select enterprise");
                return;
            }
            if (currentStoreId == null) {
                infoMessage(getActivity().getApplication(), "Please select store");
                return;
            }
            hideKeyboard(getActivity());
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            confirmAddTransferDialog();
        });
        return binding.getRoot();
    }

    private void validationAndSave() {
        /**
         * for set updated productIdList,unitList
         */
        Set<Integer> proDuctIdList = new HashSet<>();
        List<String> unitList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<Integer> quantitySet = new ArrayList<>();
        quantitySet.clear();

        List<Double> buyingPriceSet = new ArrayList<>();
        buyingPriceSet.clear();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        for (int i = 0; i < AddNewTransfer.updatedQuantityProductList.size(); i++) {
            proDuctIdList.add(Integer.parseInt(AddNewTransfer.updatedQuantityProductList.get(i).getProductID()));
            unitList.add(AddNewTransfer.updatedQuantityProductList.get(i).getUnit());
            productTitleList.add(AddNewTransfer.updatedQuantityProductList.get(i).getProductTitle());
            buyingPriceSet.add(0.0);
        }
        for (int i = 0; i < AddNewTransfer.updatedQuantityProductList.size(); i++) {
            quantitySet.add(Integer.parseInt(AddNewTransfer.updatedQuantityProductList.get(i).getQuantity()));
        }


        Set<Integer> selectedStoreIdSet = new HashSet<>();

        selectedStoreIdSet.clear();
        selectedStoreIdSet.add(Integer.parseInt(selectedStoreId));

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        addNewTransferViewModel.addNewTransfer(getActivity(), selectedStoreIdSet,
                proDuctIdList, currentStoreId, quantitySet, buyingPriceSet,
                unitList, productTitleList, selectedEnterpriseId, currentSelectedEnterpriseId,
                binding.note.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    getActivity().onBackPressed();

                    /**
                     * after successfully add new sale to server delete all data from local DB
                     */
                    AddNewTransfer.sharedPreferenceForStore.deleteData();
                    myDatabaseHelper.deleteAllData();//delete all data from local database
                    getActivity().onBackPressed();
                });


    }

    private void showSelectedDataToRecyclerView() {
        List<SalesRequisitionItems> selectedItem = new ArrayList<>();
        selectedItem.addAll(AddNewTransfer.updatedQuantityProductList);//get static data from (AddNewSale) Fragment
        /**
         * set set selected data to recyclerview
         */
        ConfirmNewSaleSelectedProductListAdapter adapter = new ConfirmNewSaleSelectedProductListAdapter(getActivity(), selectedItem);
        binding.selectedProductsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.selectedProductsRv.setAdapter(adapter);
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        selectedEnterpriseId = getArguments().getString("selectedEnterpriseId");
        selectedStoreId = getArguments().getString("selectedStoreId");
    }

    private void getEnterPriseListFromServer() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        salesRequisitionViewModel.getEnterpriseResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
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

        for (int i = 0; i < response.getEnterprise().size(); i++) {
            enterpriseNameList.add("" + response.getEnterprise().get(i).getStoreName());
        }
        binding.enterPrice.setItem(enterpriseNameList);
        if (enterpriseResponseList.size() == 1) {
            binding.enterPrice.setSelection(0);
            currentSelectedEnterpriseId = enterpriseResponseList.get(0).getStoreID();
        }
    }

    private void nowSetStoreListByEnterPriseId() {
        saleViewModel.getStoreListByOptionalEnterpriseId(getActivity(), currentSelectedEnterpriseId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    storeResponseList = new ArrayList<>();
                    storeResponseList.clear();
                    storeNameList = new ArrayList<>();
                    storeNameList.clear();

                    for (int i = 0; i < response.getEnterprise().size(); i++) {
                        if (!(response.getEnterprise().get(i).getStoreID().equals(selectedStoreId))) {//remove previous selected store from store list
                            storeResponseList.add(response.getEnterprise().get(i));
                            storeNameList.add(response.getEnterprise().get(i).getStoreName());
                        }
                    }
                    binding.store.setItem(storeNameList);

                    if (storeResponseList.size() == 1) {
                        binding.store.setSelection(0);
                        currentStoreId = storeResponseList.get(0).getStoreID();
                    }
                });
    }

    public void confirmAddTransferDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to transfer ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.app_logo));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            validationAndSave();
        });

        alertDialog.show();
    }


}