package com.rupayan_housing.view.fragment.sale.newSale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ConfirmNewSaleSelectedProductListAdapter;
import com.rupayan_housing.clickHandle.ConfirmSaleClickhandle;
import com.rupayan_housing.databinding.FragmentConfirmNewSaleBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SearchTransportList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.DiscountViewModel;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfirmNewSale extends BaseFragment {
    private FragmentConfirmNewSaleBinding binding;
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
    private String selectedEnterpriseId, selectedStoreId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_new_sale, container, false);
        binding.toolbar.toolbarTitle.setText("Confirm Sale");
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        dueCollectionViewModel = new ViewModelProvider(this).get(DueCollectionViewModel.class);
        saleViewModel = new ViewModelProvider(this).get(SaleViewModel.class);
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
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
         * for save sale
         */
        binding.setClickHandle(new ConfirmSaleClickhandle() {
            @Override
            public void save() {
                hideKeyboard(getActivity());
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check your Internet Connection");
                    return;
                }
                hideKeyboard(getActivity());

                if (selectedCustomer == null) {
                    binding.customerName.setError("Empty");
                    binding.customerName.requestFocus();
                    return;
                 }
                confirmEditPurchaseDialog(getActivity());
            }

            @Override
            public void addNewCustomer() {
                hideKeyboard(getActivity());
                Navigation.findNavController(getView()).navigate(R.id.action_confirmNewSale_to_addNewCustomer);
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
                    hideKeyboard(getActivity());
                } catch (Exception e) {
                    Log.d("ERROR", e.getLocalizedMessage());
                }
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

        return binding.getRoot();
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
        List<String> proDuctIdList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        for (int i = 0; i < AddNewSale.updatedQuantityProductList.size(); i++) {
            try {
                proDuctIdList.add(AddNewSale.updatedQuantityProductList.get(i).getProductID());
                unitList.add(AddNewSale.updatedQuantityProductList.get(i).getUnit());
                productTitleList.add(AddNewSale.updatedQuantityProductList.get(i).getProductTitle());
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        }

        /**
         * for get last order Id
         */
        discountViewModel.getLastOrderId(getActivity()).observe(getViewLifecycleOwner(), lastOrderId ->
                saleViewModel.newSale(
                        getActivity(), selectedEnterpriseId, String.valueOf(lastOrderId), selectedCustomer, proDuctIdList,
                        Arrays.asList(selectedStoreId), AddNewSale.updatedQuantityList, unitList, productTitleList,
                        "", "",
                        "", ""
                ).observe(getViewLifecycleOwner(), response -> {
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
                    AddNewSale.sharedPreferenceForStore.deleteData();
                    myDatabaseHelper.deleteAllData();//delete all data from local database
                    isConfirmSubmitData = true;

                }));
    }

    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        selectedEnterpriseId = getArguments().getString("selectedEnterpriseId");
        selectedStoreId = getArguments().getString("selectedStoreId");
    }

//    private void setTransportDetailsBySelectedTransport(SearchTransportList searchTransportList) {
//        binding.driverName.setText(searchTransportList.getPersonName());
//        binding.phone.setText(searchTransportList.getPhone());
//        binding.vehicleFare.setText("");
//        binding.vehicleNumber.setText("" + searchTransportList.getVehicleShipNo());
//    }


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


    private void getTransportDetailsBySearchKey(String currentText) {
        saleViewModel.searchTransport(getActivity(), currentText)
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
                    searchTransportLists = new ArrayList<>();
                    searchTransportLists.clear();
                    searchTransportNameList = new ArrayList<>();
                    searchTransportNameList.clear();


                    searchTransportLists.addAll(response.getLists());


                    for (int i = 0; i < response.getLists().size(); i++) {
                        searchTransportNameList.add("" + response.getLists().get(i).getTransportName());
                    }

                    if (searchTransportNameList.isEmpty()) {
                        searchTransportNameList.add(NO_DATA_FOUND);
                    }
                    customerNameAdapter = new ArrayAdapter<>(getContext(), R.layout.filter_model, R.id.customerNameModel, searchTransportNameList);
//                    binding.searchVehicle.setAdapter(customerNameAdapter);
//                    binding.searchVehicle.showDropDown();
                    isDataFetching = false;
                });
    }


    private void showSelectedDataToRecyclerView() {
        List<SalesRequisitionItems> selectedItem = new ArrayList<>();
        selectedItem.addAll(AddNewSale.updatedQuantityProductList);//get static data from (AddNewSale) Fragment
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
        tvTitle.setText("Do You Want to Confirm This Sale ?");//set warning title
        tvMessage.setText("MIS ERP");
        imageIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.app_logo));//set warning image
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

        alertDialog.show();
    }
}