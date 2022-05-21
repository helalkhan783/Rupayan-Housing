package com.rupayan_housing.view.fragment.reconciliation.addNew;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.ConfirmNewSaleSelectedProductListAdapter;
import com.rupayan_housing.databinding.FragmentConfirmAddNewReconcilationBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.serverResponseModel.CustomerResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.serverResponseModel.SearchTransportList;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.viewModel.DiscountViewModel;
import com.rupayan_housing.viewModel.DueCollectionViewModel;
import com.rupayan_housing.viewModel.ReconcilationViewModel;
import com.rupayan_housing.viewModel.SaleViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class ConfirmAddNewReconcilation extends BaseFragment {

    private FragmentConfirmAddNewReconcilationBinding binding;


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
    private String selectedEnterpriseId, selectedStoreId;

    /**
     * For Reconciliation type
     */
    private List<String> reconcilationTypeList;
    private String selectedreconcilationType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_add_new_reconcilation, container, false);
        binding.toolbar.toolbarTitle.setText("Confirm Add New Reconciliation");


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
            if (selectedreconcilationType == null) {
                infoMessage(getActivity().getApplication(), "Please select reconciliation type");
                return;
            }
            if (binding.note.getText().toString().isEmpty()){
                binding.note.setError("Please type a short note");
                binding.note.requestFocus();
                return;
            }

            hideKeyboard(getActivity());
            addNewReconcilationDialog();
        });


        return binding.getRoot();
    }


    private void showSelectedDataToRecyclerView() {
        List<SalesRequisitionItems> selectedItem = new ArrayList<>();
        selectedItem.addAll(AddNewReconcilation.updatedQuantityProductList);//get static data from (AddNewSale) Fragment
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
        /**
         * set reconcilationType
         */
        reconcilationTypeList = new ArrayList<>();
        reconcilationTypeList.addAll(Arrays.asList("Damage", "Increase", "Lost", "Expire"));
        binding.reconciliationType.setItem(reconcilationTypeList);
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
        List<Integer> proDuctIdList = new ArrayList<>();
        List<String> unitList = new ArrayList<>();
        List<String> productTitleList = new ArrayList<>();
        List<Double> sellingPriseList = new ArrayList<>();

        proDuctIdList.clear();
        unitList.clear();
        productTitleList.clear();
        for (int i = 0; i < AddNewReconcilation.updatedQuantityProductList.size(); i++) {
            proDuctIdList.add(Integer.parseInt(AddNewReconcilation.updatedQuantityProductList.get(i).getProductID()));
            unitList.add(AddNewReconcilation.updatedQuantityProductList.get(i).getUnit());
            productTitleList.add(AddNewReconcilation.updatedQuantityProductList.get(i).getProductTitle());
            sellingPriseList.add(0.0);
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        reconcilationViewModel.addNewReconcilation(
                getActivity(), selectedEnterpriseId, proDuctIdList, AddNewReconcilation.updatedQuantityList,
                sellingPriseList, unitList, productTitleList, selectedStoreId, selectedreconcilationType,
                binding.note.getText().toString()).observe(getViewLifecycleOwner(), response -> {
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
            AddNewReconcilation.sharedPreferenceForStore.deleteData();
            myDatabaseHelper.deleteAllData();//delete all data from local database
            isConfirmSubmitData = true;


        });


    }

    private void addNewReconcilationDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to Add ?");//set warning title
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