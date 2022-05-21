package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report;

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
import com.rupayan_housing.databinding.FragmentReportListBinding;
import com.rupayan_housing.permission.SharedPreferenceForReport;
import com.rupayan_housing.permission.SharedPreferenceForZoneReprt;
import com.rupayan_housing.permission.SharedPreferenceReportForMill;
import com.rupayan_housing.serverResponseModel.DistrictList;
import com.rupayan_housing.serverResponseModel.PurchaseAssociationList;
import com.rupayan_housing.serverResponseModel.PurchaseReportResponse;
import com.rupayan_housing.serverResponseModel.ReportPurchaseBrandList;
import com.rupayan_housing.serverResponseModel.ReportPurchaseCategoryList;
import com.rupayan_housing.serverResponseModel.ReportPurchaseSupplierList;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.get_miller_by_association.MillerReportByAssociationResponse;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.get_miller_by_association.PurchaseMillerList;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.purchase_store.PurchaseReportStore;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.purchase_store.PurchaseReportStoreResponse;
import com.rupayan_housing.viewModel.report_all_view_model.ReportViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ReportListFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {
    private FragmentReportListBinding binding;
    private String portion;//get previous fragment
    private ProgressDialog progressDialog;
    private boolean isStartDate = false;

    private ReportViewModel reportViewModel;
    /**
     * for Association list
     */
    private List<String> associationName;
    private List<PurchaseAssociationList> associationLists;

    /**
     * for supplier list
     */
    private List<String> supplierNameList;
    private List<ReportPurchaseSupplierList> supplierResponseLists;
    List<DistrictList> districtLists;
    List<String> districtNameList;
    /**
     * for Category List
     */
    private List<String> categoryNameList;
    private List<ReportPurchaseCategoryList> purchaseCategoryLists;

    /**
     * for brand List
     */
    private List<String> brandNameList;
    private List<ReportPurchaseBrandList> brandLists;
    /**
     * for miller List
     */
    private List<String> millerNameList;
    private List<PurchaseMillerList> millerLists;

    /**
     * for store List
     */
    private List<String> storeNameList;
    private List<PurchaseReportStore> storeLists;

    private String selectAssociationId, selectedAssociationName, districId, millerProfileId, millerSelectedName, supplierId, customerName, brandId, categoryId;
    String startDate, endDate, storeId, selectedStoreName, associaionId, pageName;
    SharedPreferenceForReport sharedPreferenceForReport;
    SharedPreferenceReportForMill sharedPreferenceReportForMill;
    SharedPreferenceForZoneReprt sharedPreferenceForZoneReprt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_list, container, false);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        sharedPreferenceForReport = new SharedPreferenceForReport(getActivity());
        sharedPreferenceReportForMill = new SharedPreferenceReportForMill(getActivity());
        sharedPreferenceForZoneReprt = new SharedPreferenceForZoneReprt(getActivity());

        getPreviousFragmentData();

        categoryNameList = new ArrayList<>();
        brandNameList = new ArrayList<>();
        categoryNameList.add("Rupayan City Uttara");
        categoryNameList.add("Rupayan Majestic");
        categoryNameList.add("Rupayan Grand");
        categoryNameList.add("Rupayan Lake Castel");
        categoryNameList.add("Rupayan Sky Villa");

        binding.selectCategory.setItem(categoryNameList);


        brandNameList.add("Commercial");
        brandNameList.add("Residential");
        binding.selectBrand.setItem(brandNameList);


        supplierNameList = new ArrayList<>();
        supplierNameList.add("Harunur Rqashid");
        supplierNameList.add("Sarowar Hossain");
        supplierNameList.add("Nijam Uddin");
        binding.selectSupplier.setItem(supplierNameList);


        storeNameList = new ArrayList<>();
        storeNameList.clear();

        storeNameList.add("Md Noman");
        storeNameList.add("Kazi Salauddin");
        storeNameList.add("Hasnat Chowdhury");

        binding.selectStore.setItem(storeNameList);


        /** get page data from ReportViewModel */
        //   getPageDataFromViewModel();

        /** set click on startDate TextView & EndDate TextView */
        setOnClick();

        /** back Control */
        binding.toolbar.setClickHandle(() -> {
            sharedPreferenceForReport.deleteCustomerData();
            sharedPreferenceReportForMill.deleteMillData();
            sharedPreferenceForZoneReprt.deleteZoneData();
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


        /** for search button Click */
        binding.search.setOnClickListener(v -> {
            /** purchase Report from start here */
          /*  if (binding.startDate.getText().toString().isEmpty()) {
                binding.startDate.setError("Empty start date");
                binding.startDate.requestFocus();
                return;
            }
            if (binding.EndDate.getText().toString().isEmpty()) {
                binding.EndDate.setError("Empty start date");
                binding.EndDate.requestFocus();
                return;
            }
*/
            Bundle bundle = new Bundle();
            bundle.putString("startDate", binding.startDate.getText().toString());
            bundle.putString("endDate", binding.EndDate.getText().toString());
            bundle.putString("portion", portion);
            bundle.putString("millerProfileId", sharedPreferenceReportForMill.getMillId());
            bundle.putString("supplierId", supplierId);
            bundle.putString("brandId", brandId);
            bundle.putString("categoryId", categoryId);
            bundle.putString("storeId", storeId);
            bundle.putString("portion", portion);
            bundle.putString("pageName", pageName);
            bundle.putString("selectedAssociationName", selectedAssociationName);
            bundle.putString("districId", districId);
            //report information show
            bundle.putString("selectedStoreName", selectedStoreName);
            bundle.putString("millerSelectedName", millerSelectedName);
            bundle.putString("selectAssociationId", sharedPreferenceForZoneReprt.getZoneId());
            bundle.putString("customerName", customerName);
            Navigation.findNavController(getView()).navigate(R.id.action_reportListFragment_to_purchaseReturnListFragment, bundle);
        });


        binding.miller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                millerProfileId = millerLists.get(position).getStoreID();
                millerSelectedName = millerLists.get(position).getDisplayName();
                sharedPreferenceReportForMill.saveMill(millerLists.get(position).getStoreID());
                //  getStoreDataByMillerStore();//store data select

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.selectSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplierId = supplierResponseLists.get(position).getCustomerID();
                sharedPreferenceForReport.saveCustomerId(supplierResponseLists.get(position).getCustomerID());
                customerName = supplierResponseLists.get(position).getCustomerFname();
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
        binding.selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = purchaseCategoryLists.get(position).getCategoryID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


        binding.selectAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectAssociationId = associationLists.get(position).getStoreID();
                selectedAssociationName = associationLists.get(position).getFullName();
                sharedPreferenceForZoneReprt.saveZone(associationLists.get(position).getStoreID());

                binding.selectAssociation.setEnableErrorLabel(false);
                getMillerPageData(selectAssociationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return binding.getRoot();
    }

    private void getPreviousFragmentData() {
        try {
            portion = getArguments().getString("portion");
            pageName = getArguments().getString("pageName");
            binding.toolbar.toolbarTitle.setText(pageName);

            if (portion.equals(ReportUtils.availAbleReport) || portion.equals(ReportUtils.projectWiseItemReport)) {
                binding.executiveAndCustomerLayout.setVisibility(View.GONE);
                binding.dateLayout.setVisibility(View.GONE);
            }
            if (portion.equals(ReportUtils.salesSummeryReport)) {
                binding.customerLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void getStoreDataByMillerStore() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        reportViewModel.getPurchaseReportStore(getActivity(), millerProfileId).observe(getViewLifecycleOwner(), new Observer<PurchaseReportStoreResponse>() {
            @Override
            public void onChanged(PurchaseReportStoreResponse response) {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong ");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    setInStoreSpinner(response);
                }
            }
        });

        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districId = districtLists.get(position).getDistrictId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setInStoreSpinner(PurchaseReportStoreResponse response) {
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
        progressDialog.show();
        reportViewModel.getPurchaseReportByAssociationId(getActivity(), selectAssociationId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                errorMessage(getActivity().getApplication(), "something wrong");
                return;
            }
            if (response.getStatus() == 400) {
                errorMessage(getActivity().getApplication(), response.getMessage());
                return;
            }
            if (response.getStatus() == 200) {
                /** now set data to select spinner */
                setDataInMillerSpinner(response);
            }
        });
    }

    private void setDataInMillerSpinner(MillerReportByAssociationResponse response) {
        if (response.getMillerList() == null || response.getMillerList().isEmpty()) {

            return;
        }
        millerNameList = new ArrayList<>();
        millerNameList.clear();
        millerLists = new ArrayList<>();
        millerLists.clear();

        millerLists.addAll(response.getMillerList());

        for (int i = 0; i < response.getMillerList().size(); i++) {
            millerNameList.add("" + response.getMillerList().get(i).getFullName());
            if (!sharedPreferenceReportForMill.getMillId().isEmpty() || sharedPreferenceReportForMill.getMillId() != null) {
                if (sharedPreferenceReportForMill.getMillId().equals(millerLists.get(i).getStoreID())) {
                    binding.miller.setSelection(i);
                }
            }
        }
        binding.miller.setItem(millerNameList);
    }

    private void getPageDataFromViewModel() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        reportViewModel.getPurchaseReportPageData(getActivity(), getProfileId(requireActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<PurchaseReportResponse>() {
            @Override
            public void onChanged(PurchaseReportResponse response) {
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    errorMessage(requireActivity().getApplication(), " " + response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {

                    setDataInAssociationSpinner(response);
                }
            }
        });
    }

    private void setDataInAssociationSpinner(PurchaseReportResponse response) {
        /** for category List */
        categoryNameList = new ArrayList<>();
        categoryNameList.clear();
        purchaseCategoryLists = new ArrayList<>();
        purchaseCategoryLists.clear();
        ;
        purchaseCategoryLists.addAll(response.getCategoryList());

        /*for (int i = 0; i < response.getCategoryList().size(); i++) {
            categoryNameList.add("" + response.getCategoryList().get(i).getCategory());
        }*/
        categoryNameList.add("Rupayan City Uttara");
        categoryNameList.add("Rupayan Majestic");
        categoryNameList.add("Rupayan Grand");
        categoryNameList.add("Rupayan Lake Castel");
        categoryNameList.add("Rupayan Sky Villa");

        binding.selectCategory.setItem(categoryNameList);


        /** for brandList */
        brandNameList = new ArrayList<>();
        brandNameList.clear();

        brandLists = new ArrayList<>();
        brandLists.clear();

        brandLists.addAll(response.getBrandList());

        /*for (int i = 0; i < response.getBrandList().size(); i++) {
            brandNameList.add("" + response.getBrandList().get(i).getBrandName());
        }*/

        brandNameList.add("Commercial");
        brandNameList.add("Residential");
        binding.selectBrand.setItem(brandNameList);


        if (checkProfileType()) {
            millerNameList = new ArrayList<>();
            millerNameList.clear();
            millerLists = new ArrayList<>();
            millerLists.clear();

            try {
                if (response.getMillerList() != null) {
                    millerLists.addAll(response.getMillerList());

                    for (int i = 0; i < response.getMillerList().size(); i++) {
                        millerNameList.add("" + response.getMillerList().get(i).getDisplayName());
                    }
                    binding.miller.setItem(millerNameList);
                }
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        }
        /** for Association */
        associationLists = new ArrayList<>();
        associationLists.clear();
        associationName = new ArrayList<>();
        associationName.clear();

        associationLists.addAll(response.getAssociationList());
        for (int i = 0; i < response.getAssociationList().size(); i++) {
            associationName.add("" + response.getAssociationList().get(i).getDisplayName());
            if (!sharedPreferenceForZoneReprt.getZoneId().isEmpty() || sharedPreferenceForZoneReprt.getZoneId() != null) {
                if (sharedPreferenceForZoneReprt.getZoneId().equals(associationLists.get(i).getStoreID())) {
                    binding.selectAssociation.setSelection(i);
                }
            }
        }
        binding.selectAssociation.setItem(associationName);

        if (getProfileTypeId(getActivity().getApplication()).equals("6")) {
            try {
                if (associationLists.size() == 1) {
                    binding.selectAssociation.setSelection(0);
                    binding.selectAssociation.setSelected(false);
                    selectAssociationId = associationLists.get(0).getStoreID();
                    getMillerPageData(selectAssociationId);
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }

        }


        /** for customerList */
        if (portion.equals(ReportUtils.processingReport) || portion.equals(ReportUtils.availAbleReport)) {
            supplierNameList = new ArrayList<>();
            supplierNameList.clear();
            supplierResponseLists = new ArrayList<>();
            supplierResponseLists.addAll(response.getCustomerList());
            if (supplierResponseLists == null || supplierResponseLists.isEmpty()) {
                return;
            }

            try {
                for (int i = 0; i < response.getCustomerList().size(); i++) {
                    supplierNameList.add("" + supplierResponseLists.get(i).getCompanyName() + "@" + supplierResponseLists.get(i).getCustomerFname());
                    if (sharedPreferenceForReport.getCustomerId() != null || !sharedPreferenceForReport.getCustomerId().isEmpty()) {
                        if (sharedPreferenceForReport.getCustomerId().equals(supplierResponseLists.get(i).getCustomerID())) {
                            binding.selectSupplier.setSelection(i);

                        }
                    }
                }
                binding.selectSupplier.setItem(supplierNameList);

            } catch (Exception e) {
            }
            return;
        }
        /** for supplierList */

        supplierNameList = new ArrayList<>();
        supplierNameList.clear();
        supplierResponseLists = new ArrayList<>();
        supplierResponseLists.addAll(response.getSupplierList());


        try {
            for (int i = 0; i < response.getSupplierList().size(); i++) {
                supplierNameList.add("" + response.getSupplierList().get(i).getCompanyName() + "@" + response.getSupplierList().get(i).getCustomerFname());
                if (sharedPreferenceForReport.getCustomerId() != null || sharedPreferenceForReport.getCustomerId().isEmpty()) {
                    if (sharedPreferenceForReport.getCustomerId().equals(response.getSupplierList().get(i).getCustomerID())) {
                        binding.selectSupplier.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {
        }
        binding.selectSupplier.setItem(supplierNameList);


        districtLists = new ArrayList<>();
        districtLists.clear();
        districtLists.addAll(response.getDistrictList());
        districtNameList = new ArrayList<>();
        districtNameList.clear();
        for (int i = 0; i < districtLists.size(); i++) {
            districtNameList.add("" + districtLists.get(i).getName());
        }
        binding.districtSpinner.setItem(districtNameList);


    }


    /**
     * for click
     */

    private void setOnClick() {
        binding.startDate.setOnClickListener(this);
        binding.EndDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                timePicker();
                isStartDate = true;
                break;

            case R.id.EndDate:
                timePicker();
                break;
        }
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

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ReportListFragment.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        assert getFragmentManager() != null;
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onStart() {
        super.onStart();
        selectAssociationId = null;
        millerProfileId = null;
        supplierId = null;
        brandId = null;
        categoryId = null;
        startDate = null;
        endDate = null;
        storeId = null;
        selectedStoreName = null;
        millerSelectedName = null;
        selectedAssociationName = null;
        customerName = null;
        districId = null;


        String profileTypeId = getProfileTypeId(getActivity().getApplication());

       /*if (!(profileTypeId.equals("4") || profileTypeId.equals("5"))) {
            binding.associationLayout.setVisibility(View.GONE);
        }*/

    }
}