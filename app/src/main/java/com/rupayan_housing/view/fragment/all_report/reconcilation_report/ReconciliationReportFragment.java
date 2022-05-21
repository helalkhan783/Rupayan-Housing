package com.rupayan_housing.view.fragment.all_report.reconcilation_report;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.FragmentReconciliationReportBinding;
import com.rupayan_housing.permission.SharedPreferenceForZoneReprt;
import com.rupayan_housing.permission.SharedPreferenceReportForMill;
import com.rupayan_housing.serverResponseModel.ReportProduct;
import com.rupayan_housing.serverResponseModel.Store;
import com.rupayan_housing.serverResponseModel.TransferReportPageDataResponse;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.miller_response.ReconciliationMillerList;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.miller_response.ReconciliationReportMillerResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationPageDataResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportAssociationList;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportBrandList;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportItemList;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.store.ReconciliationStore;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.store.ReconciliationStoreResponse;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_report.sale_response.SaleReportCategoryList;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReconciliationReportFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {
    private FragmentReconciliationReportBinding binding;
    private ReconciliationReportViewModel reconciliationViewModel;
    private boolean isStartDate = false;

    /**
     * for store List
     */
    private List<String> storeNameList;
    private List<ReconciliationStore> storeLists;


    /**
     * for Association list
     */
    private List<String> associationName;
    private List<ReconciliationReportAssociationList> associationLists;

    /**
     * for item list
     */
    private List<String> itemNameList;
    private List<ReconciliationReportItemList> itemLists;

    /**
     * for Category List
     */
    private List<String> categoryNameList;
    private List<SaleReportCategoryList> purchaseCategoryLists;

    /**
     * for brand List
     */
    private List<String> brandNameList;
    private List<ReconciliationReportBrandList> brandLists;
    /**
     * for miller List
     */
    private List<String> millerNameList;
    private List<ReconciliationMillerList> millerLists;
    private List<Store> storeList;
    private List<ReportProduct> itemList;
    /**
     * for ReconcilationType
     */
    List<String> reconcilationType;
    private String selectAssociationId, millerProfileId, itemId, storeId, brandId, categoryId;
    private String fromStoreId, toStoreId, transferItemId; // for transfer report
    String startDate, endDate, associationId, portion, pageName;
    private String selectReconciliation;
    String selectedStoreName, millerSelectedName, selectedAssociationName;

    SharedPreferenceForZoneReprt sharedPreferenceForZoneReprt;
    SharedPreferenceReportForMill sharedPreferenceReportForMill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reconciliation_report, container, false);

        reconciliationViewModel = new ViewModelProvider(this).get(ReconciliationReportViewModel.class);
        sharedPreferenceForZoneReprt = new SharedPreferenceForZoneReprt(getActivity());
        sharedPreferenceReportForMill = new SharedPreferenceReportForMill(getActivity());
        /** back Control */
        binding.toolbar.setClickHandle(() -> {
            sharedPreferenceForZoneReprt.deleteZoneData();
            sharedPreferenceReportForMill.deleteMillData();
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        getPreviousFragmentData();
        binding.toolbar.toolbarTitle.setText("Check Inventory Status");

        getPageData();

        setonClick();


        /** for search button Click */
        binding.search.setOnClickListener(v -> {
            /** purchase Report from start here */

         /*   if (binding.startDate.getText().toString().isEmpty()) {
                binding.startDate.setError("Empty start date");
                binding.startDate.requestFocus();
                return;
            }
            if (binding.EndDate.getText().toString().isEmpty()) {
                binding.EndDate.setError("Empty start date");
                binding.EndDate.requestFocus();
                return;
            }*/

            Bundle bundle = new Bundle();
            bundle.putString("startDate", binding.startDate.getText().toString());
            bundle.putString("endDate", binding.EndDate.getText().toString());
            bundle.putString("millerProfileId", sharedPreferenceReportForMill.getMillId());
            bundle.putString("itemId", itemId);
            bundle.putString("brandId", brandId);
            bundle.putString("portion", getArguments().getString("portion"));
            bundle.putString("categoryId", itemId);
            bundle.putString("storeId", storeId);
            bundle.putString("pageName", pageName);
            bundle.putString("selectReconciliation", selectReconciliation);
            //for transfer report data
            bundle.putString("fromStoreId", fromStoreId);
            bundle.putString("toStoreId", toStoreId);
            bundle.putString("transferItemId", transferItemId);

            bundle.putString("selectAssociationId", sharedPreferenceForZoneReprt.getZoneId());
//for inforamtion show
            bundle.putString("selectedStoreName", selectedStoreName);
            bundle.putString("millerSelectedName", millerSelectedName);


            Navigation.findNavController(getView()).navigate(R.id.action_reconciliationReportFragment_to_purchaseReturnListFragment, bundle);

        });


        /** select spinner item */
        binding.selectAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAssociationId = associationLists.get(position).getStoreID();
                // add zone id to local
                sharedPreferenceForZoneReprt.saveZone(associationLists.get(position).getStoreID());
                selectedAssociationName = associationLists.get(position).getDisplayName();
                if (position == 0) {
                    selectedAssociationName = "ISMOS";
                }
                 getMillerPageData(selectAssociationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.miller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                millerProfileId = String.valueOf(millerLists.get(position).getStoreID());
                sharedPreferenceReportForMill.saveMill(String.valueOf(millerLists.get(position).getStoreID()));
                millerSelectedName = millerLists.get(position).getDisplayName();

                getStoreDataByMillerStore();//store data select

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

// spinner for reconciliation and stock report
        binding.selectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemId = itemLists.get(position).getProductID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.selectBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandId = brandLists.get(position).getBrandID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        binding.selectItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                categoryId = itemLists.get(position).getProductID();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        try {
            binding.selectStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    storeId = storeLists.get(position).getStoreID();
                    selectedStoreName = storeLists.get(position).getStoreName();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
        }


        binding.selectReconciliation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) {
                    binding.selectReconciliation.clearSelection();
                }
                selectReconciliation = reconcilationType.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner for transfer report
        binding.accessForField.fromStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromStoreId = storeList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.accessForField.toStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toStoreId = storeList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.accessForField.item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transferItemId = itemList.get(position).getProductID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void getPageData() {

        getPAgeDataFromViewModel();
    }


    private void getPreviousFragmentData() {
        portion = getArguments().getString("portion");
        pageName = getArguments().getString("pageName");
    }

    private void getStoreDataByMillerStore() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        reconciliationViewModel.getSaleReturnReportStore(getActivity(), millerProfileId).observe(getViewLifecycleOwner(), new Observer<ReconciliationStoreResponse>() {
            @Override
            public void onChanged(ReconciliationStoreResponse response) {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setDataStoreSpinner(response);
                }
            }
        });
    }

    private void setDataStoreSpinner(ReconciliationStoreResponse response) {
        storeNameList = new ArrayList<>();
        storeNameList.clear();
        storeLists = new ArrayList<>();
        storeLists.clear();
        storeLists.addAll(response.getMillerList());

        for (int i = 0; i < response.getMillerList().size(); i++) {
            storeNameList.add("" + response.getMillerList().get(i).getStoreName());
        }
        binding.selectStore.setItem(storeNameList);

    }


    private void getMillerPageData(String selectAssociationId) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        reconciliationViewModel.getReconciliationMiller(getActivity(), selectAssociationId).observe(getViewLifecycleOwner(), new Observer<ReconciliationReportMillerResponse>() {
            @Override
            public void onChanged(ReconciliationReportMillerResponse response) {
                progressDialog.dismiss();
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setDataMillerSpinner(response);
                }
            }
        });

    }

    private void setDataMillerSpinner(ReconciliationReportMillerResponse response) {
        millerNameList = new ArrayList<>();
        millerNameList.clear();
        millerLists = new ArrayList<>();
        millerLists.clear();

        millerLists.addAll(response.getMillerList());

        for (int i = 0; i < response.getMillerList().size(); i++) {
            millerNameList.add("" + response.getMillerList().get(i).getFullName());
            if (!sharedPreferenceReportForMill.getMillId().isEmpty() || sharedPreferenceReportForMill.getMillId() !=null){
                if (sharedPreferenceReportForMill.getMillId().equals(millerLists.get(i).getStoreID())){
                    binding.miller.setSelection(i);
                 }
            }
        }
        binding.miller.setItem(millerNameList);

    }


    private void getPAgeDataFromViewModel() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        reconciliationViewModel.getReconciliationPageData(getActivity(), getProfileId(getActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<ReconciliationPageDataResponse>() {
            @Override
            public void onChanged(ReconciliationPageDataResponse response) {
                progressDialog.dismiss();
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        errorMessage(getActivity().getApplication(), response.getMessage());
                        return;

                    }
                    if (response.getStatus() == 200) {
                        setDataInSpinner(response);

                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    private void setDataInSpinner(ReconciliationPageDataResponse response) {


        /** for Association */
        associationLists = new ArrayList<>();
        associationLists.clear();

        associationName = new ArrayList<>();
        associationName.clear();

        associationLists.addAll(response.getAssociationList());

        for (int i = 0; i < response.getAssociationList().size(); i++) {
            associationName.add("" + response.getAssociationList().get(i).getZoneName()+"/"+response.getAssociationList().get(i).getDisplayName());
            if (sharedPreferenceForZoneReprt.getZoneId() !=null || !sharedPreferenceForZoneReprt.getZoneId().isEmpty()){
                if (sharedPreferenceForZoneReprt.getZoneId().equals(associationLists.get(i).getStoreID())){
                    binding.selectAssociation.setSelection(i);
                }
            }
        }
        binding.selectAssociation.setItem(associationName);
        if (getProfileTypeId(getActivity().getApplication()).equals("6")) {
            if (associationLists.size() == 1) {
                binding.selectAssociation.setSelection(0);
                selectAssociationId = associationLists.get(0).getStoreID();
                getMillerPageData(selectAssociationId);
            }
        }

        /** for item  */

        itemNameList = new ArrayList<>();
        itemNameList.clear();
        itemLists = new ArrayList<>();
        itemLists.addAll(response.getItemList());


        for (int i = 0; i < response.getItemList().size(); i++) {
            itemNameList.add("" + response.getItemList().get(i).getProductTitle());
        }
        binding.selectItem.setItem(itemNameList);


        /*  *//** for category List *//*
        categoryNameList = new ArrayList<>();
        categoryNameList.clear();
        purchaseCategoryLists = new ArrayList<>();
        purchaseCategoryLists.clear();;
        purchaseCategoryLists.addAll(response.getCategoryList());

        for (int i = 0; i < response.getCategoryList().size(); i++) {
            categoryNameList.add(response.getCategoryList().get(i).getCategory());
        }
        binding.selectCategory.setItem(categoryNameList);
*/

        /** for brandList */
        brandNameList = new ArrayList<>();
        brandNameList.clear();

        brandLists = new ArrayList<>();
        brandLists.clear();

        brandLists.addAll(response.getBrandList());

        for (int i = 0; i < response.getBrandList().size(); i++) {
            brandNameList.add("" + response.getBrandList().get(i).getBrandName());
        }
        binding.selectBrand.setItem(brandNameList);

        /**
         * now set selectReconciliation type
         */
        reconcilationType = new ArrayList<>();
        reconcilationType.add(response.getReconcilationType().getDamage());
        reconcilationType.add(response.getReconcilationType().getIncrease());
        reconcilationType.add(response.getReconcilationType().getLost());
        reconcilationType.add(response.getReconcilationType().getExpire());

        binding.selectReconciliation.setItem(reconcilationType);


        if (checkProfileType()) {
            millerNameList = new ArrayList<>();
            millerNameList.clear();
            millerLists = new ArrayList<>();
            millerLists.clear();

            millerLists.addAll(response.getMillerList());

            for (int i = 0; i < response.getMillerList().size(); i++) {
                millerNameList.add("" + response.getMillerList().get(i).getDisplayName());
            }
            binding.miller.setItem(millerNameList);

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        selectAssociationId = null;
        millerProfileId = null;
        selectReconciliation = null;
        itemId = null;
        brandId = null;
        categoryId = null;
        storeId = null;
        startDate = null;
        endDate = null;
        associationId = null;
        fromStoreId = null;
        toStoreId = null;
        transferItemId = null;
        selectedStoreName = null;
        millerSelectedName = null;
        selectedAssociationName = null;



        /*String profileTypeId = getProfileTypeId(getActivity().getApplication());

        if (!(profileTypeId.equals("4") || profileTypeId.equals("5"))) {
            binding.associationLayout.setVisibility(View.GONE);
        }*/
    }

    private void setonClick() {
        binding.startDate.setOnClickListener(this);
        binding.EndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                isStartDate = true;
                timePicker();
                break;

            case R.id.EndDate:
                timePicker();
                break;
        }

    }

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ReconciliationReportFragment.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        assert getFragmentManager() != null;
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear;
        if (month == 12) {
            month = 1;
        } else {
            month = monthOfYear + 1;
        }
        String mainMonth, mainDay;


        if (month <= 9) {
            mainMonth = "0" + month;
        } else {
            mainMonth = String.valueOf(month);
        }
        if (dayOfMonth <= 9) {
            mainDay = "0" + dayOfMonth;
        } else {
            mainDay = String.valueOf(dayOfMonth);
        }
        String selectedDate = year + "-" + mainMonth + "-" + mainDay;//set the selected date

        if (!isStartDate) {
            binding.EndDate.setText(selectedDate);
            binding.EndDate.setError(null);
        } else {
            binding.startDate.setText(selectedDate);
            binding.startDate.setError(null);
            isStartDate = false;
        }
    }
}