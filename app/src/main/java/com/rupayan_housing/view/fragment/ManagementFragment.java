package com.rupayan_housing.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rupayan_housing.R;
import com.rupayan_housing.adapter.CustomerChildAdapter;
import com.rupayan_housing.adapter.DashboarReportAdapter;
import com.rupayan_housing.adapter.InboxAdapter;
import com.rupayan_housing.adapter.MillerChildAdapter;
import com.rupayan_housing.adapter.MonitoringAdapter;
import com.rupayan_housing.adapter.ProductionAdapter;
import com.rupayan_housing.adapter.PurchaseAdapter;
import com.rupayan_housing.adapter.QcQaAdapter;
import com.rupayan_housing.adapter.ReconciliationSubAdapter;
import com.rupayan_housing.adapter.ReportAdapter;
import com.rupayan_housing.adapter.SaleManagementAdapter;
import com.rupayan_housing.adapter.SettingsAdapter;
import com.rupayan_housing.adapter.SubCategoryAdapter;
import com.rupayan_housing.adapter.SupplierAdapter;
import com.rupayan_housing.adapter.UserAdapter;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.MyPackagingDatabaseHelper;
import com.rupayan_housing.localDatabase.PackatingDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.InboxListResponse;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.LoginResponse;
import com.rupayan_housing.serverResponseModel.Product;
import com.rupayan_housing.serverResponseModel.SaleDeclinedList;
import com.rupayan_housing.serverResponseModel.SaleHistoryList;
import com.rupayan_housing.serverResponseModel.SalePendingList;
import com.rupayan_housing.serverResponseModel.SaleReturnHistoryList;
import com.rupayan_housing.utils.CrispUtil;
import com.rupayan_housing.utils.CustomersUtil;
import com.rupayan_housing.utils.DashBoardReportUtils;
import com.rupayan_housing.utils.HomeUtils;
import com.rupayan_housing.utils.ManagementUtils;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.utils.MonitoringUtil;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.ProductionUtils;
import com.rupayan_housing.utils.PurchaseUtill;
import com.rupayan_housing.utils.QcQaUtil;
import com.rupayan_housing.utils.ReconciliationUtils;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.utils.SaleUtil;
import com.rupayan_housing.utils.SettingsUtil;
import com.rupayan_housing.utils.SupplierUtils;
import com.rupayan_housing.utils.UserUtil;
import com.rupayan_housing.view.fragment.inbox.ClickInboxList;
import com.rupayan_housing.view.fragment.stock.StockAdapter;
import com.rupayan_housing.utils.StockUtils;
import com.rupayan_housing.viewModel.CurrentPermissionViewModel;
import com.rupayan_housing.viewModel.InboxViewModel;
import com.rupayan_housing.viewModel.PermissionViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

import static com.rupayan_housing.view.fragment.DueCollectionFragment.HIDE_KEYBOARD;

import org.jetbrains.annotations.NotNull;

public class ManagementFragment extends BaseFragment implements ClickInboxList {
    private PermissionViewModel permissionViewModel;
    private InboxViewModel inboxViewModel;
    private MyDatabaseHelper myDatabaseHelper;
    private MyPackagingDatabaseHelper helper;
    private PackatingDatabaseHelper packatingDatabaseHelper;
    private CurrentPermissionViewModel currentPermissionViewModel;

