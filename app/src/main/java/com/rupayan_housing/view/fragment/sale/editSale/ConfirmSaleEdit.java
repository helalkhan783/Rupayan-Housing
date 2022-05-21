package com.rupayan_housing.view.fragment.sale.editSale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ConfirmNewSaleSelectedProductListAdapter;
import com.rupayan_housing.clickHandle.ConfirmEditNewSale;
import com.rupayan_housing.databinding.FragmentConfirmSaleEditBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SearchTransportList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.sale.newSale.AddNewSale;
import com.rupayan_housing.viewModel.DiscountViewModel;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;
import com.rupayan_housing.viewModel.UpdateSaleViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConfirmSaleEdit extends BaseFragment {
    private FragmentConfirmSaleEditBinding binding;
    private UpdateSaleViewModel updateSaleViewModel;


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
    private String selectedEnterpriseId, selectedStoreId, siId, orderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_sale_edit, container, false);

        binding.toolbar.toolbarTitle.setText("Confirm Sale Edit");
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        dueCollectionViewModel = new ViewModelProvider(this).get(DueCollectionViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
        updateSaleViewModel = new ViewModelProvider(this).get(UpdateSaleViewModel.class);
        getDataFromPreviousFragment();
        /**
         * show quantity updated product list
         */
        showSelectedDataToRecyclerView();

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            Navigation.findNavController(getView()).popBackStack();
        });

        binding.setClickHandle(new ConfirmEditNewSale() {
            @Override
            public void addNewCustomer() {
                Navigation.findNavController(getView()).navigate(R.id.action_confirmSaleEdit_to_addNewCustomer);
            }

            @Override
            public void submit() {
                hideKeyboard(getActivity());
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }

                if (selectedCustomer == null) {
                    binding.customerName.setError("Empty");
                    binding.customerName.requestFocus();
                    return;
                 }

                if (binding.note.getText().toString().isEmpty()) {
                    binding.note.setError("Empty");
                    binding.note.requestFocus();
                    return;
                }
                confirmEditPurchaseDialog(getActivity());
            }
        });

        /**
         * handle customer suggested item click
         */
        binding.customerName.setOnItemClickListener((parent, view, position, id) -> {
            hideKeyboard(getActivity());
            if (customerNameList.get(position).equals(NO_DATA_FOUND)) {
                binding.customerName.getText().clear();
            } else {
                selectedCustomer = customerResponseList.get(position).getCustomerID();
            }
            /**
             * now set data defend on selected customer
             */
            try {
                binding.companyName.setText(customerResponseList.get(position).getCompanyName());
                binding.ownerName.setText(customerResponseList.get(position).getCustomerFname());
                binding.contactNumber.setText(customerResponseList.get(position).getPhone());
                binding.address.setText(
                        customerResponseList.get(position).getAddress() + " " +
                                customerResponseList.get(position).getThana() + ", " +
                                customerResponseList.get(position).getDistrict() + ", " +
                                customerResponseList.get(position).getDivision());
            } catch (Exception e) {
                Log.d("ERROR", e.getLocalizedMessage());
            }
        });

        /**
         * now search customer by customer name
         */
        binding.customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!binding.customerName.isPerformingCompletion()) {
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.customerName.isPerformingCompletion()) { // selected a product
                    return;
                }
                if (!s.toString().trim().isEmpty() && !isDataFetching) {
                    String currentText = binding.customerName.getText().toString();
                    if (!(isInternetOn(getActivity()))) {
                        infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    } else {
                        selectedCustomer = null;//for handle proper selected customer
                        getCustomerBySearchKey(currentText);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        try {
            binding.customerName.setText("" + EditSaleData.customerName);
            binding.companyName.setText("" + EditSaleData.companyName);
            binding.ownerName.setText("" + EditSaleData.customerName);
            binding.contactNumber.setText("" + EditSaleData.customerPhone);
            binding.address.setText("" + EditSaleData.address);
        } catch (Exception e) {

        }


        return binding.getRoot();
    }


    private void validationAndSave() {

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
            return;
        }

        /**
         * set current date will send input from user
         */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        //System.out.println(formatter.format(date));
        String currentDate = formatter.format(date);
        /*System.out.println(dtf.format(now));*/


        /**
         * for set updated productIdList,unitList
         */
        List<String> proDuctIdList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<String> sellingPriceList = new ArrayList<>();
        List<String> previousQuantityList = new ArrayList<>();
        List<String> soldFromList = new ArrayList<>();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        soldFromList.clear();
        try {
            for (int i = 0; i < EditSaleData.updatedQuantityProductList.size(); i++) {
                proDuctIdList.add(EditSaleData.updatedQuantityProductList.get(i).getProductID());
                unitList.add(EditSaleData.updatedQuantityProductList.get(i).getUnit());
                productTitleList.add(EditSaleData.updatedQuantityProductList.get(i).getProductTitle());
                sellingPriceList.add(EditSaleData.currentSaleItemList.get(i).getSellingPrice());
                previousQuantityList.add(EditSaleData.currentSaleItemList.get(i).getQuantity());
                soldFromList.add(EditSaleData.currentSaleItemList.get(i).getSoldFrom());
            }
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }

        /**
         * now send edit data to server
         */

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        updateSaleViewModel.submitSalesEditData(
                getActivity(), orderId, siId, selectedCustomer,
                proDuctIdList, soldFromList,
                EditSaleData.updatedQuantityList, unitList, sellingPriceList,
                productTitleList, Collections.singletonList("0"), previousQuantityList, "1",
                "0", "0",
                currentDate, "0", "0", "0",
                binding.note.getText().toString())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        Log.d("RESPONSE_EDIT_SALE", "" + response.getMessage());
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    getActivity().onBackPressed();

                    /**
                     * after successfully add new sale Edit data to server delete all data from local DB
                     */

                    myDatabaseHelper.deleteAllData();//delete all data from local database
                    isConfirmSubmitData = true;
                });

    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        selectedEnterpriseId = getArguments().getString("selectedEnterpriseId");
        selectedStoreId = getArguments().getString("selectedStoreId");
        siId = getArguments().getString("siId");
        orderId = getArguments().getString("orderId");
    }

  /*  private void setTransportDetailsBySelectedTransport(SearchTransportList searchTransportList) {
        binding.driverName.setText(searchTransportList.getPersonName());
        binding.phone.setText(searchTransportList.getPhone());
        binding.vehicleFare.setText("");
        binding.vehicleNumber.setText("" + searchTransportList.getVehicleShipNo());
    }
*/

    private void getCustomerBySearchKey(String currentText) {

        /**
         * call
         */
        dueCollectionViewModel
                .apiCallForGetCustomers(
                        getActivity(),
                        getToken(getActivity().getApplication()),
                        getVendorId(getActivity().getApplication()),
                        currentText
                );
        /**
         * get data from above calling api
         */
        dueCollectionViewModel.getCustomerList()
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() != 200) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }

                    isDataFetching = true;
                    selectedCustomer = null;
                    customerResponseList = new ArrayList<>();
                    customerResponseList.clear();
                    customerResponseList.addAll(response.getLists());

                    customerNameList = new ArrayList<>();
                    customerNameList.clear();


                    for (int i = 0; i < response.getLists().size(); i++) {
                        customerNameList.add("" + response.getLists().get(i).getCustomerFname());
                    }

                    if (customerNameList.isEmpty()) {
                        customerNameList.add(NO_DATA_FOUND);
                    }

                    customerNameAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, customerNameList);
                    binding.customerName.setAdapter(customerNameAdapter);
                    binding.customerName.showDropDown();
                    isDataFetching = false;
                });

    }

