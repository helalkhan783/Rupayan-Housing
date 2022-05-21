package com.rupayan_housing.view.fragment.reconciliation.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ConfirmNewSaleSelectedProductListAdapter;
import com.rupayan_housing.databinding.FragmentConfirmEditReconcilationBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SearchTransportList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.DiscountViewModel;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.ReconcilationViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class ConfirmEditReconcilation extends BaseFragment {

    private FragmentConfirmEditReconcilationBinding binding;

    private ReconcilationViewModel reconcilationViewModel;

    public static boolean isConfirmSubmitData = false;
    private DueCollectionViewModel dueCollectionViewModel;
    private SaleViewModel saleViewModel;
    private DiscountViewModel discountViewModel;
    private MyDatabaseHelper myDatabaseHelper;


    private String NO_DATA_FOUND = "No Data Found";
    private boolean isDataFetching = false; // variable to detect whether server data fetch is loading or not
    private ArrayAdapter<String> customerNameAdapter;

    /**
     * For customer search
     */
    private List<CustomerResponse> customerResponseList;
    private List<String> customerNameList;

    /**
     * for transport search
     */
    private List<SearchTransportList> searchTransportLists;
    private List<String> searchTransportNameList;

    private String selectedCustomer, selectedTransport;


    /**
     * Store previous fragment Data
     */
    private String selectedEnterpriseId, selectedStoreId, sid, orderSerial;

    /**
     * For Reconciliation type
     */
    private List<String> reconcilationTypeList;
    private List<String> typeList;
    private String selectedreconcilationType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_edit_reconcilation, container, false);
        binding.toolbar.toolbarTitle.setText("Confirm Update Reconciliation");


        myDatabaseHelper = new MyDatabaseHelper(getContext());
        dueCollectionViewModel = new ViewModelProvider(this).get(DueCollectionViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
        reconcilationViewModel = new ViewModelProvider(this).get(ReconcilationViewModel.class);
        getDataFromPreviousFragment();
        /**
         * show quantity updated product list
         */
        showSelectedDataToRecyclerView();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            Navigation.findNavController(getView()).popBackStack();
        });
        /**
         * Handle Reconciliation Type
         */
        binding.reconciliationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedreconcilationType = reconcilationTypeList.get(position);
                binding.reconciliationType.setEnableErrorLabel(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * For Save
         */
        binding.save.setOnClickListener(v -> {
            hideKeyboard(getActivity());
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
                return;
            }

            if (binding.note.getText().toString().isEmpty()) {
                binding.note.setError("Please type a short note");
                binding.note.requestFocus();
                return;
            }
            if (selectedreconcilationType == null) {
                infoMessage(getActivity().getApplication(), "Please select reconciliation type");
                return;
            }
            hideKeyboard(getActivity());
            editReconcilationDialog();
        });


        return binding.getRoot();
    }


    private void showSelectedDataToRecyclerView() {
        List<SalesRequisitionItems> selectedItem = new ArrayList<>();
        selectedItem.addAll(EditReconcilationData.updatedQuantityProductList);//get static data from (AddNewSale) Fragment
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
        sid = getArguments().getString("id");
        orderSerial = getArguments().getString("orderSerial");
        /**
         * set reconcilationType
         */
        reconcilationTypeList = new ArrayList<>();
        reconcilationTypeList.addAll(Arrays.asList("Damage", "Increase", "Lost", "Expire"));
        typeList = new ArrayList<>();
        typeList.addAll(reconcilationTypeList);

        binding.reconciliationType.setItem(typeList);
        if (EditReconcilationData.reconciliationType.equals("Damage")){
            binding.reconciliationType.setSelection(0);
        } if (EditReconcilationData.reconciliationType.equals("Increase")){
            binding.reconciliationType.setSelection(1);
        } if (EditReconcilationData.reconciliationType.equals("Lost")){
            binding.reconciliationType.setSelection(2);
        } if (EditReconcilationData.reconciliationType.equals("Expire")){
            binding.reconciliationType.setSelection(3);
        }
    }

    private void validationAndSave() {
       /* if (selectedTransport == null) {
            binding.searchVehicle.setError("Empty");
            binding.searchVehicle.requestFocus();
            return;
            //Snackbar.make(getView().findViewById(android.R.id.content), "Please Select Transport", Snackbar.LENGTH_SHORT).show();
        }*/
        /**
         * for set updated productIdList,unitList
         */
        Set<Integer> proDuctIdList = new HashSet<>();
        List<String> unitList = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();
        List<String> previousQuantityList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<Integer> quantitySet = new ArrayList<>();
        List<Double> sellingPricelist = new ArrayList<>();
        quantitySet.clear();
        sellingPricelist.clear();
        soldFromList.clear();
        previousQuantityList.clear();

        List<Double> buyingPriceSet = new ArrayList<>();
        buyingPriceSet.clear();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        for (int i = 0; i < EditReconcilationData.updatedQuantityProductList.size(); i++) {
            try {
                proDuctIdList.add(Integer.parseInt(EditReconcilationData.updatedQuantityProductList.get(i).getProductID()));
                unitList.add(EditReconcilationData.updatedQuantityProductList.get(i).getUnit());
                productTitleList.add(EditReconcilationData.updatedQuantityProductList.get(i).getProductTitle());
                buyingPriceSet.add(0.0);
                sellingPricelist.add(0.0);
                soldFromList.add(EditReconcilationData.previousReconcilationInfoResponse.getItems().get(i).getSoldFrom());
                previousQuantityList.add(EditReconcilationData.previousReconcilationInfoResponse.getItems().get(i).getQuantity());
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        }
        for (int i = 0; i < EditReconcilationData.updatedQuantityProductList.size(); i++) {
            quantitySet.add(Integer.parseInt(EditReconcilationData.updatedQuantityProductList.get(i).getQuantity()));
        }


        Set<Integer> selectedStoreIdSet = new HashSet<>();

        selectedStoreIdSet.clear();
        selectedStoreIdSet.add(Integer.parseInt(selectedStoreId));


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        reconcilationViewModel.submitReconcilationEditData(
                getActivity(), sid,
                EditReconcilationData.previousReconcilationInfoResponse.getOrderInfo().getOrderID(),
                proDuctIdList, EditReconcilationData.updatedQuantityList, sellingPricelist, productTitleList,
                soldFromList, soldFromList, previousQuantityList, binding.note.getText().toString(),
                selectedreconcilationType
        ).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "Something Wrong");
                return;
            }
            if (response.getStatus() == 400) {
                Log.d("TYPE", "" + response.getMessage());
                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                return;
            }
            successMessage(getActivity().getApplication(), "" + response.getMessage());
            getActivity().onBackPressed();

//                    *//**
//         * after successfully add new sale to server delete all data from local DB
//         *//*
            try {
                myDatabaseHelper.deleteAllData();//delete all data from local database
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
            getActivity().onBackPressed();
        });
    }


    private void editReconcilationDialog() {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Update ?");//set warning title
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