    private String root = "Management";
    private View view;
    private String previousParentItemName, inbox;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.subCategoryRv)
    RecyclerView subCategoryRv;
    @BindView(R.id.noDatasFound)
    TextView noDatasFound;
    BottomNavigationView bottomNavigationView;
    /**
     * for pagination
     */
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public static int pageNumber = 1;
    private List<Product> productList;
    private List<ListMonitorModel> monitorLists;
    private List<SalePendingList> salePendingLists;
    private List<SaleHistoryList> saleHistoryLists;
    private List<SaleReturnHistoryList> saleReturnHistoryLists;
    private List<SaleDeclinedList> saleDeclinedLists;

    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_management, container, false);
        ButterKnife.bind(this, view);
        HIDE_KEYBOARD(getActivity());
        permissionViewModel = ViewModelProviders.of(this).get(PermissionViewModel.class);
        inboxViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        currentPermissionViewModel = new ViewModelProvider(this).get(CurrentPermissionViewModel.class);


        getDataFromPreviousFragment();
        loadRecyclerViewData();
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dayBook:
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", "Inbox");
                    Navigation.findNavController(getView()).navigate(R.id.action_managementFragmentSelf, bundle);
                    break;
                case R.id.home:
                    Navigation.findNavController(getView()).navigate(R.id.action_managementFragment_to_homeFragment);
                    break;
                case R.id.chat:
                    /**item
                     * For open chat window
                     */
                    Crisp.configure(getContext(), CrispUtil.crispSecretKey);
                    Intent crispIntent = new Intent(getActivity(), ChatActivity.class);
                    startActivity(crispIntent);
                    break;
            }
            return true;
        });
        try {
            if (inbox.equals("Inbox")) {
                toolbarTitle.setText("Inbox List");


                /** for pagination **/
                subCategoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) { //check for scroll down
                            visibleItemCount = linearLayoutManager.getChildCount();
                            totalItemCount = linearLayoutManager.getItemCount();
                            pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    loading = false;
//infoMessage(getActivity().getApplication(), "Yeah");
                                    pageNumber += 1;
// Do pagination.. i.e. fetch new data
                                    loadRecyclerViewData();

                                    loading = true;
                                }
                            }
                        }
                    }
                });

            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }


        return view;
    }

    private void getDataFromPreviousFragment() {
        try {
            assert getArguments() != null;
            previousParentItemName = getArguments().getString("Item");
            inbox = getArguments().getString("portion");
        } catch (Exception e) {
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadRecyclerViewData() {

        if (inbox != null) {
            if (inbox.equals("Inbox")) {
                if (!(isInternetOn(getActivity()))) {
                    infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show();


                inboxViewModel.getInboxList(getActivity(), String.valueOf(pageNumber))
                        .observe(getViewLifecycleOwner(), response -> {
                            progressDialog.dismiss();
                            try {
                                if (response == null) {
                                    errorMessage(getActivity().getApplication(), "Something Wrong");
                                    return;
                                }
                                if (response.getStatus() == 400) {
                                    infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                    return;
                                }
                                if (response.getLists().isEmpty()) {
                                    subCategoryRv.setVisibility(View.GONE);
                                    noDatasFound.setVisibility(View.VISIBLE);
                                    return;
                                }
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                subCategoryRv.setLayoutManager(linearLayoutManager);
                                InboxAdapter adapter = new InboxAdapter(getActivity(), response.getLists(), ManagementFragment.this);
                                subCategoryRv.setAdapter(adapter);
                            } catch (Exception e) {
                                Log.d("ERROR", "" + e.getMessage());
                            }

                        });

                return;
            }
        }

        subCategoryRv.setHasFixedSize(true);
        subCategoryRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        subCategoryRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        subCategoryRv.setNestedScrollingEnabled(false);

        permissionViewModel.getAccountPermission(getActivity())
                .observe(getViewLifecycleOwner(), permission -> {
                    SubCategoryAdapter adapter;
                    if (previousParentItemName.equals(HomeUtils.report)) {
                        ReportAdapter reportAdapter = new ReportAdapter(getActivity(), ReportUtils.reportNameList(), ReportUtils.reportImageList());
                        subCategoryRv.setAdapter(reportAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }
                    /**
                     * For item management
                     */
                    if (previousParentItemName.equals(HomeUtils.itemManagement)) {
                        adapter = new SubCategoryAdapter(getActivity(), ManagementUtils.getItemManagementImageList(), ManagementUtils.getItemManagementNameList());
                        subCategoryRv.setAdapter(adapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }


                    if (previousParentItemName.equals(HomeUtils.qcQa)) {
                        QcQaAdapter qcQaAdapter = new QcQaAdapter(getActivity(), QcQaUtil.qcQaNameList(), QcQaUtil.qcQaImageList());
                        subCategoryRv.setAdapter(qcQaAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }
                    if (previousParentItemName.equals("Mill Profile")) {
                        MillerChildAdapter millerChildAdapter = new MillerChildAdapter(getActivity(), MillerUtils.getMillerNameList(), MillerUtils.getMillerImageList());

                        subCategoryRv.setAdapter(millerChildAdapter);
                        toolbarTitle.setText("Mill Profile");
                        return;
                    }
                    if (previousParentItemName.equals("Settings")) {
                        View view = getView();
                        SettingsAdapter settingsAdapter = new SettingsAdapter(getActivity(), SettingsUtil.settingNameList(), SettingsUtil.settingImageList(), view);
                        subCategoryRv.setAdapter(settingsAdapter);
                        toolbarTitle.setText("Settings");
                        return;
                    }


                    if (previousParentItemName.equals(HomeUtils.monitoring)) {
                        MonitoringAdapter monitoringAdapter = new MonitoringAdapter(getActivity(), MonitoringUtil.getMonitoringNameList(), MonitoringUtil.getMonitoringImageList());
                        subCategoryRv.setAdapter(monitoringAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }
                    if (previousParentItemName.equals(HomeUtils.customers)) {
                        CustomerChildAdapter customerAdapter = new CustomerChildAdapter(getActivity(), CustomersUtil.customerNameList(), CustomersUtil.customerImageList());
                        subCategoryRv.setAdapter(customerAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }
                    if (previousParentItemName.equals(HomeUtils.sales)) {
                        List<Integer> saleImageList = SaleUtil.saleImageList();
                        List<String> saleNameList = SaleUtil.saleNameList();


                        SaleManagementAdapter saleManagementAdapter = new SaleManagementAdapter(getActivity(), saleNameList, saleImageList);
                        subCategoryRv.setAdapter(saleManagementAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }


                    if (previousParentItemName.equals(HomeUtils.purchases)) {
                        PurchaseAdapter purchaseAdapter = new PurchaseAdapter(getActivity(), PurchaseUtill.getPurchaseName(), PurchaseUtill.getPurchaseImage());
                        subCategoryRv.setAdapter(purchaseAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;

                    }

                    if (previousParentItemName.equals(HomeUtils.production)) {
                        /**
                         * for handle all production with their child
                         */
                        ProductionAdapter purchaseAdapter = new ProductionAdapter(getActivity(), ProductionUtils.productionNameList(), ProductionUtils.productionImageList());
                        subCategoryRv.setAdapter(purchaseAdapter);
                        toolbarTitle.setText(previousParentItemName);
                        return;
                    }


                    if (previousParentItemName.equals(HomeUtils.stock)) {//here handle transfer

                        StockAdapter stockAdapter = new StockAdapter(getActivity(), StockUtils.getStockItemName(), StockUtils.getStockItemImage());
                        subCategoryRv.setAdapter(stockAdapter);
                        toolbarTitle.setText("Stock Management");
                        return;
                    }

                    if (previousParentItemName.equals(HomeUtils.reconciliation)) {
                        /**
                         * for handle all production with their child
                         */
                        ReconciliationSubAdapter reconciliationAdapter = new ReconciliationSubAdapter(getActivity(), ReconciliationUtils.getReconciliationItemName(), ReconciliationUtils.getReconciliationItemImage());
                        subCategoryRv.setAdapter(reconciliationAdapter);
                        toolbarTitle.setText("Reconciliation");
                        return;
                    }


                    if (previousParentItemName.equals(HomeUtils.user)) {
                        boolean havePermission = false;
                        if (permission.contains(PermissionUtil.UserList) || permission.contains(PermissionUtil.manageAll)) {
                            havePermission = true;
                        }
                        /**
                         * for handle all production with their child
                         */
                        UserAdapter reconciliationAdapter = new UserAdapter(getActivity(), UserUtil.userNameList(), UserUtil.userImageList(), havePermission);
                        subCategoryRv.setAdapter(reconciliationAdapter);
                        toolbarTitle.setText("User");
                        return;
                    }

                    if (previousParentItemName.equals(HomeUtils.suppliers)) {
                        /**
                         * for handle all production with their child
                         */

                        View view = getView();
                        SupplierAdapter reconciliationAdapter = new SupplierAdapter(getActivity(), SupplierUtils.supplierNameList(), SupplierUtils.supplierImageList(), view);
                        subCategoryRv.setAdapter(reconciliationAdapter);
                        toolbarTitle.setText("Supplier");
                        return;
                    }

                    if (previousParentItemName.equals(ReportUtils.dashBoardReport)) {
                        DashboarReportAdapter dashboarReportAdapter = new DashboarReportAdapter(getActivity(), DashBoardReportUtils.dashBoardReportNameList(), DashBoardReportUtils.dashBoardReeportImageList(), getView());
                        subCategoryRv.setAdapter(dashboarReportAdapter);
                        toolbarTitle.setText("Report");
                        return;
                    }


                });


        /**
         * for control home page account
         */
//        if (previousParentItemName.contains(HomeUtils.accounts)) {
    /*    permissionViewModel.getAccountPermission(getActivity()).observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> permissions) {
                List<Integer> finalAccountImage = AccountsUtil.getAccountsChildImageList();
                List<String> finalAccountTitle = AccountsUtil.getAccountsChildNameList();

                *//**
         * if user have manage all permission user can access everything
         *//*
                if (permissions.contains(PermissionUtil.manageAll)) {
                    *//**
         * now show view to recyclerview
         *//*
                    AccountsSubcategoryAdapter accountAdapter = new AccountsSubcategoryAdapter(getActivity(), finalAccountImage, finalAccountTitle);
                    subCategoryRv.setAdapter(accountAdapter);
                } else {
                    *//**
         * if don't have manage all permission then user access permission  wise option like below
         *//*
         *//**
         * for show Received due if have permission
         *//*
                    if (!permissions.contains(PermissionUtil.showDueReceived)) {//here showDueReceived = 1058 means Manage receipt
                        if (finalAccountImage.contains(AccountsUtil.receiveDueImage)) {
                            finalAccountImage.remove(AccountsUtil.receiveDueImage);
                        }
                        if (finalAccountTitle.contains(AccountsUtil.receiveDue)) {
                            finalAccountTitle.remove(AccountsUtil.receiveDue);
                        }
                    }

                    *//**
         * for show pay due if have permission
         *//*
                    if (!permissions.contains(PermissionUtil.showPayDue)) {//here 1062 means show (pay due) permission to the user
                        if (finalAccountImage.contains(AccountsUtil.payDueImage)) {
                            finalAccountImage.remove(AccountsUtil.payDueImage);
                        }
                        if (finalAccountTitle.contains(AccountsUtil.payDue)) {
                            finalAccountTitle.remove(AccountsUtil.payDue);
                        }
                    }

                    *//**
         * for show (pay due expense) if have permission
         *//*

                    if (!permissions.contains(PermissionUtil.payDueExpense)) {//here 1070 means show (pay due expense) permission to the user
                        if (finalAccountImage.contains(AccountsUtil.payDueExpenseImage)) {
                            finalAccountImage.remove(AccountsUtil.payDueExpenseImage);
                        }
                        if (finalAccountTitle.contains(AccountsUtil.payDueExpense)) {
                            finalAccountTitle.remove(AccountsUtil.payDueExpense);
                        }
                    }

                    *//**
         * for show (payment instruction) if have permission
         *//*

                    if (!permissions.contains(PermissionUtil.payInstruction)) {//here 1073 means show (payment instruction) permission to the user
                        if (finalAccountImage.contains(AccountsUtil.payInstructionImage)) {
                            finalAccountImage.remove(AccountsUtil.payInstructionImage);
                        }
                        if (finalAccountTitle.contains(AccountsUtil.payInstruction)) {
                            finalAccountTitle.remove(AccountsUtil.payInstruction);
                        }
                    }
                    *//**
         * for show (payment instruction list) if have permission
         *//*

                    if (!permissions.contains(PermissionUtil.payInstructionList)) {//here 1336 means show (payment instruction) permission to the user
                        if (finalAccountImage.contains(AccountsUtil.payInstructionListImage)) {
                            finalAccountImage.remove(AccountsUtil.payInstructionListImage);
                        }
                        if (finalAccountTitle.contains(AccountsUtil.payInstructionList)) {
                            finalAccountTitle.remove(AccountsUtil.payInstructionList);
                        }
                    }
                    *//**
         * now show view to recyclerview
         *//*
                    AccountsSubcategoryAdapter accountAdapter = new AccountsSubcategoryAdapter(getActivity(), finalAccountImage, finalAccountTitle);
                    subCategoryRv.setAdapter(accountAdapter);
                }
            }
        });*/
        assert getArguments() != null;
        toolbarTitle.setText(getArguments().getString("Item"));//set toolbar title
        return;
//        }
       /* if (previousParentItemName.equals(HomeUtils.sales)) {//only for salesRequisition
            toolbarTitle.setText(previousParentItemName);
        } else {*/
//            toolbarTitle.setText(previousParentItemName + root);
//        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.backbtn)
    public void backBtnClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            myDatabaseHelper = new MyDatabaseHelper(getContext());
            helper = new MyPackagingDatabaseHelper(getContext());
            packatingDatabaseHelper = new PackatingDatabaseHelper(getContext());
        } catch (Exception e) {
        }
        try {
            myDatabaseHelper.deleteAllData();
            helper.deleteAllData();
            packatingDatabaseHelper.deleteAllData();
            /**
             * now update current credentials
             */
            updateCurrentUserPermission(getActivity());
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }
    }

    @Override
    public void click(InboxListResponse clickResponse) {
        if (!(isInternetOn(getActivity()))) {
            infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
            return;
        }
        seenMessage(getActivity(), clickResponse);
    }

    public void seenMessage(FragmentActivity context, InboxListResponse response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.message_seen_dialog, null);
        //Set the view
        builder.setView(view);
        TextView tvTitle, tvMessage;
        ImageView imageIcon = view.findViewById(R.id.img_icon);
        tvMessage = view.findViewById(R.id.tv_message);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("Welcome To Mis ERP");//set warning title
        tvMessage.setText("" + response.getMessage().replaceAll("\\<.*?\\>", ""));
        imageIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));//set warning image
        Button bOk = view.findViewById(R.id.btn_ok);
        AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        bOk.setOnClickListener(v -> {
            if (!(isInternetOn(getActivity()))) {
                infoMessage(getActivity().getApplication(), "Please Check Your Internet Connection");
                return;
            }
            inboxViewModel.sendSeenStatus(getActivity(), response.getId())
                    .observe(getViewLifecycleOwner(), seenResponse -> {
                        try {
                            if (seenResponse == null) {
                                errorMessage(getActivity().getApplication(), "Something Wrong");
                                return;
                            }
                            if (seenResponse.getStatus() == 400) {
                                infoMessage(getActivity().getApplication(), "" + response.getMessage());
                                return;
                            }
                            alertDialog.dismiss();
                            loadRecyclerViewData();
                        } catch (Exception e) {
                            alertDialog.dismiss();
                        }
                    });
        });
        alertDialog.show();
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

}