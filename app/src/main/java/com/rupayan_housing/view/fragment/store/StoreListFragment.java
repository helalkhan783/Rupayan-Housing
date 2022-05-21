package com.rupayan_housing.view.fragment.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.EnterPriseAdapterForStore;
import com.rupayan_housing.adapter.StockDetailsAdapter;
import com.rupayan_housing.adapter.StockInformationListAdapter;
import com.rupayan_housing.adapter.StoreListAdapter;
import com.rupayan_housing.databinding.AddStoreDialogLayoutBinding;
import com.rupayan_housing.databinding.FragmentStoreListBinding;
import com.rupayan_housing.dialog.MyApplication;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DuePaymentResponse;
import com.rupayan_housing.serverResponseModel.EnterpriseResponse;
import com.rupayan_housing.serverResponseModel.Enterprize;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.PurchaseReportResponse;
import com.rupayan_housing.serverResponseModel.ReportPurchaseBrandList;
import com.rupayan_housing.serverResponseModel.ReportPurchaseCategoryList;
import com.rupayan_housing.serverResponseModel.StockList;
import com.rupayan_housing.serverResponseModel.StockStoreList;
import com.rupayan_housing.serverResponseModel.ZoneResponse;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.SettingsUtil;
import com.rupayan_housing.utils.StockUtils;
import com.rupayan_housing.view.fragment.BaseFragment;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationPageDataResponse;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.page_data_response.ReconciliationReportAssociationList;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.purchase_report.get_miller_by_association.PurchaseMillerList;
import com.rupayan_housing.view.fragment.customers.CustomerInterface;
import com.rupayan_housing.view.fragment.customers.SwitchInterface;
import com.rupayan_housing.view.fragment.store.list_response.StoreListViewModel;
import com.rupayan_housing.view.fragment.store.list_response.StoreLst;
import com.rupayan_housing.viewModel.BrandViewModel;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.MillerProfileInfoViewModel;
import com.rupayan_housing.viewModel.SalesRequisitionViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.ReconciliationReportViewModel;
import com.rupayan_housing.viewModel.report_all_view_model.ReportViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class StoreListFragment extends BaseFragment implements View.OnClickListener, CustomerInterface, SwitchInterface {
    private FragmentStoreListBinding binding;
    private StoreListViewModel storeListViewModel;
    private CurrentPermissionViewModel currentPermissionViewModel;
    String porsion, productId;
    List<StockStoreList> stockStoreLists;
    List<String> storeNameList;
    List<Enterprize> enterprizeList;
    private MillerProfileInfoViewModel millerProfileInfoViewModel;
    private BrandViewModel brandViewModel;
    private ReconciliationReportViewModel reconciliationViewModel;

    /**
     * for zone
     */
    private List<ReconciliationReportAssociationList> zoneResponseList;
    private List<String> zoneList;
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


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1, isFirstLoad = 0;

    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;
    List<StoreLst> storeLst;
    public static boolean endScroll = false;
    List<StoreLst> storeLists = new ArrayList<>();
    List<StockList> stockLists = new ArrayList<>();

    public static int manage = 0;
    private ReportViewModel reportViewModel;
    private SalesRequisitionViewModel salesRequisitionViewModel;
    /**
     * For enterprise
     */
    List<EnterpriseResponse> enterpriseResponseList;
    List<String> enterpriseNameList;
    String storeSelectedId, zoneSelectId, enterpriseId, brandId, categoryId;
    //for store edit
    String enterPriseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_list, container, false);

        salesRequisitionViewModel = new ViewModelProvider(this).get(SalesRequisitionViewModel.class);
        storeListViewModel = new ViewModelProvider(this).get(StoreListViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        millerProfileInfoViewModel = new ViewModelProvider(this).get(MillerProfileInfoViewModel.class);
        brandViewModel = new ViewModelProvider(this).get(BrandViewModel.class);
        reconciliationViewModel = new ViewModelProvider(this).get(ReconciliationReportViewModel.class);

        binding.toolbar.setClickHandle(() -> {
            hideKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        setOnClick();
        getPAgeDataFromViewModel();
        binding.stockFilter.zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zoneSelectId = zoneResponseList.get(position).getZoneID();
             }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.stockFilter.enterprise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enterpriseId = enterpriseResponseList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.stockFilter.store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeSelectedId = stockStoreLists.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.stockFilter.selectBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandId = brandLists.get(position).getBrandID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.stockFilter.selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = purchaseCategoryLists.get(position).getCategoryID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** for pagination **/
        binding.storeRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        return binding.getRoot();
    }

    private void getStockDetails() {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        storeListViewModel.stockDetails(getActivity(), productId).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            if (response == null) {
                 return;
            }
            if (response.getStatus() == 400) {
                 return;
            }
            if (response.getStatus() == 200) {
                binding.stockDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                StockDetailsAdapter adapter = new StockDetailsAdapter(getActivity(), response.getStockDetails());
                binding.stockDetailsRecyclerView.setAdapter(adapter);
            }
        });

    }

    public void getAllListFromServer() {
        if (porsion.equals("stockDetails")) {
            try {
                binding.toolbar.addBtn.setVisibility(View.GONE);
                binding.storeRv.setVisibility(View.GONE);
                binding.stockDetailsRecyclerView.setVisibility(View.VISIBLE);
                getStockDetails();
            } catch (Exception e) {
            }
            return;
        }
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
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
        if (porsion.equals(StockUtils.stockInfo)) {
            try {
                getStockList();
                getPageData();
            } catch (Exception e) {
            }
            binding.toolbar.filterBtn.setVisibility(View.VISIBLE);
        }
        if (porsion.equals(SettingsUtil.storeList)) {
            binding.toolbar.addBtn.setVisibility(View.VISIBLE);
            binding.storeManagementLayout.setVisibility(View.VISIBLE);
            getStoreListFromServer();
        }
        return;
    }

    private void getPageData() {

        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        // for enterprise
        salesRequisitionViewModel.getEnterpriseResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                         return;
                    }
                    if (response.getStatus() != 200) {
                         return;
                    }

                    enterpriseResponseList = new ArrayList<>();
                    enterpriseResponseList.clear();
                    enterpriseNameList = new ArrayList<>();
                    enterpriseNameList.clear();
                    enterpriseResponseList.addAll(response.getEnterprise());

                    for (int i = 0; i < response.getEnterprise().size(); i++) {
                        enterpriseNameList.add("" + response.getEnterprise().get(i).getStoreName());
                        if (enterpriseId != null) {
                            if (enterpriseId.equals(response.getEnterprise().get(i).getStoreID())) {
                                binding.stockFilter.enterprise.setSelection(i);
                            }
                        }
                    }
                    binding.stockFilter.enterprise.setItem(enterpriseNameList);
                });


        reportViewModel.getPurchaseReportPageData(getActivity(), getProfileId(requireActivity().getApplication())).observe(getViewLifecycleOwner(), new Observer<PurchaseReportResponse>() {
            @Override
            public void onChanged(PurchaseReportResponse response) {
                if (response == null) {
                     return;
                }
                if (response.getStatus() == 400) {
                     return;
                }
                if (response.getStatus() == 200) {
                    setDataInAssociationSpinner(response);
                }
            }
        });
