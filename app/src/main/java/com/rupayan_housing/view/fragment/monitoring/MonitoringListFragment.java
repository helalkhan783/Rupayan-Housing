package com.rupayan_housing.view.fragment.monitoring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerListAdapter;
import com.rupayan_housing.adapter.CustomerTrashListAdapter;
import com.rupayan_housing.adapter.SupplierListAdapter;
import com.rupayan_housing.adapter.SupplierTrashListAdapter;
import com.rupayan_housing.databinding.FragmentMonitoringListBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CountryListResponse;
import com.rupayan_housing.serverResponseModel.CustoerTrashList;
import com.rupayan_housing.serverResponseModel.CustomerListModel;
import com.rupayan_housing.serverResponseModel.CustomerListResponse;
import com.rupayan_housing.serverResponseModel.CustomerTrashListResponse;
import com.rupayan_housing.serverResponseModel.DistrictListResponse;
import com.rupayan_housing.serverResponseModel.DivisionResponse;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.MonitoringModel;
import com.rupayan_housing.serverResponseModel.Product;
import com.rupayan_housing.serverResponseModel.SupplierList;
import com.rupayan_housing.serverResponseModel.SupplierListResponse;
import com.rupayan_housing.serverResponseModel.SupplierTrashList;
import com.rupayan_housing.serverResponseModel.SupplierTrashListResponse;
import com.rupayan_housing.serverResponseModel.TypeList;
import com.rupayan_housing.serverResponseModel.ZoneListt;
import com.rupayan_housing.utils.CustomersUtil;
import com.rupayan_housing.utils.InternetCheckerRecyclerBuddy;
import com.rupayan_housing.utils.MonitoringUtil;
import com.rupayan_housing.utils.SupplierUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.DashboardFragment;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationPageDataResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportAssociationList;
import com.rupayan_housing.view.fragment.customers.CustomerInterface;
import com.rupayan_housing.viewModel.BrandViewModel;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.CustomerViewModel;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.MonitoringViewModel;
import com.rupayan_housing.viewModel.SupplierViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class MonitoringListFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CustomerInterface {
    private FragmentMonitoringListBinding binding;
    private MonitoringViewModel monitoringViewModel;
    private CustomerViewModel customerViewModel;
    private SupplierViewModel supplierViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    private BrandViewModel brandViewModel;
    private ReconciliationReportViewModel reconciliationViewModel;


    /**
     * for  customer filter division and District
     */
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private List<DivisionResponse> divisionResponseList;
    private List<String> divisionNameList;

    /**
     * fro supplier
     */
    private List<String> supplierTypeList;
    private List<TypeList> typeLists;

    private List<DistrictListResponse> districtListResponseList;
    private List<String> districtNameList;

    private String portion;
    private ProgressDialog progressDialog;
    /**
     * country for supplier list
     */
    private List<CountryListResponse> countryListResponseList;
    /**
     * district for supplier list
     */
    List<DistrictListResponse> supplierDistrictList;

    /**
     * for pagination
     */

    private boolean isIssuingDate = false;
    private boolean isCirtificateDate = false;

    //private List<Product> productList;

    private List<ListMonitorModel> monitorLists = new ArrayList<>();
    private List<CustomerListModel> customerLists = new ArrayList<>();
    private List<CustoerTrashList> customerTrashLists = new ArrayList<>();
    private List<SupplierList> supplierLists = new ArrayList<>();
    private List<SupplierTrashList> supplierTrashLists = new ArrayList<>();


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;
    private boolean endScroll = false;
    private LinearLayoutManager linearLayoutManager;


    /**
     * for monitoring type
     */
    List<String> monitoringType;
    List<ReconciliationReportAssociationList> zoneListts;


    private String startDate, endDate, publishedDate, monitorTypeId; //  for monitoring filter variable
    private String zoneId, customerDistrictId, customerDivisionId, supplierType, supplierCountryId, supplierDistrictId;//for supplier and customer


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monitoring_list, container, false);

        monitoringViewModel = new ViewModelProvider(this).get(MonitoringViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        supplierViewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        brandViewModel = new ViewModelProvider(this).get(BrandViewModel.class);
        reconciliationViewModel = new ViewModelProvider(this).get(ReconciliationReportViewModel.class);


        /** click handle */
        onClick();

        /** get Division*/
        getDivisionData();
        getPAgeDataFromViewModel();
        /**
         * get Data From Previous Fragment
         */


        binding.toolbar.setClickHandle(() -> {
            pageNumber = 1;
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });


/** for pagination */
        binding.monitoringRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (endScroll) {
                                return;
                            }
                            loading = false;
                            pageNumber += 1;

                            getAllListFromServer();

                            loading = true;
                        }
                    }
                }
            }
        });


        /**
         * Now handle division item click
         */
        binding.customerFilter.division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerDivisionId = divisionResponseList.get(position).getDivisionId();

                /**
                 * Now get district list based on division id
                 */
                getDistrictListByDivisionId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.monitorFilter.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zoneId = zoneListts.get(position).getZoneID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * Nw handle district item click
         */
        binding.customerFilter.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerDistrictId = districtListResponseList.get(position).getDistrictId();

                /**
                 * Now get Thana list based on district
                 */

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.monitorFilter.monitoringType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monitorTypeId = monitoringType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /** for supplier */

        binding.customerFilter.selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplierType = String.valueOf(typeLists.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.customerFilter.trashCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplierCountryId = countryListResponseList.get(position).getCountryID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.customerFilter.trashDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplierDistrictId = supplierDistrictList.get(position).getDistrictId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void getAllListFromServer() {
        if (!new InternetCheckerRecyclerBuddy(getActivity()).isInternetAvailableHere(binding.monitoringRv, binding.noDataFound)) {
            return;
        }
        if (pageNumber == 1) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        if (pageNumber > 1) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.progress.setProgress(20);
            binding.progress.setMax(100);
        }


        /**
         * For show Monitoring list
         */
        if (portion.equals(MonitoringUtil.monitoringHistory)) {
            binding.toolbar.toolbarTitle.setText("Monitoring List");
            getMonitoringList();
            return;
        }


/**
 * For show Customer list from server
 */
        if (portion.equals(CustomersUtil.customerList)) {
            binding.toolbar.toolbarTitle.setText("Customer List");
            getCustomerListFromServer();
            return;
        }

/**
 * For show Customer trash list from server
 */
        if (portion.equals(CustomersUtil.customerTrashList)) {
            binding.toolbar.toolbarTitle.setText("Customer Trash List");
            getCustomerTrashListFromServer();
            return;
        }

/**
 * supplier list
 */
        if (portion.equals(SupplierUtils.supplierList)) {
            binding.customerFilter.supplierSelect.setVisibility(View.VISIBLE);
            binding.customerFilter.trashLayout.setVisibility(View.VISIBLE);
            binding.customerFilter.customerLayout.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText("Supplier List");

            hideKeyboard(getActivity());
            getData();
            return;
        }
        /**
         * supplier list
         */
        if (portion.equals(SupplierUtils.supplierTrashList)) {
            binding.customerFilter.supplierSelect.setVisibility(View.VISIBLE);
            binding.toolbar.toolbarTitle.setText("Supplier Trash List");
            getTrashData();
            return;

        }
    }

    private void getTrashData() {
        supplierViewModel.getSupplierTrashList(getActivity(), String.valueOf(pageNumber), supplierType, customerDivisionId, customerDistrictId).observe(getViewLifecycleOwner(), new Observer<SupplierTrashListResponse>() {
            @Override
            public void onChanged(SupplierTrashListResponse response) {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);
                try {

                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        /** set supplier type */
                        setTypeInSpinner(response.getTypeList());
                        if (response.getLists() == null || response.getLists().isEmpty()) {
                            managePaginationAndFilter();
                        } else {
                            manageFilterBtnAndRvAndDataNotFound();
                            supplierTrashLists.addAll(response.getLists());
                            SupplierTrashListAdapter adapter = new SupplierTrashListAdapter(getActivity(), supplierTrashLists);
                            binding.monitoringRv.setLayoutManager(linearLayoutManager);
                            binding.monitoringRv.setHasFixedSize(true);
                            binding.monitoringRv.setAdapter(adapter);
                        }
                    }

                } catch (Exception e) {
                }

            }
        });
    }

    private void getData() {
        supplierViewModel.getSupplierList(getActivity(), String.valueOf(pageNumber), supplierType, supplierCountryId, supplierDistrictId).observe(getViewLifecycleOwner(), new Observer<SupplierListResponse>() {
            @Override
            public void onChanged(SupplierListResponse response) {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);
                try {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    if (response.getStatus() == 200) {
                        if (response.getLists() == null || response.getLists().isEmpty()) {
                            managePaginationAndFilter();
                        } else {
                            manageFilterBtnAndRvAndDataNotFound();
                            supplierLists.addAll(response.getLists());
                            SupplierListAdapter adapter = new SupplierListAdapter(getActivity(), supplierLists, getView(), MonitoringListFragment.this);
                            binding.monitoringRv.setLayoutManager(linearLayoutManager);
                            binding.monitoringRv.setHasFixedSize(true);
                            binding.monitoringRv.setAdapter(adapter);

                            /** set supplier type */
                            setTypeInSpinner(response.getTypeList());
                            /** for supplier country */
                            setSupplierCountry(response.getCountryList());
                            /** for supplier district */
                            setSupplierDistrict(response.getDistrictList());
                        }
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }
        });
    }

    private void setTypeInSpinner(List<TypeList> list) {
        supplierTypeList = new ArrayList<>();
        supplierTypeList.clear();
        typeLists = new ArrayList<>();
        typeLists.clear();
        typeLists.addAll(list);

        for (int i = 0; i < typeLists.size(); i++) {
            supplierTypeList.add("" + typeLists.get(i).getName());
            if (supplierType != null) {
                if (supplierType == String.valueOf(typeLists.get(i).getId())) {
                    binding.customerFilter.selectType.setSelection(i);
                }
            }


        }
        binding.customerFilter.selectType.setItem(supplierTypeList);

    }

    private void setSupplierDistrict(List<DistrictListResponse> districtList) {
        List<String> supplierDistrictName = new ArrayList<>();
        supplierDistrictName.clear();
        supplierDistrictList = new ArrayList<>();
        supplierDistrictList.clear();
        supplierDistrictList.addAll(districtList);
        for (int i = 0; i < districtList.size(); i++) {
            supplierDistrictName.add("" + districtList.get(i).getName());
            if (supplierDistrictId != null) {
                if (supplierDistrictId.equals(districtList.get(i).getDistrictId())) {
                    binding.customerFilter.trashDistrict.setSelection(i);
                }
            }

        }
        binding.customerFilter.trashDistrict.setItem(supplierDistrictName);
    }

    private void setSupplierCountry(List<CountryListResponse> countryList) {
        List<String> countryNameList = new ArrayList<>();
        countryNameList.clear();
        countryListResponseList = new ArrayList<>();
        countryListResponseList.clear();
        try {
            countryListResponseList.addAll(countryList);
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
        for (int i = 0; i < countryList.size(); i++) {
            countryNameList.add("" + countryList.get(i).getCountryName());
            if (supplierCountryId != null) {
                if (countryListResponseList.get(i).getCountryID().equals(supplierCountryId)) {
                    binding.customerFilter.trashCountry.setSelection(i);
                }
            }
        }
        binding.customerFilter.trashCountry.setItem(countryNameList);
    }


    private void getCustomerTrashListFromServer() {
        customerViewModel.getCustomerTrashList(getActivity(), String.valueOf(pageNumber), customerDivisionId, customerDistrictId).observe(getViewLifecycleOwner(), new Observer<CustomerTrashListResponse>() {
            @Override
            public void onChanged(CustomerTrashListResponse response) {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);
                if (response == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (response.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                    return;
                }
                if (response.getStatus() == 200) {
                    if (response.getLists().isEmpty() || response.getLists() == null) {
                        managePaginationAndFilter();

                    } else {
                        manageFilterBtnAndRvAndDataNotFound();
                        customerTrashLists.addAll(response.getLists());
                        CustomerTrashListAdapter adapter = new CustomerTrashListAdapter(getActivity(), customerTrashLists);
                        binding.monitoringRv.setLayoutManager(linearLayoutManager);
                        binding.monitoringRv.setHasFixedSize(true);
                        binding.monitoringRv.setAdapter(adapter);
                    }
                }

            }
        });
    }


    private void getCustomerListFromServer() {
        customerViewModel.getCustomerList(getActivity(), String.valueOf(pageNumber), binding.customerFilter.searchByName.getText().toString(), customerDivisionId, customerDistrictId).observe(getViewLifecycleOwner(), new Observer<CustomerListResponse>() {
            @Override
            public void onChanged(CustomerListResponse customerListResponse) {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);

                if (customerListResponse == null) {
                    errorMessage(getActivity().getApplication(), "something wrong");
                    return;
                }
                if (customerListResponse.getStatus() == 400) {
                    infoMessage(getActivity().getApplication(), customerListResponse.getMessage());
                    return;
                }
                if (customerListResponse.getStatus() == 200) {
                    if (customerListResponse.getCustomerLists() == null || customerListResponse.getCustomerLists().isEmpty()) {
                        managePaginationAndFilter();

                    } else {
                        manageFilterBtnAndRvAndDataNotFound();
                        customerLists.addAll(customerListResponse.getCustomerLists());
                        CustomerListAdapter adapter = new CustomerListAdapter(getActivity(), customerLists, getView(), MonitoringListFragment.this);
                        binding.monitoringRv.setLayoutManager(linearLayoutManager);
                        binding.monitoringRv.setHasFixedSize(true);
                        binding.monitoringRv.setAdapter(adapter);
                    }
                }


            }
        });


    }


    private void getDataFromPreviousFragment() {
        assert getArguments() != null;
        portion = getArguments().getString("portion");


        if (DashboardFragment.manageMonitor) {
            binding.monitorFilter.startDate.setText("" + getArguments().getString("startDate"));
            binding.monitorFilter.EndDate.setText("" + getArguments().getString("endDate"));
        }

    }

    private void getMonitoringList() {
        monitoringViewModel.getMonitoringList(getActivity(), String.valueOf(pageNumber), binding.monitorFilter.startDate.getText().toString(),
                binding.monitorFilter.EndDate.getText().toString(), binding.monitorFilter.publishedDateforFilter.getText().toString(),
                monitorTypeId, zoneId)
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    binding.progress.setVisibility(View.GONE);
                    if (response == null) {
                        //errorMessage(getActivity().getApplication(), "Something wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), response.getMessage());
                        return;
                    }

                    if (response.getStatus() == 200) {
                        binding.toolbar.filterBtn.setVisibility(View.VISIBLE);
                        if (response.getLists() == null || response.getLists().isEmpty()) {
                            managePaginationAndFilter();
                        } else {
                            manageFilterBtnAndRvAndDataNotFound();

                            try {
                                monitorLists.addAll(response.getLists());
                                // MonitoringListAdapter adapter = new MonitoringListAdapter(getActivity(), monitorLists, response.getMonitoringType(), getView());

                                MyAdapter adapter1 = new MyAdapter(getActivity(), monitorLists, response.getMonitoringType(), getView());
                                binding.monitoringRv.setLayoutManager(linearLayoutManager);
                                binding.monitoringRv.setHasFixedSize(true);
                                binding.monitoringRv.setAdapter(adapter1);

                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }

                            /** for monitoring type*/
                            monitoringType = new ArrayList<>();
                            monitoringType.clear();
                            for (int i = 0; i < response.getMonitoringType().size(); i++) {
                                monitoringType.add("" + response.getMonitoringType().get(i).getMonitoringTypeName());
                            }
                            binding.monitorFilter.monitoringType.setItem(monitoringType);




                        }
                    }

                });
    }

    private void getPAgeDataFromViewModel() {
        reconciliationViewModel.getReconciliationPageData(getActivity(), getProfileId(getActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<ReconciliationPageDataResponse>() {
            @Override
            public void onChanged(ReconciliationPageDataResponse response) {

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
                        /** for Association */


                        List<String> zoneNameList = new ArrayList<>();

                        zoneListts = new ArrayList<>();
                        zoneListts.addAll(response.getAssociationList());

                        for (int i = 0; i < zoneListts.size(); i++) {
                            zoneNameList.add("" + zoneListts.get(i).getZoneName() + "/" + zoneListts.get(i).getDisplayName());

                        }
                        binding.monitorFilter.zone.setItem(zoneNameList);


                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }


    private void onClick() {
        binding.toolbar.filterBtn.setOnClickListener(this);
        /** for monitor */
        binding.monitorFilter.monitorSearchBtn.setOnClickListener(this);
        binding.monitorFilter.monitorResetBtn.setOnClickListener(this);
        binding.monitorFilter.startDate.setOnClickListener(this);
        binding.monitorFilter.EndDate.setOnClickListener(this);
        binding.monitorFilter.publishedDateforFilter.setOnClickListener(this);
        /** for customer */
        binding.customerFilter.customerSearchBtn.setOnClickListener(this);
        binding.customerFilter.customerResetBtn.setOnClickListener(this);
        binding.customerFilter.searchByNameBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startDate:
                isIssuingDate = true;
                timePicker();

                break;
            case R.id.EndDate:
                isCirtificateDate = true;
                timePicker();
                break;

            case R.id.publishedDateforFilter:
                timePicker();
                break;

            case R.id.publishDate:
                timePicker();
                break;

            case R.id.filterBtn:

                if (portion.equals(MonitoringUtil.monitoringHistory)) {

                    if (binding.monitoringFilterLayout.isExpanded()) {
                        binding.monitoringFilterLayout.setExpanded(false);
                        return;
                    }
                    binding.monitoringFilterLayout.setExpanded(true);
                }

                if (portion.equals(CustomersUtil.customerList) || portion.equals(CustomersUtil.customerTrashList)) {

                    if (binding.customerFilterlayout.isExpanded()) {
                        binding.customerFilterlayout.setExpanded(false);
                        return;
                    }
                    binding.customerFilterlayout.setExpanded(true);
                }

                if (portion.equals(SupplierUtils.supplierTrashList) || portion.equals(SupplierUtils.supplierList)) {

                    if (binding.customerFilterlayout.isExpanded()) {
                        binding.customerFilterlayout.setExpanded(false);
                        return;
                    } else {
                        binding.customerFilterlayout.setExpanded(true);
                    }


                }

                break;
            /** for customer */

            case R.id.searchByNameBtn:
                pageNumber = 1;
                //for filter
                clearAllList();
                //for filter
                isFirstLoad = 0;
                getAllListFromServer();
                break;
            case R.id.customerSearchBtn:
                pageNumber = 1;
                //for filter
                clearAllList();

                getAllListFromServer();

                break;

            case R.id.customerResetBtn:
                pageNumber = 1;
                customerDivisionId = null;
                customerDistrictId = null;
                binding.customerFilter.searchByName.setText("");
                binding.customerFilter.division.clearSelection();
                binding.customerFilter.district.clearSelection();
                /** for supplier list */
                supplierType = null;
                supplierCountryId = null;
                supplierDistrictId = null;

                //for filter
                clearAllList();

                if (supplierType == null && supplierCountryId == null && supplierDistrictId == null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.monitoringRv.setVisibility(View.VISIBLE);
                    getAllListFromServer();
                    return;
                }

                if (supplierType == null && customerDivisionId == null && customerDistrictId == null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.monitoringRv.setVisibility(View.VISIBLE);
                    getAllListFromServer();
                    return;
                }

                if (customerDivisionId == null && customerDistrictId == null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.monitoringRv.setVisibility(View.VISIBLE);
                    getAllListFromServer();
                    return;
                }
                break;


            /** for monitor */
            case R.id.monitorSearchBtn:
                pageNumber = 1;

                //for filter
                isFirstLoad = 0;
                monitorLists.clear();
                getAllListFromServer();
                break;
            case R.id.monitorResetBtn:
                pageNumber = 1;
                startDate = null;
                endDate = null;
                publishedDate = null;
                binding.monitorFilter.startDate.setText("");
                binding.monitorFilter.EndDate.setText("");
                binding.monitorFilter.publishedDateforFilter.setText("");
                monitorTypeId = null;
                zoneId = null;
                binding.monitorFilter.zone.clearSelection();

                // for filter
                isFirstLoad = 0;
                monitorLists.clear();

                if (startDate == null && endDate == null && publishedDate == null && monitorTypeId == null) {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.monitoringRv.setVisibility(View.VISIBLE);
                    getAllListFromServer();
                }
                break;
        }
    }

    private void clearAllList() {
        isFirstLoad = 0;
        customerLists.clear();
        customerTrashLists.clear();
        supplierLists.clear();
        supplierTrashLists.clear();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        int month = monthOfYear;
//        if (month == 12) {
//            month = 1;
//        } else {
//            month = monthOfYear + 1;
//        }
//
//        String mainMonth, mainDay;
//
//
//        if (month <= 9) {
//            mainMonth = "0" + month;
//        } else {
//            mainMonth = String.valueOf(month);
//        }
//        if (dayOfMonth <= 9) {
//            mainDay = "0" + dayOfMonth;
//        } else {
//            mainDay = String.valueOf(dayOfMonth);
//        }
//        String selectedDate = year + "-" + mainMonth + "-" + mainDay;//set the selected date
//
//
//        if (!isStartDate && number == 0) {
//            binding.monitorFilter.EndDate.setText(selectedDate);
//            endDate = binding.monitorFilter.EndDate.getText().toString();
//            number =1;
//        }
//        if (!isStartDate && number == 1) {
//            binding.monitorFilter.publishedDate.setText(selectedDate);
//            number = 0 ;
//        }
//        else {
//            binding.monitorFilter.startDate.setText(selectedDate);
//            startDate = binding.monitorFilter.startDate.getText().toString();
//            isStartDate = false;
//        }

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


        if (isIssuingDate) {
            binding.monitorFilter.startDate.setText(selectedDate);
            binding.monitorFilter.startDate.setError(null);
            isIssuingDate = false;
            return;
        }
        if (isCirtificateDate) {
            binding.monitorFilter.EndDate.setText(selectedDate);
            binding.monitorFilter.EndDate.setError(null);
            isCirtificateDate = false;
            return;
        }

        binding.monitorFilter.publishedDateforFilter.setText(selectedDate);
        binding.monitorFilter.publishedDateforFilter.setError(null);
        return;
    }

    private void managePaginationAndFilter() {
        if (isFirstLoad == 0) { // if filter time list is null.  so then, data_not_found will be visible
            binding.monitoringRv.setVisibility(View.GONE);
            binding.noDataFound.setVisibility(View.VISIBLE);
            return;
        }
        if (isFirstLoad > 0) {//for scrolling off
            endScroll = true;//means scroll off
            pageNumber -= 1;
            return;
        }
        return;
    }

    private void manageFilterBtnAndRvAndDataNotFound() {
        isFirstLoad += 1;
        binding.toolbar.filterBtn.setVisibility(View.VISIBLE);
        //for filter
        // sometime filter list data came null when, data_not_found have visible,
        // And again search comes data in list by the others filter parameter.that for recycler view visible
        binding.noDataFound.setVisibility(View.GONE);
        binding.monitoringRv.setVisibility(View.VISIBLE);
    }

    private void timePicker() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        assert getFragmentManager() != null;
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    private void getDivisionData() {

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        /**
         * For get Division list from This Api
         */
        millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }


                    /**
                     * now set division list
                     */
                    try {
                        divisionResponseList = new ArrayList<>();
                        divisionResponseList.clear();
                        divisionResponseList.addAll(response.getDivisions());

                        divisionNameList = new ArrayList<>();
                        divisionNameList.clear();

                        for (int i = 0; i < response.getDivisions().size(); i++) {
                            divisionNameList.add(response.getDivisions().get(i).getName());
                            if (customerDivisionId != null) {
                                if (customerDivisionId.equals(response.getDivisions().get(i).getDivisionId())) {
                                    binding.customerFilter.division.setSelection(i);
                                }
                            }

                        }
                        binding.customerFilter.division.setItem(divisionNameList);
                    } catch (Exception e) {
                    }
                });


    }

    private void getDistrictListByDivisionId() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }

        millerProfileInfoViewModel.getDistrictListByDivisionId(getActivity(), customerDivisionId)
                .observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        return;
                    }
                    if (response.getStatus() != 200) {
                        return;
                    }
                    districtListResponseList = new ArrayList<>();
                    districtListResponseList.clear();
                    districtNameList = new ArrayList<>();
                    districtNameList.clear();

                    districtListResponseList.addAll(response.getLists());

                    for (int i = 0; i < response.getLists().size(); i++) {
                        districtNameList.add("" + response.getLists().get(i).getName());
                        if (customerDistrictId != null) {
                            if (customerDistrictId.equals(response.getLists().get(i).getDistrictId())) {
                                binding.customerFilter.district.setSelection(i);
                            }
                        }

                    }
                    binding.customerFilter.district.setItem(districtNameList);
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        monitorLists.clear();
        customerLists.clear();
        customerTrashLists.clear();
        supplierLists.clear();
        supplierTrashLists.clear();
        pageNumber = 1;
        isFirstLoad = 0;
        progressDialog.dismiss();
        getDataFromPreviousFragment();

        /**
         * get all list data
         */
        getAllListFromServer();


        try {
            updateCurrentUserPermission(getActivity());
            clearAllList();
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }

    }

    public void updateCurrentUserPermission(FragmentActivity activity) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        currentPermissionViewModel.getCurrentUserRealtimePermissions(
                PreferenceManager.getInstance(activity).getUserCredentials().getToken(),
                PreferenceManager.getInstance(activity).getUserCredentials().getUserId()
        ).observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                Toasty.error(activity, "Something Wrong", Toasty.LENGTH_LONG).show();
                return;
            }
            if (response.getStatus() == 400) {
                Toasty.info(activity, "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                return;
            }
            try {
                LoginResponse loginResponse = PreferenceManager.getInstance(activity).getUserCredentials();
                if (loginResponse != null) {
                    loginResponse.setPermissions(response.getMessage());
                    loginResponse.setToken(response.getToken());
                    PreferenceManager.getInstance(activity).saveUserCredentials(loginResponse);
                }
            } catch (Exception e) {
                infoMessage(getActivity().getApplication(), "" + e.getMessage());
                Log.d("ERROR", "" + e.getMessage());
            }
        });
    }

    @Override
    public void getPosition(int position, String id) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to delete customer ?");//set warning title
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
            brandViewModel.deleCustomer(getActivity(), id).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
                @Override
                public void onChanged(DuePaymentResponse response) {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    pageNumber = 1;
                    customerLists.clear();
                    getCustomerListFromServer();
                    binding.monitoringRv.getAdapter().notifyDataSetChanged();
                }
            });

        });

        alertDialog.show();
    }

    @Override
    public void getSupplierPosition(int position, String id) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());

        @SuppressLint("InflateParams")
        View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.purchase_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Do You Want to delete supplier ?");//set warning title
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
            brandViewModel.deleCustomer(getActivity(), id).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
                @Override
                public void onChanged(DuePaymentResponse response) {
                    if (response == null) {
                        errorMessage(getActivity().getApplication(), "Something Wrong");
                        return;
                    }
                    if (response.getStatus() == 400) {
                        infoMessage(getActivity().getApplication(), "" + response.getMessage());
                        return;
                    }
                    successMessage(getActivity().getApplication(), "" + response.getMessage());
                    pageNumber = 1;
                    supplierLists.clear();
                    getData();
                    binding.monitoringRv.getAdapter().notifyDataSetChanged();
                }
            });

        });

        alertDialog.show();
    }

    @Override
    public void getDataForEditStore(int position, String enterPriseId, String storeFullName, String storeShortname, String storeAddress, String storeId) {

    }


}