//
//    private void getTransportDetailsBySearchKey(String currentText) {
//        saleViewModel.searchTransport(getActivity(), currentText)
//                .observe(getViewLifecycleOwner(), response -> {
//                    if (response == null) {
//                        errorMessage(getActivity().getApplication(), "Something Wrong");
//                        return;
//                    }
//                    if (response.getStatus() != 200) {
//                        errorMessage(getActivity().getApplication(), "Something Wrong");
//                        return;
//                    }
//
//                    isDataFetching = true;
//                    searchTransportLists = new ArrayList<>();
//                    searchTransportLists.clear();
//                    searchTransportNameList = new ArrayList<>();
//                    searchTransportNameList.clear();
//
//
//                    searchTransportLists.addAll(response.getLists());
//
//
//                    for (int i = 0; i < response.getLists().size(); i++) {
//                        searchTransportNameList.add(response.getLists().get(i).getTransportName());
//                    }
//
//                    if (searchTransportNameList.isEmpty()) {
//                        searchTransportNameList.add(NO_DATA_FOUND);
//                    }
//                    customerNameAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, searchTransportNameList);
//                    binding.searchVehicle.setAdapter(customerNameAdapter);
//                    binding.searchVehicle.showDropDown();
//                    isDataFetching = false;
//                });
//    }


    private void showSelectedDataToRecyclerView() {
        List<SalesRequisitionItems> selectedItem = new ArrayList<>();
        selectedItem.addAll(EditSaleData.updatedQuantityProductList);//get static data from (EditSaleData) Fragment
        /**
         * set set selected data to recyclerview
         */
        ConfirmNewSaleSelectedProductListAdapter adapter = new ConfirmNewSaleSelectedProductListAdapter(getActivity(), selectedItem);
        binding.selectedProductsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.selectedProductsRv.setAdapter(adapter);
    }

    public void confirmEditPurchaseDialog(FragmentActivity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Edit This Sale ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.unicef_main));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        Button cancel = view.findViewById(R.id.cancel);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(v -> alertDialog.dismiss());//for cancel
        bOk.setOnClickListener(v -> {
            alertDialog.dismiss();
            validationAndSave();
        });
        /* if (alertDialog.getWindow() != null) {
         *//**
         * for show sliding animation in alert dialog
         *//*
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        }*/
        alertDialog.show();
    }
}