// for page  store List data
        try {
            storeListViewModel.getStockList(getActivity(), null, "", null, null, null, null, null, null).observe(getViewLifecycleOwner(), response -> {

                if (response == null) {
                     return;
                }
                if (response.getStatus() == 400) {
                     return;
                }
                if (response.getLists().isEmpty() || response.getLists() == null) {
                    managePaginationAndFilter();
                    return;
                }
                stockStoreLists = new ArrayList<>();
                stockStoreLists.clear();
                stockStoreLists.addAll(response.getStoreList());
                storeNameList = new ArrayList<>();
                storeNameList.clear();

                try {
                    for (int i = 0; i < stockStoreLists.size(); i++) {
                        storeNameList.add("" + stockStoreLists.get(i).getFullName());
                        if (storeSelectedId != null) {
                            if (stockStoreLists.get(i).getStoreID().equals(storeSelectedId)) {
                                binding.stockFilter.store.setSelection(i);
                            }
                        }
                    }
                    binding.stockFilter.store.setItem(storeNameList);

                } catch (Exception e) {
                }
            });

        } catch (Exception e) {
        }




        // for zone list
        millerProfileInfoViewModel.getProfileInfoResponse(getActivity())
                .observe(getViewLifecycleOwner(), response -> {
                    progressDialog.dismiss();
                    if (response == null) {
                         return;
                    } if (response.getStatus() == 400) {
                         return;
                    }
              /*     try {
                       *//** for zone *//*
                       zoneResponseList = new ArrayList<>();
                       zoneResponseList.clear();
                       zoneList = new ArrayList<>();
                       zoneList.clear();
                       zoneResponseList.addAll(response.getZones());

                       for (int i = 0; i < response.getZones().size(); i++) {
                           zoneList.add("" + response.getZones().get(i).getZoneName());
                       }
                       binding.stockFilter.zone.setItem(zoneList);
                   }catch (Exception e){}

*/
                });


    }

    private void setDataInAssociationSpinner(PurchaseReportResponse response) {

        /** for category List */
        categoryNameList = new ArrayList<>();
        categoryNameList.clear();
        purchaseCategoryLists = new ArrayList<>();
        purchaseCategoryLists.clear();
        purchaseCategoryLists.addAll(response.getCategoryList());

        try {
            for (int i = 0; i < response.getCategoryList().size(); i++) {
                categoryNameList.add("" + response.getCategoryList().get(i).getCategory());
                if (categoryId != null) {
                    if (categoryId.equals(response.getCategoryList().get(i).getCategoryID())) {
                        binding.stockFilter.selectCategory.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {
        }
        binding.stockFilter.selectCategory.setItem(categoryNameList);


        /** for brandList */
        brandNameList = new ArrayList<>();
        brandNameList.clear();

        brandLists = new ArrayList<>();
        brandLists.clear();
        brandLists.addAll(response.getBrandList());

        try {
            for (int i = 0; i < response.getBrandList().size(); i++) {
                brandNameList.add("" + response.getBrandList().get(i).getBrandName());
                if (brandId != null) {
                    if (brandId.equals(response.getBrandList().get(i).getBrandID())) {
                        binding.stockFilter.selectBrand.setSelection(i);
                    }
                }
            }
        } catch (Exception e) {
        }
        binding.stockFilter.selectBrand.setItem(brandNameList);


        millerNameList = new ArrayList<>();
        millerNameList.clear();
        millerLists = new ArrayList<>();
        millerLists.clear();


    }

    private void getStoreListFromServer() {
        storeListViewModel.getStoreList(getActivity(), String.valueOf(pageNumber)).observe(getViewLifecycleOwner(), response -> {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);

            if (response == null) {
                 return;
            }
            if (response.getStatus() == 400) {
                 return;
            }

            // for enter prise list
            enterprizeList = new ArrayList<>();
            enterprizeList.addAll(response.getEnterprizeList());
            if (!response.getEnterprizeList().isEmpty()) {
                EnterPriseAdapterForStore adapter = new EnterPriseAdapterForStore(getActivity(), response.getEnterprizeList(), this);
                binding.enterPriseList.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.enterPriseList.setAdapter(adapter);
            }

            if (response.getEnterprizeList().isEmpty() || response.getEnterprizeList() == null) {
                binding.enterPriseList.setVisibility(View.GONE);
                binding.enterPriseListNotFound.setVisibility(View.VISIBLE);
            }


            // for store list
            if (response.getStoreLst().isEmpty() || response.getStoreLst() == null) {
                if (isFirstLoad > 0) {
                    endScroll = true;
                    pageNumber -= 1;
                    return;
                }
                binding.storeRv.setVisibility(View.GONE);
                binding.storeListDataNoFoundTv.setVisibility(View.VISIBLE);
                return;
            }

            isFirstLoad += 1;
            linearLayoutManager = new LinearLayoutManager(getContext());
            storeLists.addAll(response.getStoreLst());
            binding.storeRv.setLayoutManager(linearLayoutManager);
            StoreListAdapter storeListAdapter = new StoreListAdapter(getActivity(), storeLists, StoreListFragment.this);
            binding.storeRv.setAdapter(storeListAdapter);
        });

    }

    private void getStockList() {
        if (manage == 0) {
            storeListViewModel.getStockList(getActivity(), String.valueOf(pageNumber), "", storeSelectedId, brandId, categoryId, zoneSelectId, binding.stockFilter.itemNameEt.getText().toString(), enterpriseId).observe(getViewLifecycleOwner(), response -> {
                progressDialog.dismiss();
                binding.progress.setVisibility(View.GONE);

                if (response == null) {
                     return;
                }
                if (response.getStatus() == 400) {
                     return;
                }
                if (response.getLists().isEmpty() || response.getLists() == null) {
                    managePaginationAndFilter();
                    return;
                }

                manageFilterBtnAndRvAndDataNotFound();
                stockLists.addAll(response.getLists());
                linearLayoutManager = new LinearLayoutManager(getContext());
                binding.storeRv.setLayoutManager(linearLayoutManager);
                StockInformationListAdapter stockListAdapter = new StockInformationListAdapter(getActivity(), stockLists, getView());
                binding.storeRv.setAdapter(stockListAdapter);

            });

        }

        if (manage == 1) {
            progressDialog.dismiss();
            binding.progress.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(getContext());
            binding.storeRv.setLayoutManager(linearLayoutManager);
            StockInformationListAdapter stockListAdapter = new StockInformationListAdapter(getActivity(), stockLists, getView());
            binding.storeRv.setAdapter(stockListAdapter);
        }


    }


    private void getPreviousFragmentData() {
        try {
            productId = getArguments().getString("productId");
            porsion = getArguments().getString("porson");
            binding.toolbar.toolbarTitle.setText(getArguments().getString("pageName"));
        } catch (Exception e) {
        }
    }

    private void managePaginationAndFilter() {
        if (isFirstLoad == 0) { // if filter time list data is  null.  so then, data_not_found will be visible
            binding.storeRv.setVisibility(View.GONE);
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
        //for filter
        // sometime filter list data came null when, data_not_found have visible,
        // And again search comes data in list by the others filter parameter.that for recycler view visible
        binding.noDataFound.setVisibility(View.GONE);
        binding.storeRv.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (manage == 0) {
            storeLists.clear();
            stockLists.clear();
        }

        storeSelectedId = null;
        zoneSelectId = null;
        brandId = null;
        categoryId = null;
        /** get previous fragment data*/
        getPreviousFragmentData();
        getAllListFromServer();
        try {
            updateCurrentUserPermission(getActivity());
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
                 return;
            }
            if (response.getStatus() == 400) {
             }
            try {
                LoginResponse loginResponse = PreferenceManager.getInstance(activity).getUserCredentials();
                if (loginResponse != null) {
                    loginResponse.setPermissions(response.getMessage());
                    loginResponse.setToken(response.getToken());
                    PreferenceManager.getInstance(activity).saveUserCredentials(loginResponse);
                }
            } catch (Exception e) {
                 Log.d("ERROR", "" + e.getMessage());
            }
        });
    }


    private void setOnClick() {
        binding.toolbar.filterBtn.setOnClickListener(this);
        binding.toolbar.addBtn.setOnClickListener(this);
        binding.stockFilter.filterSearchBtn.setOnClickListener(this);
        binding.stockFilter.resetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterBtn:
                if (binding.expandableView.isExpanded()) {
                    binding.expandableView.setExpanded(false);
                    return;
                }
                binding.expandableView.setExpanded(true);
                break;
            case R.id.filterSearchBtn:
                manage = 0;
                pageNumber = 1;
                endScroll = false;
                isFirstLoad = 0;
                stockLists.clear();
                getAllListFromServer();
                break;
            case R.id.resetBtn:
                manage = 0;
                pageNumber = 1;
                isFirstLoad = 0;
                storeSelectedId = null;
                zoneSelectId = null;
                brandId = null;
                categoryId = null;
                endScroll = false;
                binding.stockFilter.enterprise.clearSelection();
                enterpriseId = null;
                binding.stockFilter.itemNameEt.setText("");

                stockLists.clear();
                getAllListFromServer();
                break;
            case R.id.addBtn:
                try {
                    /**
                     * only miller can create store
                     */
                    String currentProfileTypeId = PreferenceManager.getInstance(getActivity()).getUserCredentials().getProfileTypeId();
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(getActivity()).getUserCredentials().getPermissions()).contains(15)) {
                        MyApplication.addStore(getActivity(), enterprizeList, getViewLifecycleOwner());
                    } else {
                        infoMessage(getActivity().getApplication(), PermissionUtil.permissionMessage);
                    }

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
                break;
        }
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
        tvTitle.setText("Do You Want to delete store ?");//set warning title
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
            brandViewModel.deleteStore(getActivity(), id).observe(getViewLifecycleOwner(), new Observer<DuePaymentResponse>() {
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
                    storeLists.clear();
                    pageNumber = 1;
                    getStoreListFromServer();
                    binding.storeRv.getAdapter().notifyDataSetChanged();
                }
            });

        });

        alertDialog.show();
    }

    @Override
    public void getSupplierPosition(int position, String id) {

    }

    @Override
    public void getDataForEditStore(int position, String enterPriseID, String storeFullName, String storeShortname, String storeAddress, String storeId) {
        enterPriseId = enterPriseID;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        AddStoreDialogLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.add_store_dialog_layout, null, false);
        AlertDialog alertDialog;
        binding.btnOk.setText("Update");
        binding.addStoreTvLevel.setText("Update Store");

        //set previous data
        binding.fullNameEt.setText("" + storeFullName);
        binding.shortNameEt.setText("" + storeShortname);
        if (storeAddress != null) {
            binding.addressEt.setText("" + storeAddress);
        }

        if (enterprizeList != null) {
            List<String> nameList = new ArrayList<>();
            nameList.clear();
            try {
                for (int i = 0; i < enterprizeList.size(); i++) {
                    if (enterPriseID != null) {
                        if (enterPriseId.equals(enterprizeList.get(i).getStoreID())) {
                            binding.enterprise.setSelection(i);
                        }
                    }
                    nameList.add("" + enterprizeList.get(i).getStoreName());
                }
            } catch (Exception e) {
            }
            binding.enterprise.setItem(nameList);
        }

        builder.setView(binding.getRoot());

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        binding.btnOk.setOnClickListener(v -> {
            if (binding.fullNameEt.getText().toString().isEmpty()) {
                binding.fullNameEt.setError("Empty");
                binding.fullNameEt.requestFocus();
                return;
            }
            if (binding.shortNameEt.getText().toString().isEmpty()) {
                binding.shortNameEt.setError("Empty");
                binding.shortNameEt.requestFocus();
                return;
            }

            storeListViewModel.editStore(getActivity(), enterPriseId, binding.fullNameEt.getText().toString(), binding.shortNameEt.getText()
                    .toString(), binding.addressEt.getText().toString(), storeId).observe(getViewLifecycleOwner(), response -> {
                if (response == null) {
                    Toasty.error(getContext(), "Something Wrong Contact to Support \n", Toasty.LENGTH_LONG).show();
                    return;
                }
                if (response.getStatus() == 400) {
                    Toasty.info(getContext(), "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                    return;
                }
                Toasty.success(getContext(), "Successful \n", Toasty.LENGTH_LONG).show();
                try {
                    storeLists.clear();
                    getStoreListFromServer();

                } catch (Exception e) {
                    Log.d("dfsaa", e.getMessage());
                }
            });

            alertDialog.dismiss();
            return;

        });
        binding.enterprise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enterPriseId = enterprizeList.get(position).getStoreID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.cancel.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
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

                        /** for Association */

                        /** for zone */
                        zoneResponseList = new ArrayList<>();
                        zoneResponseList.clear();
                        zoneList = new ArrayList<>();
                        zoneList.clear();
                        zoneResponseList.addAll(response.getAssociationList());

                        for (int i = 0; i < response.getAssociationList().size(); i++) {
                            zoneList.add("" +  response.getAssociationList().get(i).getZoneName()+"/"+ response.getAssociationList().get(i).getDisplayName());

                        }
                        binding.stockFilter.zone.setItem(zoneList);


                    }

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    @Override
    public void statusManage(int position, String id) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        storeListViewModel.statusManage(getActivity(), id).observe(getViewLifecycleOwner(), response -> {
                    if (response == null) {
                        return;
                    }

                    if (response.getStatus() == 400) {
                        return;
                    }
                    if (response.getStatus() == 500) {
                        return;
                    }
                    if (response.getStatus() == 200) {
                        successMessage(getActivity().getApplication(), "" + response.getMessage());
                        storeLists.clear();
                        getAllListFromServer();
                    }
                }
        );
    }
}